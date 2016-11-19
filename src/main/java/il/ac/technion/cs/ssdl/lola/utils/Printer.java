package il.ac.technion.cs.ssdl.lola.utils;
import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.parser.tokenizer.*;
public class Printer {
	private static final boolean AST = false;
	private static final boolean RE = false;
	private static final boolean Chain = false;
	private static final boolean Python = false;
	private static final boolean Tokens = false;

	public static void printAST(final Node n) {
		if (!AST)
			return;
		println(n.name());
		printAST(n, 0);
	}

	public static void printChain(final Chain<Bunny, Lexi> c) {
		if (!Chain)
			return;
		c.printChain();
	}

	public static void printCommand(final String s) {
		if (!Python)
			return;
		System.out.println(s);
	}

	public static void printRe(final RegExp e) {
		if (!RE)
			return;
		printRe(e, 0);
	}

	public static void printTokens(final Tokenizer t) {
		if (!Tokens)
			return;
		t.printTokens();
	}

	private static void printAST(final Node n, int tabs) {
		String allignment = tabs <= 0
				? ""
				: String.valueOf(new char[tabs]).replace("\0", "-");
		if (n.snippet() != null)
			println(allignment + "[" + n.snippet().getText() + "]");
		++tabs;
		allignment = String.valueOf(new char[tabs]).replace("\0", "-");
		if (n.list() != null)
			for (final Node b : n.list()) {
				if (b instanceof TriviaToken)
					println(allignment + ("\n".equals(b.text())
							? "<\\n>"
							: "\r".equals(b.text())
									? "<\\r>"
									: "\r\n".equals(b.text())
											? "<\\r\\n>"
											: "\t".equals(b.text())
													? "<\\t>"
													: "<" + b.text() + ">"));
				if (b instanceof HostToken) {
					println(allignment + ((HostToken) b).getText());
					continue;
				}
				if (b instanceof Keyword)
					println(allignment + b.name());
				printAST(b, tabs);
			}
		if (n.elaborators() != null)
			for (final Elaborator b : n.elaborators()) {
				println(allignment + b.name());
				printAST(b, tabs);
			}
	}

	private static void println(final Object o) {
		System.out.println(o);
	}

	private static void printRe(final RegExp e, final int tabs) {
		final String allignment = tabs <= 0
				? ""
				: new String(new char[tabs]).replace("\0", "-");
		println(allignment + e.getClass().getSimpleName());
		println(allignment + ":" + e.text());
		// else
		// println(allignment + "[" + ((RegExp.Atomic) e).text() + "]");
		if (!(e instanceof RegExp.Composite)) {
			if (e.text() != null && e.text().contains("null"))
				System.out.println("BUG HERE");
		} else if (e instanceof sequence)
			for (final Branch br : ((sequence) e).branches) {
				if (br.satiated()) {
					for (final RegExp re : br.res)
						printRe(re, tabs + 1);
					break;
				}
				if (e instanceof OneOrMore)
					for (final KlineBranch kbr : ((OneOrMore) e).branches) {
						if (kbr.satiated()) {
							for (final RegExp re : kbr.res)
								printRe(re, tabs + 1);
							break;
						}
						for (final RegExp c : ((RegExp.Composite) e).children)
							printRe(c, tabs + 1);
					}
			}
	}
}
