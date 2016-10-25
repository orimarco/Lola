package il.ac.technion.cs.ssdl.lola.parser.builders;

import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;

public class $SubsetOf extends RegExpKeyword {
  public $SubsetOf(final Token token) {
    super(token);
  }
  @Override public RegExp toRegExp() {
    throw new RuntimeException("not implemented yet");
  }
}
