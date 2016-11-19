package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
public class $note extends Elaborator {
	public $note(final Token token) {
		super(token);
		state = Automaton.Snippet;
	}

	@Override
	public boolean accepts(final AST.Node b) {
		return b instanceof SnippetToken || b instanceof TriviaToken;
	}

	@Override
	public void adopt(final AST.Node b) {
		if (b instanceof TriviaToken)
			return;
		snippet = (SnippetToken) b;
		state = Automaton.Done;
	}
};
