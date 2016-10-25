package il.ac.technion.cs.ssdl.lola.parser.builders;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.utils.*;

public class $delete extends ExecutableElaborator {
  public $delete(final Token token) {
    super(token);
  }
  @Override public boolean accepts(final AST.Node b) {
    return b instanceof TriviaToken;
  }
  @Override public void adopt(final AST.Node b) {
    list.add(b);
  }
  @Override public void execute(final Chain<Bunny, Lexi>.Interval i, final PythonAdapter a, final Parser p) {
    i.delete();
  }
}
