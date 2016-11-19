package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
public class $elseIf extends Elaborator {
	public $elseIf(final Token token) {
		super(token);
	}

	@Override
	public boolean accepts(final AST.Node b) {
		switch (state) {
			case List :
				return !(b instanceof Elaborator) && state != Automaton.Done;
			case Snippet :
				return true;
			default :
				return false;
		}
	}

	@Override
	public void adopt(final AST.Node b) {
		switch (state) {
			case List :
				list.add(b);
				break;
			case Snippet :
				snippet = (SnippetToken) b;
				state = Automaton.List;
				break;
			default :
				break;
		}
	}
}
