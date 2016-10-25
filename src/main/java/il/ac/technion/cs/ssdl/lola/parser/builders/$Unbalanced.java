package il.ac.technion.cs.ssdl.lola.parser.builders;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;

public class $Unbalanced extends Keyword implements RegExpable {
  Unbalanceable son;

  public $Unbalanced(final Token token) {
    super(token);
    state = Automaton.List;
  }
  @Override public boolean accepts(final Node b) {
    return b instanceof TriviaToken || b instanceof Unbalanceable;
  }
  @Override public void adopt(final Node b) {
    if (!(b instanceof Unbalanceable))
      return;
    son = (Unbalanceable) b;
    son.setUnbalanced();
    state = Automaton.Done;
  }
  @Override public RegExp toRegExp() {
    return son.toRegExp();
  }
}