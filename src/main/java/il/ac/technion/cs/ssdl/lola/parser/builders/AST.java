package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
public interface AST {
	enum Automaton {
		Snippet, List, Elaborators, Done, Error
	}
	abstract class Node {
		protected final Token token;

		public Node(final Token token) {
			this.token = token;
		}

		public String categoryName() {
			return token.category.name;
		}

		public int column() {
			return token.column;
		}

		public List<Elaborator> elaborators() {
			return null;
		}

		public List<Node> list() {
			return null;
		}

		public String name() {
			return getClass().getSimpleName();
		}

		public SnippetToken snippet() {
			return null;
		}

		public String text() {
			return token.text;
		}
	}
}
