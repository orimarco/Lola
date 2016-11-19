package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
public class $resultsIn extends RegExpElaborator {
	public $resultsIn(final Token token) {
		super(token);
	}

	@Override
	public boolean accepts(final AST.Node b) {
		return b instanceof HostToken || b instanceof TriviaToken;
	}

	public String getText() {
		String $ = "";
		for (final AST.Node b : list)
			$ += b.token.text;
		return $;
	}
};
