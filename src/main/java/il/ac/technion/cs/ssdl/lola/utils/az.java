package il.ac.technion.cs.ssdl.lola.utils;
import il.ac.technion.cs.ssdl.lola.parser.Bunny;
import il.ac.technion.cs.ssdl.lola.parser.HostBunny;
import il.ac.technion.cs.ssdl.lola.parser.builders.$Find;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.Node;
import il.ac.technion.cs.ssdl.lola.parser.builders.Builder;
import il.ac.technion.cs.ssdl.lola.parser.builders.Elaborator;
import il.ac.technion.cs.ssdl.lola.parser.builders.SnippetToken;
import il.ac.technion.cs.ssdl.lola.parser.builders.TriviaToken;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExpable;
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

	/**
	 * @param ¢
	 * @return
	 */
	public static HostBunny hostBunny(Bunny ¢) {
		return ¢ == null ? null : (HostBunny) ¢;
	}

	/**
	 * @param b
	 * @return
	 */
	public static Elaborator elaborator(Node b) {
		return b == null ? null : (Elaborator) b;
	}

	/**
	 * @param b
	 * @return
	 */
	public static Builder builder(Node b) {
		return b == null ? null : (Builder) b;
	}

	/**
	 * @param ¢
	 * @return
	 */
	public static RegExpable regExpable(Node ¢) {
		return ¢ == null ? null : (RegExpable) ¢;
	}
}
