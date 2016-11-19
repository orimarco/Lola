package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
public class $If extends Keyword implements GeneratingKeyword {
	public $If(final Token token) {
		super(token);
		expectedElaborators = new ArrayList<>(
				Arrays.asList(new String[]{"$else", "$elseIf"}));
	}

	/**
	 * $If follows the pattern: #If(<expression>) B1 #elseIf(<expression>) B2
	 * #elseIf(<expression>) B3 ... #else Bn
	 */
	@Override
	public boolean accepts(final AST.Node b) {
		switch (state) {
			case Elaborators :
				return expectedElaborators.contains(b.name())
						&& b instanceof Elaborator;
			case List :
				return !(b instanceof Elaborator)
						|| expectedElaborators.contains(b.name());
			case Snippet :
				return true;
			default :
				return false;
		}
	}

	@Override
	public void adopt(final AST.Node b) {
		switch (state) {
			case Elaborators :
				adoptElaborator((Builder) b);
				break;
			case List :
				if (!expectedElaborators.contains(b.name()))
					list.add(b);
				else {
					adoptElaborator((Elaborator) b);
					state = Automaton.Elaborators;
				}
				break;
			case Snippet :
				snippet = (SnippetToken) b;
				state = Automaton.List;
				break;
			default :
				break;
		}
	}

	/**
	 * We find the first expression that evaluates to true and execute it. If we
	 * get to an else we execute it.
	 */
	@Override
	public String generate(final PythonAdapter a) {
		List<AST.Node> toExecute = new ArrayList<>();
		if (a.evaluateBooleanExpression(snippet.getText()))
			toExecute = list;
		else
			for (final Elaborator ¢ : elaborators)
				if (¢ instanceof $else)
					toExecute = ¢.list;
				else if (a.evaluateBooleanExpression(¢.snippet.getText())) {
					toExecute = ¢.list;
					break;
				}
		return toExecute.stream().map(n -> n.token.text).reduce("",
				(s1, s2) -> s1 + s2);
	}

	private void adoptElaborator(final Builder ¢) {
		elaborators.add((Elaborator) ¢);
		if (¢ instanceof $else)
			state = Automaton.Done;
	}
};
