package il.ac.technion.cs.ssdl.lola.parser.builders;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;

public class $ifNone extends Elaborator {
  public $ifNone(final Token token) {
    super(token);
    state = Automaton.List;
  }
  @Override public boolean accepts(final Node b) {
    return !(b instanceof Elaborator);
  }
  @Override public void adopt(final Node b) {
    list.add(b);
  }
  public String generate(final PythonAdapter a) {
    return list.stream().map(x -> !(x instanceof GeneratingKeyword) ? x.token.text : ((GeneratingKeyword) x).generate(a)).reduce((x, y) -> x + y)
        .get();
  }
};
