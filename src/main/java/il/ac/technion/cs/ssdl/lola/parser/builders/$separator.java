package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
public class $separator extends Elaborator implements RegExpable {
	public String separator;

	public $separator(final Token token) {
		super(token);
	}

	@Override
	public boolean accepts(final Node b) {
		return separator == null;
	}

	@Override
	public void adopt(final AST.Node b) {
		if (b.token.isTrivia())
			return;
		list.add(b);
		separator = b.token.text;
		state = Automaton.Done;
	}

	@Override
	public RegExp toRegExp() {
		return new RegExp.Atomic.Host(separator);
	}
}
