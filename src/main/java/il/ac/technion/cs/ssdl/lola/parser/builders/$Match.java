package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
public class $Match extends RegExpKeyword {
	public $Match(final Token token) {
		super(token);
		expectedElaborators = new ArrayList<>(Arrays.asList(new String[]{"$andAlso", "$exceptFor"}));
	}

	@Override
	public RegExp toRegExp() {
		final ArrayList<RegExp> res = new ArrayList<>();
		for (final Node ¢ : list)
			res.add(((RegExpable) ¢).toRegExp());
		for (final Elaborator ¢ : elaborators)
			res.add(¢ instanceof $andAlso ? ((RegExpable) ¢).toRegExp() : new not(((RegExpable) ¢).toRegExp()));
		return new and(res);
	}
}
