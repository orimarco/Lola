package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
public class $NonEmptySubsetOf extends RegExpKeyword {
	public $NonEmptySubsetOf(final Token token) {
		super(token);
		expectedElaborators = new ArrayList<>(Arrays.asList(new String[]{"$and"}));
	}

	@Override
	public RegExp toRegExp() {
		throw new RuntimeException("not implemented yet");
	}
}
