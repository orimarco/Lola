package il.ac.technion.cs.ssdl.lola.utils;
import il.ac.technion.cs.ssdl.lola.parser.builders.SnippetToken;
import il.ac.technion.cs.ssdl.lola.parser.builders.TriviaToken;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.Node;
/**
 * @author Ori Marcovitch
 * @since Nov 24, 2016
 */
public enum iz {
	;
	/**
	 * @param b
	 * @return
	 */
	public static boolean snippetToken(Node b) {
		return b instanceof SnippetToken;
	}

	/**
	 * @param b
	 * @return
	 */
	public static boolean triviaToken(Node b) {
		return b instanceof TriviaToken;
	}
}
