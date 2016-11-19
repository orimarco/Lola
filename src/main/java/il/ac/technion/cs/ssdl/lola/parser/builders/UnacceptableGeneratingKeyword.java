package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.lexer.Token;
/**
 * @author Ori Marcovitch
 * @Since 2016
 * @mail sorimar@cs.technion.ac.il
 */
public abstract class UnacceptableGeneratingKeyword extends Elaborator
		implements
			GeneratingKeyword {
	/**
	 * @param t
	 */
	public UnacceptableGeneratingKeyword(Token t) {
		super(t);
	}
}
