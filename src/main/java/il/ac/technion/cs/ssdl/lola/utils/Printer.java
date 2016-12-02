package il.ac.technion.cs.ssdl.lola.utils;
import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.Elaborator;
import il.ac.technion.cs.ssdl.lola.parser.builders.HostToken;
import il.ac.technion.cs.ssdl.lola.parser.builders.Keyword;
import il.ac.technion.cs.ssdl.lola.parser.builders.TriviaToken;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.parser.tokenizer.*;
public class Printer {
	private static final boolean AST = false;
	private static final boolean RE = false;
	private static final boolean Chain = false;
	private static final boolean Python = false;
	private static final boolean Tokens = false;

	public static void logAST(final Node ¢) {
		if (!AST)
			return;
		println(¢.name());
		printAST(¢, 0);
	}

	public static void printAST(final Node ¢) {
		println(¢.name());
		printAST(¢, 0);
	}

	public static void printChain(final Chain<Bunny, Lexi> ¢) {
		if (Chain)
			¢.printChain();
	}

	public static void printCommand(final String ¢) {
		if (Python)
			System.out.println(¢);
	}

	public static void printRe(final RegExp ¢) {
		if (RE)
			printRe(¢, 0);
	}

	public static void printTokens(final Tokenizer ¢) {
		if (Tokens)
			¢.printTokens();
	}

	private static void printAST(final Node n, int tabs) {
		String allignment = tabs <= 0 ? "" : String.valueOf(new char[tabs]).replace("\0", "-");
		if (n.snippet() != null)
			println(allignment + "[" + n.snippet().getText() + "]");
		++tabs;
		allignment = String.valueOf(new char[tabs]).replace("\0", "-");
		if (n.list() != null)
			for (final Node b : n.list()) {
				if (b instanceof TriviaToken)
					println(allignment + "<" + ("\n".equals(b.text()) ? "\\n>" : ("\r".equals(b.text())
							? "\\r>"
							: ("\r\n".equals(b.text()) ? "\\r\\n>" : (("\t".equals(b.text()) ? "\\t" : b.text() + "") + ">")))));
				if (b instanceof HostToken) {
					println(allignment + ((HostToken) b).getText());
					continue;
				}
				if (b instanceof Keyword)
					println(allignment + b.name());
				printAST(b, tabs);
			}
		if (n.elaborators() != null)
			for (final Elaborator ¢ : n.elaborators()) {
				println(allignment + ¢.name());
				printAST(¢, tabs);
			}
	}

	private static void println(final Object ¢) {
		System.out.println(¢);
	}

	private static void printRe(final RegExp x, final int tabs) {
		final String allignment = tabs <= 0 ? "" : String.valueOf(new char[tabs]).replace("\0", "-");
		println(allignment + x.getClass().getSimpleName());
		println(allignment + ":" + x.text());
		// else
		// println(allignment + "[" + ((RegExp.Atomic) e).text() + "]");
		if (!(x instanceof RegExp.Composite)) {
			if (x.text() != null && x.text().contains("null"))
				System.out.println("BUG HERE");
		} else if (x instanceof sequence)
			for (final Branch br : ((sequence) x).branches) {
				if (br.satiated()) {
					for (final RegExp ¢ : br.res)
						printRe(¢, tabs + 1);
					break;
				}
				if (x instanceof OneOrMore)
					for (final KlineBranch kbr : ((OneOrMore) x).branches) {
						if (kbr.satiated()) {
							for (final RegExp ¢ : kbr.res)
								printRe(¢, tabs + 1);
							break;
						}
						for (final RegExp c : ((RegExp.Composite) x).children)
							printRe(c, tabs + 1);
					}
			}
	}
}
