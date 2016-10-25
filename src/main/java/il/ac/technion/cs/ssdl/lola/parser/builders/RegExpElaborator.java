package il.ac.technion.cs.ssdl.lola.parser.builders;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;

public abstract class RegExpElaborator extends Elaborator implements RegExpable {
  RegExpable son = null;

  public RegExpElaborator(final Token t) {
    super(t);
    state = Automaton.List;
  }
  @Override public boolean accepts(final AST.Node b) {
    return b.token.isTrivia() || !(b instanceof Elaborator) && state != Automaton.Done && son == null && !(b instanceof $Find);
  }
  @Override public void adopt(final AST.Node b) {
    list.add(b);
    if (!b.token.isTrivia())
      son = (RegExpable) b;
  }
  @Override public boolean mature() {
    return state == Automaton.Done && son != null;
  }
  @Override public RegExp toRegExp() {
    return son.toRegExp();
  }
}
