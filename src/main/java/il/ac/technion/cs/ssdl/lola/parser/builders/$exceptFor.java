package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
public class $exceptFor extends RegExpElaborator {
	public $exceptFor(final Token token) {
		super(token);
	}

	@Override
	public RegExp toRegExp() {
		return new not(son.toRegExp());
	}
};
