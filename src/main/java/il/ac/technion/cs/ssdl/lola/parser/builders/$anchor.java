package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
public class $anchor extends Elaborator implements RegExpable {
	public $anchor(final Token token) {
		super(token);
		state = Automaton.List;
	}

	@Override
	public boolean accepts(final AST.Node b) {
		return !(b instanceof Keyword) || b instanceof RegExpKeyword;
	}

	@Override
	public void adopt(final AST.Node b) {
		list.add(b);
	}

	@Override
	public RegExp toRegExp() {
		final ArrayList<RegExp> res = new ArrayList<>();
		for (final Node ¢ : list)
			if (!¢.token.isTrivia())
				res.add(((RegExpable) ¢).toRegExp());
		return new sequence(res, null);
	}
};
