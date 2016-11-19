package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.utils.*;
public class $run extends ExecutableElaborator {
	public $run(final Token token) {
		super(token);
		state = Automaton.Snippet;
	}

	@Override
	public boolean accepts(final AST.Node b) {
		return b.token.isTrivia() || b.token.isSnippet() && state != Automaton.Done;
	}

	@Override
	public void adopt(final AST.Node b) {
		if (b.token.isTrivia())
			list.add(b);
		else {
			snippet = (SnippetToken) b;
			state = Automaton.Done;
		}
	}

	@Override
	public void execute(final Chain<Bunny, Lexi>.Interval __,
			final PythonAdapter a, final Parser p) {
		final String code = snippet.getText();
		a.runShellCode(
				code.substring(code.indexOf('{') + 1, code.lastIndexOf('}')));
	}
};
