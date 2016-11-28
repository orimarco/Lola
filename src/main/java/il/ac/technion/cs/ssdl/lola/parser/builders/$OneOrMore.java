package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.Atomic;
import static il.ac.technion.cs.ssdl.lola.utils.wizard.*;
public class $OneOrMore extends RegExpKeyword implements RegExpable {
	$separator separator;
	$opener opener;
	$closer closer;

	public $OneOrMore(final Token token) {
		super(token);
		expectedElaborators = new ArrayList<>(Arrays.asList(new String[]{"$separator", "$opener", "$closer"}));
	}

	@Override
	public boolean accepts(final AST.Node b) {
		switch (state) {
			case Elaborators :
				return b instanceof TriviaToken || expectedElaborators.contains(b.name())
						&& elaborators.stream().filter(e -> e.getClass().equals(b.getClass())).count() == 0;
			case List :
				return !(b instanceof Keyword) || b instanceof RegExpKeyword || expectedElaborators.contains(b.name());
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
		final ArrayList<RegExp> seqRes = new ArrayList<>();
		final ArrayList<RegExp> $ = new ArrayList<>();
		for (final Node ¢ : list)
			if (!¢.token.isTrivia())
				seqRes.add(((RegExpable) ¢).toRegExp());
		if (elaborators.isEmpty())
			return seqPlus(seqRes);
		if (opener != null)
			$.add(opener.toRegExp());
		$.addAll(cloneList(seqRes));
		$.add(seqAsterisk(seqRes, separator));
		if (closer != null)
			$.add(closer.toRegExp());
		return new sequence($);
	}

	private static RegExp seqPlus(List<RegExp> ¢) {
		return new OneOrMore(new sequence(cloneList(¢)));
	}

	private static RegExp seqAsterisk(final List<RegExp> ¢, $separator $) {
		List<RegExp> ss = cloneList(¢);
		if ($ != null)
			ss.add(0, $.toRegExp());
		return new or(new OneOrMore(new sequence(ss)), new Atomic.Empty());
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
