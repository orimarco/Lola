package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.utils.iz;
public class $Match extends RegExpKeyword {
	public $Match(final Token token) {
		super(token);
		expectedElaborators = new ArrayList<>(Arrays.asList(new String[]{"$andAlso", "$exceptFor"}));
	}

	@Override
	public RegExp toRegExp() {
		final ArrayList<RegExp> res = new ArrayList<>();
		for (final Node ¢ : list)
			if (!iz.triviaToken(¢))
				res.add(((RegExpable) ¢).toRegExp());
		for (final Elaborator ¢ : elaborators)
			res.add(((RegExpable) ¢).toRegExp());
		return new and(res);
	}
}
