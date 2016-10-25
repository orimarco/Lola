package il.ac.technion.cs.ssdl.lola.parser.builders;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;

public class $UserDefinedKeyword extends RegExpKeyword {
  public $UserDefinedKeyword(final Token token, final List<Node> children) {
    super(token);
    list.addAll(children);
    state = Automaton.Snippet;
  }
  @Override public boolean accepts(final AST.Node b) {
    return state == Automaton.Snippet && (b instanceof SnippetToken && isIdentifier((SnippetToken) b) || b instanceof TriviaToken);
  }
  @Override public void adopt(final AST.Node b) {
    if (b instanceof SnippetToken)
      snippet = (SnippetToken) b;
  }
  @Override public RegExp toRegExp() {
    final ArrayList<RegExp> res = new ArrayList<>();
    for (final Node n : list)
      if (!n.token.isTrivia())
        res.add(((RegExpable) n).toRegExp());
    return new sequence(res, snippet == null ? null : snippet.token.text);
  }
  private boolean isIdentifier(final SnippetToken b) {
    return !b.getText().contains(" ") && !b.getText().contains("\t") && !b.getText().contains("\n") && !b.getText().contains("\r");
    // TODO: user defined identifiers...
  }
}
