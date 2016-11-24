package il.ac.technion.cs.ssdl.lola.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import il.ac.technion.cs.ssdl.lola.parser.builders.$Find;
import il.ac.technion.cs.ssdl.lola.parser.builders.$UserDefinedKeyword;
import il.ac.technion.cs.ssdl.lola.parser.builders.Keyword;
import il.ac.technion.cs.ssdl.lola.parser.lexer.JflexLexer;
import il.ac.technion.cs.ssdl.lola.parser.lexer.Token;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp;
/**
 * @author Ori Marcovitch
 * @since Nov 23, 2016
 */
public enum wizard {
	;
	private static final String BUILDERS_PATH = "il.ac.technion.cs.ssdl.lola.parser.builders.";
	private static final String lolaEscapingCharacter = JflexLexer.lolaEscapingCharacter;;
	public static Map<String, $Find> userDefinedKeywords = new HashMap<>();

	public static Keyword newKeyword(final Token t) {
		// some reflection, to create a keyword by its name...
		final String name = t.text.replace(lolaEscapingCharacter, "");
		try {
			return (Keyword) Class.forName(BUILDERS_PATH + "$" + name).getConstructor(Token.class).newInstance(t);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			try {
				return (Keyword) Class.forName(BUILDERS_PATH + "$" + toUpperCaseClass(name)).getConstructor(Token.class)
						.newInstance(t);
			} catch (final Exception e1) {
				if (userDefinedKeywords.containsKey(name))
					return new $UserDefinedKeyword(t, userDefinedKeywords.get(name).list());
				e1.printStackTrace();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean keywordExists(final Token t) {
		final String name = t.text.replace(lolaEscapingCharacter, "");
		try {
			Class.forName(BUILDERS_PATH + "$" + name).getConstructor(Token.class).newInstance(t);
			return true;
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			try {
				Class.forName(BUILDERS_PATH + "$" + toUpperCaseClass(name)).getConstructor(Token.class).newInstance(t);
				return true;
			} catch (final Exception e1) {
				if (userDefinedKeywords.containsKey(name))
					return true;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String toUpperCaseClass(final String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public static List<RegExp> cloneList(final List<RegExp> seqRes) {
		List<RegExp> $ = new ArrayList<>();
		for (RegExp ¢ : seqRes)
			$.add(¢.clone());
		return $;
	}
}
