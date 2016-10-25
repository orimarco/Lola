package il.ac.technion.cs.ssdl.lola.parser;

import il.ac.technion.cs.ssdl.lola.parser.lexer.*;

public class HostBunny implements Bunny {
  public final Token token;

  public HostBunny(final Token token) {
    this.token = token;
  }
  @Override public String text() {
    return token.text;
  }
}
