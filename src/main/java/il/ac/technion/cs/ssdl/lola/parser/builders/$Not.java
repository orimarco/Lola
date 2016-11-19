package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
public class $Not extends RegExpKeyword {
	public $Not(final Token token) {
		super(token);
	}

	@Override
	public RegExp toRegExp() {
		return new not(((RegExpable) list.get(0)).toRegExp());
	}
}
