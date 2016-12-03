package il.ac.technion.cs.ssdl.lola.parser.tokenizer;
import static il.ac.technion.cs.ssdl.lola.utils.wizard.keywordExists;
import static il.ac.technion.cs.ssdl.lola.utils.wizard.newKeyword;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import il.ac.technion.cs.ssdl.lola.parser.CategoriesHierarchy;
import il.ac.technion.cs.ssdl.lola.parser.builders.SnippetToken;
import il.ac.technion.cs.ssdl.lola.parser.builders.$UserDefinedKeyword;
import il.ac.technion.cs.ssdl.lola.parser.lexer.JflexLexer;
import il.ac.technion.cs.ssdl.lola.parser.lexer.Token;
import il.ac.technion.cs.ssdl.lola.utils.Printer;
public class Tokenizer implements Iterable<Token> {
	public static String lolaEscapingCharacter = JflexLexer.lolaEscapingCharacter;
	private ArrayList<Token> tokens = new ArrayList<>();
	private int idx;

	public Tokenizer(final Reader stream) {
		try {
			readAllTokens(stream);
			Printer.printTokens(this);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Iterator<Token> iterator() {
		final TokenizerIterator $ = new TokenizerIterator();
		$.tokenizer = this;
		return $;
	}

	public Token next_token() {
		return idx >= tokens.size() ? null : tokens.get(idx++);
	}

	public void printTokens() {
		for (final Token ¢ : tokens)
			System.out.print((¢.isHost() ? "<" : ¢.isKeyword() ? "{" : ¢.isSnippet() ? "(" : "[")
					+ (isTrivia(¢) ? " " : ¢.text + "," + ¢.category.name)
					+ (¢.isHost() ? ">" : ¢.isKeyword() ? "}" : ¢.isSnippet() ? ")" : "]"));
		System.out.println("");
	}

	public void unget() {
		--idx;
	}

	private void consumeKeyword(final ListIterator<Token> it) throws IOException {
		Token t = it.next();
		tokens.add(t);
		consumeSnippetIfCan(it, t);
	}

	private static Token newSnippetToken(final Token ¢) {
		return Token.newSnippetToken(
				new Token(¢.row, ¢.column + ¢.text.length(), ¢.text, CategoriesHierarchy.getCategory("package_snippet")));
	}

	private static boolean acceptsSnippet(final Token kw, final SnippetToken ¢) {
		return keywordExists(kw) && newKeyword(kw).accepts(¢)
				|| !keywordExists(kw) && new $UserDefinedKeyword(kw, new ArrayList<>()).accepts(¢);
	}

	private void consumeSnippetIfCan(final ListIterator<Token> it, final Token kw) throws IOException {
		Token t = getNextNotTrivia(it);
		if (t != null)
			if (isPackageSnippet(t))
				tokens.add(newSnippetToken(t));
			else if (!mightBeSnippet(t))
				it.previous();
			else if (!acceptsSnippet(kw, createDummySnippet(t)))
				it.previous();
			else if ("lstring".equals(t.category.name))
				addLstringSnippet(t);
			else
				switch (t.text.charAt(0)) {
					case '{' :
						consumeCurlyBracketsSnippet(it, t);
						return;
					case '(' :
						consumeParenthesisSnippet(it, t);
						return;
				}
	}

	private static boolean isPackageSnippet(Token $) {
		return $.snippet != null;
	}

	/**
	 * @param $
	 * @return
	 */
	private static boolean mightBeSnippet(Token $) {
		return Arrays.asList('(', '{', '"').contains($.text.charAt(0));
	}

	/**
	 * @param ¢
	 * @return
	 * @throws IOException
	 */
	private Token getNextNotTrivia(ListIterator<Token> ¢) throws IOException {
		while (¢.hasNext()) {
			Token $ = ¢.next();
			if (!isTrivia($))
				return $;
			tokens.add($);
		}
		return null;
	}

	/**
	 * @param ¢
	 * @return
	 */
	private static SnippetToken createDummySnippet(Token ¢) {
		String str = null;
		char c = ¢.text.charAt(0);
		switch (c) {
			case '(' :
				str = "(dummy)";
				break;
			case '{' :
				str = "{dummy}";
				break;
			case '"' :
				str = "\"dummy\"";
				break;
			default :
				return null;
		}
		return new SnippetToken(new Token(0, 0, str, CategoriesHierarchy.getCategory("snippet")));
	}

	/**
	 * consumes all following trivia and concatenates it to one
	 */
	private void consumeTrivia(ListIterator<Token> ¢) throws IOException {
		if (!¢.hasNext())
			return;
		Token $ = ¢.next();
		if (!$.isTrivia()) {
			¢.previous();
			return;
		}
		while (¢.hasNext()) {
			Token t = ¢.next();
			if (!isTrivia(t)) {
				¢.previous();
				break;
			}
			$.text += t.text;
		}
		tokens.add($);
	}

	private void consumeBracketsSnippet(final ListIterator<Token> it, Token t, final String opener, final String closer)
			throws IOException {
		int counter = 1;
		t = Token.newSnippetToken(t);
		int lp_counter = 1;
		while (lp_counter != 0 && it.hasNext()) {
			String text = it.next().text;
			++counter;
			t.text += text;
			if (opener.equals(text))
				++lp_counter;
			if (closer.equals(text))
				--lp_counter;
		}
		if (lp_counter == 0)
			tokens.add(t);
		else
			for (int ¢ = 0; ¢ < counter; ++¢)
				it.previous();
	}

	private void consumeCurlyBracketsSnippet(final ListIterator<Token> it, final Token t) throws IOException {
		consumeBracketsSnippet(it, t, "{", "}");
	}

	private void addLstringSnippet(final Token ¢) {
		tokens.add(Token.newSnippetToken(¢));
	}

	private void consumeParenthesisSnippet(final ListIterator<Token> it, final Token t) throws IOException {
		consumeBracketsSnippet(it, t, "(", ")");
	}

	public void pushTokens(final Reader stream) throws IOException {
		tokens.addAll(idx, new Tokenizer(stream).tokens);
	}

	/** reads all tokens :) */
	private void readAllTokens(final Reader stream) throws IOException {
		final JflexLexer jfl = new JflexLexer(stream);
		final List<Token> ts = new ArrayList<>();
		for (Token ¢ = jfl.next_token_safe(); ¢ != null; ¢ = jfl.next_token_safe())
			ts.add(¢);
		for (ListIterator<Token> it = ts.listIterator(); it.hasNext();) {
			Token ¢ = it.next();
			if (isKeyword(¢)) {
				it.previous();
				consumeKeyword(it);
				continue;
			}
			if (isTrivia(¢)) {
				it.previous();
				consumeTrivia(it);
				continue;
			}
			tokens.add(¢);
		}
	}

	private static boolean isTrivia(Token ¢) {
		return ¢.isTrivia();
	}

	private static boolean isKeyword(final Token ¢) {
		return ¢.text.startsWith(lolaEscapingCharacter);
	}
	public class TokenizerIterator implements Iterator<Token> {
		Tokenizer tokenizer;

		@Override
		public boolean hasNext() {
			final Token t = tokenizer.next_token();
			tokenizer.unget();
			return t != null;
		}

		@Override
		public Token next() {
			return tokenizer.next_token();
		}
	}
}
