package il.ac.technion.cs.ssdl.lola.parser;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.parser.tokenizer.*;
import il.ac.technion.cs.ssdl.lola.utils.*;
import static il.ac.technion.cs.ssdl.lola.utils.wizard.*;
public class Parser {
	private final Tokenizer tokenizer;
	private final Stack<Builder> stack = new Stack<>();
	private PythonAdapter pyAdapter = new PythonAdapter();

	public Parser(final Reader stream) {
		tokenizer = new Tokenizer(stream);
	}

	/**
	 * [[SuppressWarningsSpartan]]
	 */
	/* meanwhile this function is for testing */
	public List<Lexi> directives() throws IOException {
		final List<Lexi> $ = new ArrayList<>();
		Token t;
		while ((t = tokenizer.next_token()) != null)
			if (t.isKeyword())
				$.add(directive(wizard.newKeyword(t)));
		return $;
	}

	public List<String> parse() throws IOException {
		final Chain<Bunny, Lexi> chain = generateChain();
		parse(chain);
		return chain2stringList(chain);
	}

	public String parseExample(final Lexi l, final String example) throws IOException {
		final Chain<Bunny, Lexi> chain = new Chain<>(string2bunnies(example));
		chain.addFirst(l);
		parse(chain);
		return chain2string(chain);
	}

	public List<Bunny> string2bunnies(final String ¢) throws IOException {
		return tokenizerToBunnies(new Tokenizer(new StringReader(¢)));
	}

	private boolean anyAccepts(final Keyword kw) {
		for (final Builder ¢ : stack)
			if (¢.accepts(kw))
				return true;
		return false;
	}

	/* Execute rule and add new tokens to tokenizer. */
	private void applyImmediateDirective(final GeneratingKeyword kw) throws IOException {
		Printer.printTokens(tokenizer);
		tokenizer.pushTokens(new StringReader(kw.generate(new PythonAdapter())));
		Printer.printTokens(tokenizer);
	}

	/** meanwhile, this seems to be just the #Find keyword */
	private void applyLexi(final Matcher ruller) {
		final $Find kw = az.$Find(ruller.lexi.keyword);
		Printer.printRe(ruller.re);
		ruller.re.apply(pyAdapter);
		for (final Elaborator ¢ : kw.elaborators)
			if (¢ instanceof ExecutableElaborator)
				((ExecutableElaborator) ¢).execute(ruller.interval(), pyAdapter, this);
		pyAdapter.afterLexi();
		ruller.interval().earmark();
	}

	/**
	 * This is the FixedPoint algorithm. Kind of. Gets a chain of bunnies and
	 * apply lexies that are in it until no lexi can be applied.
	 */
	private void parse(final Chain<Bunny, Lexi> b) {
		for (boolean again = true; again;) {
			again = false;
			final List<Lexi> directives = new ArrayList<>();
			final List<Matcher> matchers = new ArrayList<>();
			final List<Anchor> anchors = new ArrayList<>();
			final List<Anchor> satiatedAnchors = new ArrayList<>();
			b.printChain();
			for (final Chain<Bunny, Lexi>.Node node : b) {
				if (node.get() instanceof Lexi) {
					directives.add((Lexi) node.get());
					continue;
				}
				if (node.get() instanceof TriviaBunny) {
					feedTrivia(matchers, node.get());
					continue;
				}
				if (!(node.get() instanceof TriviaBunny))
					startNewMatchers(b, directives, matchers, anchors, node);
				final List<Matcher> satiatedMatchers = feedAndGetSatiated(matchers, node);
				satiatedAnchors.addAll(azListAnchor(feedAndGetSatiated(anchors, node)));
				for (final Anchor a : satiatedAnchors)
					if (matchers.stream().filter(m -> m.lexi == a.lexi && m.startsBeforeOrTogether(a)).count() == 0)
						a.explode();
				if (!satiatedMatchers.isEmpty()) {
					applyLexi(getRuller(satiatedMatchers));
					again = true;
					break;
				}
			}
			if (!again && !satiatedAnchors.isEmpty())
				satiatedAnchors.get(0).explode();
		}
	}

	/**
	 * @param ms
	 */
	private static void feedTrivia(List<Matcher> ms, Bunny ¢) {
		ms.stream().forEach(m -> m.feedTrivia(¢));
	}

	/**
	 * @param ¢
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Collection<? extends Anchor> azListAnchor(List<Matcher> ¢) {
		return (List<Anchor>) (List<?>) ¢;
	}

	private static String chain2string(final Chain<Bunny, Lexi> b) {
		final List<String> li = chain2stringList(b);
		String $ = "";
		for (final String ¢ : li)
			$ += ¢;
		return $;
	}

	private static List<String> chain2stringList(final Chain<Bunny, Lexi> b) {
		final List<String> $ = new ArrayList<>();
		for (final Chain<Bunny, Lexi>.Node ¢ : b)
			if (!(¢.get() instanceof Lexi))
				$.add(¢.get().text());
		return $;
	}

	/**
	 * Generates a lexi.
	 * 
	 * @param c - the keyword that initiated the lexi <br>
	 *          [[SuppressWarningsSpartan]]
	 */
	private Lexi directive(final Keyword c) throws IOException {
		stack.push(c);
		Token t;
		while ((t = tokenizer.next_token()) != null)
			if (isLeaf(t)) {
				if (!percolate(t.isSnippet() && stack.peek().accepts(new SnippetToken(t))
						? new SnippetToken(t)
						: !t.isTrivia() ? new HostToken(t) : new TriviaToken(t)) && stack.isEmpty()) {
					tokenizer.unget();
					Printer.logAST(c);
					return new Lexi(c);
				}
			} else { // t is keyword
				final Keyword kw = newKeyword(t);
				if (!anyAccepts(kw)) {
					tokenizer.unget();
					break;
				}
				stack.push(kw);
			}
		while (!stack.isEmpty()) {
			final Builder b = stack.pop();
			b.done();
			percolate(b);
		}
		Printer.logAST(c);
		return new Lexi(c);
	}

	private static List<Matcher> feedAndGetSatiated(final List<? extends Matcher> ms, final Chain<Bunny, Lexi>.Node n) {
		final List<Matcher> $ = new ArrayList<>();
		final List<Matcher> toRemove = new ArrayList<>();
		for (final Matcher ¢ : ms)
			if (!¢.eats(n.get()))
				toRemove.add(¢);
			else {
				¢.feed(n.get());
				if (¢.satiated() && !¢.interval().earmarked())
					$.add(¢);
			}
		for (final Matcher ¢ : toRemove)
			ms.remove(¢);
		return $;
	}

	/**
	 * Generates a chain of bunnies (Host tokens + Lola directives) We start with
	 * the tokenizer and whenever we come up an immedaite directive we execute it,
	 * resulting in a new stream of tokens to parse, which we add to the tokenizer
	 * at the place where the lexi was found.
	 */
	private Chain<Bunny, Lexi> generateChain() throws IOException {
		return new Chain<>(tokenizerToBunnies(tokenizer));
	}

	private static Matcher getRuller(final List<Matcher> candidates) {
		return candidates.stream().reduce((x, y) -> y.interval().strictlyContainedIn(x.interval()) ? y : x).get();
	}

	private static boolean isLeaf(final Token b) {
		return b.isSnippet() || b.isTrivia() || b.isHost();
	}

	private boolean percolate(AST.Node c) {
		while (!stack.isEmpty()) {
			final Builder b = stack.peek();
			if (c.column() < b.column() || !b.accepts(c)) {
				b.done();
				stack.pop();
				percolate(b);
				continue;
			}
			b.adopt(c);
			if (!b.mature())
				return true;
			stack.pop();
			c = b;
			if (stack.isEmpty())
				return true;
		}
		return false;
	}

	private static void startNewMatchers(final Chain<Bunny, Lexi> b, final List<Lexi> directives, final List<Matcher> ms,
			final List<Anchor> as, final Chain<Bunny, Lexi>.Node n) {
		ms.addAll(directives.stream().map(le -> new Matcher(le, b, n.before())).collect(Collectors.toList()));
		as.addAll(directives.stream().filter(le -> le.hasAnchor()).map(le -> new Anchor(le, b, n.before()))
				.collect(Collectors.toList()));
	}

	private List<Bunny> tokenizerToBunnies(final Tokenizer tokenizer) throws IOException {
		final List<Bunny> $ = new ArrayList<>();
		while (true) {
			final Token t = tokenizer.next_token();
			if (t == null)
				break;
			if (!t.isKeyword())
				$.add(t.isHost() ? new HostBunny(t) : new TriviaBunny(t));
			else {
				final Lexi lexi = directive(newKeyword(t));
				if (lexi.isImmediate())
					applyImmediateDirective((GeneratingKeyword) lexi.keyword);
				else {
					for (final Elaborator ¢ : lexi.keyword.elaborators)
						if (¢ instanceof $example)
							(($example) ¢).checkExample(this, lexi);
					$.add(lexi);
					if (lexi.keyword.snippet() != null)
						userDefinedKeywords.put(lexi.keyword.snippet().getExpression(), ($Find) lexi.keyword);
				}
			}
		}
		return $;
	}
}