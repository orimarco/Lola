package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;
import il.ac.technion.cs.ssdl.lola.utils.iz;
public class $Any extends RegExpKeyword implements Unbalanceable {
	private boolean unbalanced;

	public $Any(final Token token) {
		super(token);
		state = Automaton.Snippet; // no snippet for RegExps...
	}

	@Override
	public boolean accepts(final Node b) {
		return state == Automaton.Snippet && iz.snippetToken(b);
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
	public void setUnbalanced() {
		unbalanced = true;
	}

	@Override
	public RegExp toRegExp() {
		return unbalanced
				? new Atomic.Any(snippet == null ? null : snippet.token.text)
				: new Atomic.BalancedAny(snippet == null ? null : snippet.token.text);
	}
};
