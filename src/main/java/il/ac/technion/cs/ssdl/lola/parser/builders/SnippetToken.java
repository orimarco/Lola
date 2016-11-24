package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import com.google.common.collect.*;

import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.utils.*;
public class SnippetToken extends AST.Node {
	private static Map<Type, Pair<String, String>> type2string = ImmutableMap.of(
			Type.Parentheses, new Pair<>("(", ")"), Type.CurlyBrackets,
			new Pair<>("{", "}"), Type.String, new Pair<>("\"", "\""));
	public Type type;

	public SnippetToken(final Token token) {
		super(token);
		type = token.text.startsWith("{")
				? Type.CurlyBrackets
				: token.text.startsWith("(")
						? Type.Parentheses
						: !token.text.startsWith("\"") ? Type.PackagePath : Type.String;
	}

	/**
	 * @return text without parenthesis\apostrophes\whatever wraps the snippet.
	 */
	public String getExpression() {
		return type == Type.PackagePath
				? packageToFile(token.text)
				: token.text.substring(token.text.indexOf(type2string.get(type).x) + 1,
						token.text.lastIndexOf(type2string.get(type).y));
	}

	public String getText() {
		return token.text;
	}

	private static String packageToFile(final String packageName) {
		return "./lola_libs/" + packageName.replace(".", "/") + ".lola";
	}
	enum Type {
		Parentheses, CurlyBrackets, String, PackagePath
	}
}