package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;
import il.ac.technion.cs.ssdl.lola.utils.iz;
public class $NoneOrMore extends RegExpKeyword implements RegExpable {
	public $NoneOrMore(final Token token) {
		super(token);
		expectedElaborators = new ArrayList<>(Arrays.asList(new String[]{"$separator", "$opener", "$closer", "$ifNone"}));
	}

	@Override
	public boolean accepts(final AST.Node b) {
		if (iz.triviaToken(b))
			return true;
		switch (state) {
			case Elaborators :
				return expectedElaborators.contains(b.name());
			case List :
				return !(b instanceof Keyword) || b instanceof RegExpKeyword || expectedElaborators.contains(b.name());
			default :
				return false;
		}
	}

	@Override
	public void adopt(final AST.Node b) {
		if (!iz.triviaToken(b))
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
		final ArrayList<RegExp> seqRes = new ArrayList<>();
		for (final Node ¢ : list)
			if (!¢.token.isTrivia())
				seqRes.add(((RegExpable) ¢).toRegExp());
		for (final Elaborator ¢ : elaborators)
			seqRes.add(((RegExpable) ¢).toRegExp());
		return new or(Arrays.asList(new RegExp[]{new Atomic.Empty(), new OneOrMore(new sequence(seqRes))}));
	}

	private void adoptElaborator(final Builder ¢) {
		elaborators.add((Elaborator) ¢);
	}
};
