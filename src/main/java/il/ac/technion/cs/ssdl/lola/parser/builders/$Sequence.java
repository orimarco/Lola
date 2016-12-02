package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.utils.iz;
public class $Sequence extends RegExpKeyword implements RegExpable {
	public $Sequence(final Token token) {
		super(token);
		expectedElaborators = new ArrayList<>(Arrays.asList(new String[]{"$followedBy"}));
		state = Automaton.Snippet;
	}

	@Override
	public boolean accepts(final AST.Node b) {
		switch (state) {
			case Snippet :
				if (iz.snippetToken(b) && isIdentifier((SnippetToken) b) || iz.hostToken(b) || iz.triviaToken(b))
					return true;
			case List :
				if (!iz.keyword(b) || iz.regExpKeyword(b) || expectedElaborators.contains(b.name()))
					return true;
			case Elaborators :
				return expectedElaborators.contains(b.name());
			default :
				return false;
		}
	}

	@Override
	public void adopt(final AST.Node b) {
		if (!iz.triviaToken(b))
			switch (state) {
				case Snippet :
					if (iz.snippetToken(b)) {
						snippet = (SnippetToken) b;
						state = Automaton.List;
						break;
					}
					if (iz.hostToken(b)) {
						list.add(b);
						state = Automaton.List;
						break;
					}
				case List :
					state = Automaton.List;
					if (!expectedElaborators.contains(b.name()))
						list.add(b);
					else {
						adoptElaborator((Builder) b);
						state = Automaton.Elaborators;
					}
					break;
				case Elaborators :
					adoptElaborator((Builder) b);
					break;
				default :
					break;
			}
	}

	@Override
	public RegExp toRegExp() {
		final ArrayList<RegExp> res = new ArrayList<>();
		for (final Node ¢ : list)
			if (!¢.token.isTrivia())
				res.add(((RegExpable) ¢).toRegExp());
		for (final Elaborator ¢ : elaborators)
			res.add(((RegExpable) ¢).toRegExp());
		return new sequence(res, snippet == null ? null : snippet.token.text);
	}

	private void adoptElaborator(final Builder ¢) {
		elaborators.add((Elaborator) ¢);
	}

	private static boolean isIdentifier(final SnippetToken b) {
		return !b.getText().contains(" ") && !b.getText().contains("\t") && !b.getText().contains("\n")
				&& !b.getText().contains("\r");
		// TODO: user defined identifiers...
	}
}
