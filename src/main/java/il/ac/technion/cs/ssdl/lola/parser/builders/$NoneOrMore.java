package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.ArrayList;
import java.util.Arrays;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.Automaton;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.Node;
import il.ac.technion.cs.ssdl.lola.parser.lexer.Token;
import il.ac.technion.cs.ssdl.lola.parser.re.OneOrMore;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.Atomic;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExpable;
import il.ac.technion.cs.ssdl.lola.parser.re.or;
import il.ac.technion.cs.ssdl.lola.parser.re.sequence;
import il.ac.technion.cs.ssdl.lola.utils.iz;
public class $NoneOrMore extends RegExpKeyword implements RegExpable {
	$separator separator;
	$opener opener;
	$closer closer;
	$ifNone ifNone;

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
		final ArrayList<RegExp> $ = new ArrayList<>();
		for (final Node ¢ : list)
			if (!¢.token.isTrivia())
				seqRes.add(((RegExpable) ¢).toRegExp());
		if (opener != null)
			$.add(opener.toRegExp());
		$.addAll(seqRes);
		$.add(seqAsterisk(seqRes, separator));
		if (closer != null)
			$.add(closer.toRegExp());
		return new or(new sequence($), new Atomic.Empty());
	}

	private static RegExp seqAsterisk(final ArrayList<RegExp> ¢, $separator $) {
		@SuppressWarnings("unchecked")
		ArrayList<RegExp> ss = (ArrayList<RegExp>) ¢.clone();
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
		else if (¢ instanceof $ifNone)
			ifNone = ($ifNone) ¢;
	}
};
