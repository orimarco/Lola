package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
public class $ProperSubsetOf extends RegExpKeyword {
	public $ProperSubsetOf(final Token token) {
		super(token);
		expectedElaborators = new ArrayList<>(Arrays.asList(new String[]{"$and"}));
	}

	@Override
	public RegExp toRegExp() {
		throw new RuntimeException("not implemented yet");
	}
}
