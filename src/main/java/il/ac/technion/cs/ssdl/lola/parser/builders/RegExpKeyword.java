package il.ac.technion.cs.ssdl.lola.parser.builders;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;

public abstract class RegExpKeyword extends Keyword implements RegExpable {
  public RegExpKeyword(final Token token) {
    super(token);
    state = Automaton.List; // no snippet for RegExps...
  }
  @Override public boolean accepts(final Node b) {
    if (b instanceof $Find)
      return false;
    switch (state) {
      case Elaborators:
        return expectedElaborators.contains(b.name()) || b.token.isTrivia();
      case List:
        return !(b instanceof Elaborator) || expectedElaborators.contains(b.name());
      default:
        return false;
    }
  }
  @Override public void adopt(final Node b) {
    switch (state) {
      case Elaborators:
        if (b.token.isTrivia())
          ;
        else
          elaborators.add((Elaborator) b);
        break;
      case List:
        if (!b.token.isTrivia())
          list.add(b);
        if (b instanceof Elaborator)
          state = Automaton.Elaborators;
        break;
      default:
        break;
    }
  }
  @Override public boolean mature() {
    return state == Automaton.Done && list.stream().filter(x -> !x.token.isTrivia()).count() > 0;
  }
}
