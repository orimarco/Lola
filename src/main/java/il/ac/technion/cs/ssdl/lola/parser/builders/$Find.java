package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
public class $Find extends Keyword implements RegExpable {
	public $Find(final Token token) {
		super(token);
		expectedElaborators = new ArrayList<>(Arrays.asList(new String[]{"$replace",
				"$delete", "$append", "$prepend", "$description", "$note", "$log",
				"$see", "$example", "$anchor", "$run", "$filter", "$Assert"}));
		state = Automaton.Snippet;
	}

	@Override
	public boolean accepts(final AST.Node b) {
		// TODO: Refactor the damm thing
		switch (state) {
			case Snippet :
				if (b instanceof SnippetToken || b instanceof TriviaToken)
					return true;
				return !(b instanceof $Find)
						&& !(b instanceof UnacceptableGeneratingKeyword)
						&& (!(b instanceof Elaborator)
								|| expectedElaborators.contains(b.name()));
			case List :
				return !(b instanceof $Find)
						&& !(b instanceof UnacceptableGeneratingKeyword)
						&& (!(b instanceof Elaborator)
								|| expectedElaborators.contains(b.name()));
			case Elaborators :
				if (b instanceof TriviaToken)
					return true;
				if (!(b instanceof Elaborator))
					return false;
				if (b instanceof $replace || b instanceof $delete)
					return elaborators.stream().filter(e -> e instanceof $replace)
							.count() == 0
							&& elaborators.stream().filter(e -> e instanceof $delete)
									.count() == 0;
				return expectedElaborators.contains(b.name())
						&& b instanceof Elaborator;
			default :
				return false;
		}
	}

	@Override
	public void adopt(final AST.Node b) {
		// TODO: Refactor the damm thing
		switch (state) {
			case Snippet :
				if (b instanceof SnippetToken) {
					snippet = (SnippetToken) b;
					state = Automaton.List;
					return;
				}
				if (expectedElaborators.contains(b.name())) {
					adoptElaborator((Elaborator) b);
					state = Automaton.Elaborators;
				} else {
					list.add(b);
					if (!(b instanceof TriviaToken))
						state = Automaton.List;
				}
				break;
			case List :
				if (expectedElaborators.contains(b.name())) {
					adoptElaborator((Elaborator) b);
					state = Automaton.Elaborators;
				} else {
					list.add(b);
					if (!(b instanceof TriviaToken))
						state = Automaton.List;
				}
				break;
			case Elaborators :
				if (b instanceof TriviaToken)
					return;
				adoptElaborator((Builder) b);
				break;
			default :
				break;
		}
	}

	@Override
	public boolean mature() {
		return state == Automaton.Done && !elaborators.isEmpty();
	}

	@Override
	public RegExp toRegExp() {
		final ArrayList<RegExp> res = new ArrayList<>();
		for (final Node ¢ : list)
			if (!¢.token.isTrivia())
				res.add(((RegExpable) ¢).toRegExp());
		return new sequence(res, null);
	}

	private void adoptElaborator(final Builder ¢) {
		elaborators.add((Elaborator) ¢);
	}
}
