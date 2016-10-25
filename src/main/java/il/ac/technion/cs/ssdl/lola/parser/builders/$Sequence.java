package il.ac.technion.cs.ssdl.lola.parser.builders;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;

public class $Sequence extends RegExpKeyword implements RegExpable {
  public $Sequence(final Token token) {
    super(token);
    expectedElaborators = new ArrayList<>(Arrays.asList(new String[] { "$followedBy" }));
    state = Automaton.Snippet;
  }
  @Override public boolean accepts(final AST.Node b) {
    switch (state) {
      case Elaborators:
        return expectedElaborators.contains(b.name());
      case List:
        return !(b instanceof Keyword) || b instanceof RegExpKeyword || expectedElaborators.contains(b.name());
      case Snippet:
        return b instanceof SnippetToken && isIdentifier((SnippetToken) b) || b instanceof HostToken || b instanceof TriviaToken;
      default:
        return false;
    }
  }
  @Override public void adopt(final AST.Node b) {
    switch (state) {
      case Elaborators:
        adoptElaborator((Builder) b);
        break;
      case List:
        if (!expectedElaborators.contains(b.name()))
          list.add(b);
        else {
          adoptElaborator((Builder) b);
          state = Automaton.Elaborators;
        }
        break;
      case Snippet:
        if (b instanceof HostToken || b instanceof TriviaToken) {
          list.add(b);
          state = Automaton.List;
          break;
        }
        snippet = (SnippetToken) b;
        state = Automaton.List;
        break;
      default:
        break;
    }
  }
  @Override public RegExp toRegExp() {
    final ArrayList<RegExp> res = new ArrayList<>();
    for (final Node n : list)
      if (!n.token.isTrivia())
        res.add(((RegExpable) n).toRegExp());
    for (final Elaborator e : elaborators)
      res.add(((RegExpable) e).toRegExp());
    for (int i = 0; i < res.size(); i += 2)
      res.add(i, new RegExp.Atomic.TriviaPlaceHolder());
    return new sequence(res, snippet == null ? null : snippet.token.text);
  }
  private void adoptElaborator(final Builder b) {
    elaborators.add((Elaborator) b);
  }
  private boolean isIdentifier(final SnippetToken b) {
    return !b.getText().contains(" ") && !b.getText().contains("\t") && !b.getText().contains("\n") && !b.getText().contains("\r");
    // TODO: user defined identifiers...
  }
}
