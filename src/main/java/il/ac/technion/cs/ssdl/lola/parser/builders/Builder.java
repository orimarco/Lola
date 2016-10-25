package il.ac.technion.cs.ssdl.lola.parser.builders;

import il.ac.technion.cs.ssdl.lola.parser.lexer.*;

public abstract class Builder extends AST.Node {
  public Builder(final Token token) {
    super(token);
  }
  public abstract boolean accepts(AST.Node b);
  public abstract void adopt(AST.Node b);
  public abstract AST.Node done();
  public abstract boolean mature();
}
