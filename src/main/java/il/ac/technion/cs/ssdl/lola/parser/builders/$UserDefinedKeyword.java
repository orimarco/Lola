package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.utils.iz;
public class $UserDefinedKeyword extends RegExpKeyword {
	public $UserDefinedKeyword(final Token token, final List<Node> children) {
		super(token);
		list.addAll(children);
		state = Automaton.Snippet;
	}

	@Override
	public boolean accepts(final AST.Node b) {
		return state == Automaton.Snippet && (iz.snippetToken(b) && isIdentifier((SnippetToken) b) || iz.triviaToken(b));
	}

	@Override
	public void adopt(final AST.Node b) {
		if (iz.snippetToken(b))
			snippet = (SnippetToken) b;
	}

	@Override
	public RegExp toRegExp() {
		final ArrayList<RegExp> res = new ArrayList<>();
		for (final Node ¢ : list)
			if (!¢.token.isTrivia())
				res.add(((RegExpable) ¢).toRegExp());
		return new sequence(res, snippet == null ? null : snippet.token.text);
	}

	private static boolean isIdentifier(final SnippetToken b) {
		return !b.getText().contains(" ") && !b.getText().contains("\t") && !b.getText().contains("\n")
				&& !b.getText().contains("\r") && !b.getText().contains("{") && !b.getText().contains("}");
		// TODO Marco: user defined identifiers... or REGEXPJAVA
	}
}
