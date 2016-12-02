package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.ArrayList;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.Node;
import il.ac.technion.cs.ssdl.lola.parser.lexer.Token;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExpable;
import il.ac.technion.cs.ssdl.lola.parser.re.not;
import il.ac.technion.cs.ssdl.lola.parser.re.sequence;
import il.ac.technion.cs.ssdl.lola.utils.iz;
public class $exceptFor extends RegExpElaborator {
	public $exceptFor(final Token token) {
		super(token);
	}

	@Override
	public boolean accepts(final AST.Node b) {
		return !iz.keyword(b) || iz.regExpKeyword(b);
	}

	@Override
	public void adopt(final AST.Node b) {
		if (!iz.triviaToken(b))
			list.add(b);
	}

	@Override
	public RegExp toRegExp() {
		final ArrayList<RegExp> res = new ArrayList<>();
		for (final Node ¢ : list)
			if (!¢.token.isTrivia())
				res.add(((RegExpable) ¢).toRegExp());
		return new not(res.size() == 1 ? res.get(0) : new sequence(res));
	}
};
