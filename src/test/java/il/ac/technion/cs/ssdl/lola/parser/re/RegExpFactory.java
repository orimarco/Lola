package il.ac.technion.cs.ssdl.lola.parser.re;
import java.io.IOException;

import il.ac.technion.cs.ssdl.lola.parser.Bunny;
import il.ac.technion.cs.ssdl.lola.parser.CategoriesHierarchy;
import il.ac.technion.cs.ssdl.lola.parser.HostBunny;
import il.ac.technion.cs.ssdl.lola.parser.Parser;
import il.ac.technion.cs.ssdl.lola.parser.lexer.Token;
/**
 * @author Ori Marcovitch
 * @since Nov 24, 2016
 */
public enum RegExpFactory {
	;
	public static RegExp newRegExp(final String s) {
		try {
			return new Parser(s).directives().get(0).toRegExp();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bunny bunny(String ¢) {
		return new HostBunny(new Token(0, 0, ¢, CategoriesHierarchy.getCategory("lstring")));
	}

	public static Bunny identifier(String ¢) {
		return new HostBunny(new Token(0, 0, ¢, CategoriesHierarchy.getCategory("identifier")));
	}
}
