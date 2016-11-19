package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
public class $else extends Elaborator {
	public $else(final Token token) {
		super(token);
		state = Automaton.List;
	}

	@Override
	public boolean accepts(final AST.Node b) {
		return !(b instanceof Elaborator) && state != Automaton.Done;
	}

	@Override
	public void adopt(final AST.Node b) {
		list.add(b);
	}

	@Override
	public boolean mature() {
		return state == Automaton.Done
				&& list.stream().filter(x -> !x.token.isTrivia()).count() > 0;
	}
}
