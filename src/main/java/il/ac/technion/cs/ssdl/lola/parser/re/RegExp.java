package il.ac.technion.cs.ssdl.lola.parser.re;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
public interface RegExp {
	void apply(PythonAdapter a);

	RegExp clone();

	/** can the RE be fed by token */
	boolean eats(Bunny b);

	/** feed RE with token */
	void feed(Bunny b);

	/** does the RE match? */
	boolean satiated();

	String text();
	interface Atomic extends RegExp {
		class Any implements Atomic, Cloneable {
			protected final String snippet;
			protected String text = "";

			public Any(final String snippet) {
				this.snippet = snippet;
			}

			public Any(final String snippet, final boolean unbalanced) {
				this.snippet = snippet;
			}

			@Override
			public void apply(final PythonAdapter ¢) {
				if (snippet != null)
					¢.addIdentifier(snippet.substring(snippet.indexOf('(') + 1, snippet.lastIndexOf(')')), "'" + text + "'");
				// TODO: might not work if the text doesn't evaluate
			}

			@Override
			public Any clone() {
				final Any $ = new Any(snippet);
				$.text = text;
				return $;
			}

			@Override
			public boolean eats(final Bunny __) {
				return true;
			}

			@Override
			public void feed(final Bunny ¢) {
				text += ¢.text();
			}

			@Override
			public boolean satiated() {
				return true;
			}

			@Override
			public String text() {
				return text;
			}
		}
		class BalancedAny extends Any {
			public BalancedAny(final String snippet) {
				super(snippet);
			}

			@Override
			public BalancedAny clone() {
				final BalancedAny $ = new BalancedAny(snippet);
				$.text = text;
				return $;
			}

			@Override
			public boolean eats(final Bunny ¢) {
				return Balancing.hasHope(text + ¢.text());
			}

			@Override
			public boolean satiated() {
				return Balancing.isBalanced(text);
			}
		}
		class Empty implements Atomic, Cloneable {
			public Empty() {
			}

			@Override
			public void apply(final PythonAdapter __) {
			}

			@Override
			public Empty clone() {
				return new Empty();
			}

			@Override
			public boolean eats(final Bunny __) {
				return false;
			}

			@Override
			public void feed(final Bunny __) {
			}

			@Override
			public boolean satiated() {
				return true;
			}

			@Override
			public String text() {
				return "";
			}
		}
		class Expression implements Atomic, Cloneable {
			private String text;
			private final String snippet;

			public Expression(final String snippet) {
				this.snippet = snippet;
			}

			@Override
			public void apply(final PythonAdapter ¢) {
				if (snippet != null)
					¢.addVariable(snippet, text);
			}

			@Override
			public Expression clone() {
				final Expression $ = new Expression(snippet);
				$.text = text;
				return $;
			}

			@Override
			public boolean eats(final Bunny __) {
				throw new RuntimeException("expressions not supported yet");
			}

			@Override
			public void feed(final Bunny __) {
				throw new RuntimeException("expressions not supported yet");
			}

			@Override
			public boolean satiated() {
				throw new RuntimeException("expressions not supported yet");
			}

			@Override
			public String text() {
				return text;
			}
		}
		class Host implements Atomic, Cloneable {
			private final String text;
			private boolean done;

			public Host(final String text) {
				this.text = text;
			}

			@Override
			public void apply(final PythonAdapter __) {
			}

			@Override
			public Host clone() {
				final Host $ = new Host(text);
				$.done = done;
				return $;
			}

			@Override
			public boolean eats(final Bunny ¢) {
				return !done && ¢ instanceof HostBunny && ((HostBunny) ¢).token.text.equals(text);
			}

			@Override
			public void feed(final Bunny __) {
				done = true;
			}

			@Override
			public boolean satiated() {
				return done;
			}

			@Override
			public String text() {
				return text;
			}
		}
		class Identifier implements Atomic, Cloneable {
			private String text;
			private final String snippet;

			public Identifier(final String snippet) {
				this.snippet = snippet;
			}

			@Override
			public void apply(final PythonAdapter ¢) {
				if (snippet != null)
					¢.addIdentifier(snippet.substring(snippet.indexOf('(') + 1, snippet.lastIndexOf(')')), "'" + text + "'");
			}

			@Override
			public Identifier clone() {
				final Identifier $ = new Identifier(snippet);
				$.text = text;
				return $;
			}

			@Override
			public boolean eats(final Bunny ¢) {
				return !(¢ instanceof TriviaBunny) && text == null && "identifier".equals(((HostBunny) ¢).token.category.name);
			}

			@Override
			public void feed(final Bunny ¢) {
				text = ((HostBunny) ¢).token.text;
			}

			@Override
			public boolean satiated() {
				return text != null;
			}

			@Override
			public String text() {
				return text;
			}
		}
		class Literal implements Atomic, Cloneable {
			private String text;
			private final String snippet;

			public Literal(final String snippet) {
				this.snippet = snippet;
			}

			@Override
			public void apply(final PythonAdapter a) {
				if (snippet == null)
					return;
				final String name = snippet.substring(snippet.indexOf('(') + 1, snippet.lastIndexOf(')'));
				if (text.startsWith("'"))
					a.addCharVariable(name, text);
				else if (!text.startsWith("\""))
					a.addVariable(name, text);
				else
					a.addStringVariable(name, text);
			}

			@Override
			public Literal clone() {
				final Literal $ = new Literal(snippet);
				$.text = text;
				return $;
			}

			@Override
			public boolean eats(final Bunny ¢) {
				return !(¢ instanceof TriviaBunny) && text == null && CategoriesHierarchy
						.isClassifiedAs(((HostBunny) ¢).token.category, CategoriesHierarchy.getCategory("literal"));
			}

			@Override
			public void feed(final Bunny ¢) {
				text = ((HostBunny) ¢).token.text;
			}

			@Override
			public boolean satiated() {
				return text != null;
			}

			@Override
			public String text() {
				return text;
			}
		}
		// TODO: Trivia...
		class Trivia implements Atomic, Cloneable {
			private String text;
			private boolean done;

			public Trivia(final String text) {
				this.text = text;
			}

			@Override
			public void apply(final PythonAdapter __) {
			}

			@Override
			public Trivia clone() {
				final Trivia $ = new Trivia(text);
				$.done = done;
				return $;
			}

			@Override
			public boolean eats(final Bunny ¢) {
				return text == null && ¢ instanceof TriviaBunny && ((TriviaBunny) ¢).text().equals(text);
			}

			@Override
			public void feed(final Bunny __) {
				done = true;
			}

			@Override
			public boolean satiated() {
				return done;
			}

			@Override
			public String text() {
				return text == null ? "" : text;
			}
		}
	}
	abstract class Composite implements RegExp, Cloneable {
		public final ArrayList<RegExp> children = new ArrayList<>();

		public Composite() {
		}

		@Override
		public abstract Composite clone();
	}
}
