package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
public abstract class Keyword extends Builder {
	protected Automaton state = Automaton.Snippet;
	protected ArrayList<String> expectedElaborators = new ArrayList<>(
			Arrays.asList(new String[]{}));
	public SnippetToken snippet;
	public List<Node> list = new ArrayList<>();
	public List<Elaborator> elaborators = new ArrayList<>();

	public Keyword(final Token token) {
		super(token);
	}

	// default done
	@Override
	public Node done() {
		while (!list.isEmpty()
				&& "enter".equals(list.get(list.size() - 1).categoryName()))
			list.remove(list.size() - 1);
		return this;
	}

	@Override
	public List<Elaborator> elaborators() {
		return elaborators;
	}

	@Override
	public List<Node> list() {
		return list;
	}

	// default mature
	@Override
	public boolean mature() {
		return state == Automaton.Done;
	}

	@Override
	public SnippetToken snippet() {
		return snippet;
	}
}
