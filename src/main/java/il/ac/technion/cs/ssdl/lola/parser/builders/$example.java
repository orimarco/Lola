package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.io.*;
import static org.junit.Assert.*;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
public class $example extends Elaborator {
	private static void equalsIgnoringTrivia(final String s1, final String s2) {
		assertEquals(cleanTrivia(s1), cleanTrivia(s2));
	}

	private static String cleanTrivia(final String s1) {
		return s1.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", "");
	}
	// String example = "";
	String expected;

	public $example(final Token token) {
		super(token);
		expectedElaborators = new ArrayList<>(Arrays.asList(new String[]{"$resultsIn"}));
		state = Automaton.List;
	}

	@Override
	public boolean accepts(final AST.Node b) {
		return state == Automaton.List && !(b instanceof SnippetToken)
				&& (!(b instanceof Elaborator) && !(b instanceof $Find) || expectedElaborators.contains(b.name()));
	}

	@Override
	public void adopt(final AST.Node b) {
		if (Automaton.List == state)
			// example += b.token.text;
			if (!(b instanceof $resultsIn))
			list.add(b);
			else {
			elaborators.add((Elaborator) b);
			expected = (($resultsIn) b).getText();
			state = Automaton.Done;
			}
	}

	public void checkExample(final Parser p, final Lexi l) throws IOException {
		equalsIgnoringTrivia(p.parseExample(l, list.stream().map(s -> s.text()).reduce("", (s1, s2) -> s1 + s2)), expected);
	}
};
