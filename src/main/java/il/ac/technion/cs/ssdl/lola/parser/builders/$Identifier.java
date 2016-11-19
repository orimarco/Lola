package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;
public class $Identifier extends RegExpKeyword {
	public $Identifier(final Token token) {
		super(token);
		state = Automaton.Snippet; // no snippet for RegExps...
	}

	@Override
	public boolean accepts(final Node b) {
		return state == Automaton.Snippet && b.token.isSnippet();
	}

	@Override
	public void adopt(final Node b) {
		snippet = (SnippetToken) b;
		state = Automaton.Done;
	}

	@Override
	public boolean mature() {
		return state == Automaton.Done;
	}

	@Override
	public RegExp toRegExp() {
		return new Atomic.Identifier(snippet.token.text);
	}
};
