package il.ac.technion.cs.ssdl.lola.parser.builders;

import il.ac.technion.cs.ssdl.lola.parser.PythonAdapter;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.Automaton;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.Node;
import il.ac.technion.cs.ssdl.lola.parser.lexer.Token;

public class $ extends Keyword implements GeneratingKeyword {
  public $(final Token token) {
    super(token);
  }
  @Override public boolean accepts(final Node b) {
    return snippet == null && b instanceof SnippetToken;
  }
  @Override public void adopt(final Node b) {
    snippet = (SnippetToken) b;
    state = Automaton.Done;
  }
  @Override public String generate(final PythonAdapter a) {
    switch (snippet.type) {
      case Parentheses:
        return a.eavluateStringExpression(snippet.getExpression());
      case CurlyBrackets:
        a.runShellCode(snippet.getExpression());
        return "";
      default:
        return "";
    }
  }
}
