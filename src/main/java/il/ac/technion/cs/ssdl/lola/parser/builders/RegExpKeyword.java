package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.utils.az;
import il.ac.technion.cs.ssdl.lola.utils.iz;
public abstract class RegExpKeyword extends Keyword implements RegExpable {
	public RegExpKeyword(final Token token) {
		super(token);
		state = Automaton.List; // no snippet for RegExps...
	}

	@Override
	public boolean accepts(final Node b) {
		if (b instanceof $Find)
			return false;
		if (iz.triviaToken(b))
			return true;
		switch (state) {
			case Elaborators :
				return expectedElaborators.contains(b.name());
			case List :
				return !iz.elaborator(b) || expectedElaborators.contains(b.name());
			default :
				return false;
		}
	}

	@Override
	public void adopt(final Node b) {
		if (!iz.triviaToken(b))
			switch (state) {
				case Elaborators :
					elaborators.add((Elaborator) b);
					break;
				case List :
					if (!iz.elaborator(b))
						list.add(b);
					else {
						elaborators.add(az.elaborator(b));
						state = Automaton.Elaborators;
					}
					break;
				default :
					break;
			}
	}

	@Override
	public boolean mature() {
		return state == Automaton.Done && !list.isEmpty();
	}
}
