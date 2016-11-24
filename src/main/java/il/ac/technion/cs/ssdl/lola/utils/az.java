package il.ac.technion.cs.ssdl.lola.utils;
import il.ac.technion.cs.ssdl.lola.parser.builders.$Find;
import il.ac.technion.cs.ssdl.lola.parser.builders.SnippetToken;
import il.ac.technion.cs.ssdl.lola.parser.builders.TriviaToken;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.Node;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
/**
 * @author Ori Marcovitch
 * @since Nov 24, 2016
 */
public enum az {
	;
	/**
	 * @param b JD
	 * @return
	 */
	public static SnippetToken snippetToken(Node b) {
		return b == null ? null : (SnippetToken) b;
	}

	/**
	 * @param b JD
	 * @return
	 */
	public static TriviaToken triviaToken(Node b) {
		return b == null ? null : (TriviaToken) b;
	}

	/**
	 * @param b JD
	 * @return
	 */
	public static $Find $Find(Node b) {
		return b == null ? null : ($Find) b;
	}
}
