package il.ac.technion.cs.ssdl.lola.utils;
import il.ac.technion.cs.ssdl.lola.parser.builders.SnippetToken;
import il.ac.technion.cs.ssdl.lola.parser.builders.TriviaToken;
import il.ac.technion.cs.ssdl.lola.parser.Bunny;
import il.ac.technion.cs.ssdl.lola.parser.HostBunny;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.Node;
import il.ac.technion.cs.ssdl.lola.parser.builders.Elaborator;
import il.ac.technion.cs.ssdl.lola.parser.builders.HostToken;
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

	/**
	 * @param b
	 * @return
	 */
	public static boolean hostToken(Node b) {
		return b instanceof HostToken;
	}

	/**
	 * @param ¢
	 * @return
	 */
	public static boolean hostBunny(Bunny ¢) {
		return ¢ instanceof HostBunny;
	}

	/**
	 * @param b
	 * @return
	 */
	public static boolean elaborator(Node b) {
		return b instanceof Elaborator;
	}
}
