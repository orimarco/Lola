package il.ac.technion.cs.ssdl.lola.parser.builders;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;

public class $Optional extends RegExpKeyword {
  public $Optional(final Token token) {
    super(token);
    state = Automaton.List;
  }
  @Override public boolean accepts(final Node b) {
    return state == Automaton.List;
  }
  @Override public void adopt(final Node b) {
    if (!(b instanceof TriviaToken))
      list.add(b);
  }
  @Override public RegExp toRegExp() {
    final ArrayList<RegExp> res = new ArrayList<>();
    for (final Node n : list)
      res.add(((RegExpable) n).toRegExp());
    return new or(new sequence(res), new RegExp.Atomic.Empty());
  }
}
