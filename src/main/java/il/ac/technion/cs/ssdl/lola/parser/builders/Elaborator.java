package il.ac.technion.cs.ssdl.lola.parser.builders;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;

public abstract class Elaborator extends Keyword {
  public Elaborator(final Token t) {
    super(t);
  }
  @Override public Node done() {
    while (!list.isEmpty() && "enter".equals(list.get(list.size() - 1).categoryName()))
      list.remove(list.size() - 1);
    return this;
  }
}
