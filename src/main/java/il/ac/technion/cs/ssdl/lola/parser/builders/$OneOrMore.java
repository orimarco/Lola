package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
public class $OneOrMore extends RegExpKeyword implements RegExpable {
	$separator separator;
	$opener opener;
	$closer closer;

	public $OneOrMore(final Token token) {
		super(token);
		expectedElaborators = new ArrayList<>(
				Arrays.asList(new String[]{"$separator", "$opener", "$closer"}));
	}

	@Override
	public boolean accepts(final AST.Node b) {
		switch (state) {
			case Elaborators :
				return b instanceof TriviaToken
						|| expectedElaborators.contains(b.name()) && elaborators.stream()
								.filter(e -> e.getClass().equals(b.getClass())).count() == 0;
			case List :
				return !(b instanceof Keyword) || b instanceof RegExpKeyword
						|| expectedElaborators.contains(b.name());
			default :
				return false;
		}
	}

	@Override
	public void adopt(final AST.Node b) {
		if (!(b instanceof TriviaToken))
			switch (state) {
				case Elaborators :
					adoptElaborator((Builder) b);
					break;
				case List :
					if (!expectedElaborators.contains(b.name()))
						list.add(b);
					else {
						adoptElaborator((Builder) b);
						state = Automaton.Elaborators;
					}
					break;
				default :
					break;
			}
	}

	@Override
	public RegExp toRegExp() {
		final ArrayList<RegExp> oneOrMoreRes = new ArrayList<>();
		for (final Node ¢ : list)
			if (!¢.token.isTrivia()) {
				oneOrMoreRes.add(((RegExpable) ¢).toRegExp());
				if (separator != null)
					oneOrMoreRes.add(separator.toRegExp());
			}
		if (separator != null && oneOrMoreRes.size() > 1)
			oneOrMoreRes.remove(oneOrMoreRes.size() - 1);
		final ArrayList<RegExp> seqRes = new ArrayList<>();
		if (opener != null)
			seqRes.add(opener.toRegExp());
		seqRes.add(new OneOrMore(new sequence(oneOrMoreRes)));
		if (closer != null)
			seqRes.add(opener.toRegExp());
		return new sequence(seqRes);
	}

	private void adoptElaborator(final Builder ¢) {
		elaborators.add((Elaborator) ¢);
		if (¢ instanceof $separator)
			separator = ($separator) ¢;
		else if (¢ instanceof $opener)
			opener = ($opener) ¢;
		else if (¢ instanceof $closer)
			closer = ($closer) ¢;
	}
};
