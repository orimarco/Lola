package il.ac.technion.cs.ssdl.lola.parser.builders;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;

public class $ForEach extends Keyword implements GeneratingKeyword {
  public $ForEach(final Token token) {
    super(token);
    expectedElaborators = new ArrayList<>(Arrays.asList(new String[] { "$ifNone", "$separator" }));
  }
  @Override public boolean accepts(final Node b) {
    // if ("\n".equals(b.token.text) || "\r".equals(b.token.text))
    // return false;
    switch (state) {
      case Elaborators:
        return expectedElaborators.contains(b.name());
      case List:
        return !(b instanceof Keyword) || expectedElaborators.contains(b.name()) || b instanceof GeneratingKeyword;
      case Snippet:
        return true;
      default:
        return false;
    }
  }
  @Override public void adopt(final Node b) {
    switch (state) {
      case Elaborators:
        elaborators.add((Elaborator) b);
        expectedElaborators.remove(b.name()); // can't consume same
        // elaborator again
        break;
      case List:
        if (!expectedElaborators.contains(b.name()))
          list.add(b);
        else {
          elaborators.add((Elaborator) b);
          state = Automaton.Elaborators;
        }
        break;
      case Snippet:
        snippet = (SnippetToken) b;
        state = Automaton.List;
        break;
      default:
        break;
    }
  }
  @Override public Node done() {
    if ("\n".equals(list.get(list.size() - 1).text()) || "\r".equals(list.get(list.size() - 1).text())
        || "\r\n".equals(list.get(list.size() - 1).text()))
      list.remove(list.size() - 1);
    return this;
  }
  @Override public String generate(final PythonAdapter a) {
    final int iterations = a
        .forEachBeforeAndGetIterationsNum(snippet.getText().substring(snippet.getText().indexOf('(') + 1, snippet.getText().lastIndexOf(')')));
    // System.out.println(iterations);
    if (iterations == 0) {
      a.forEachAfter();
      return (($ifNone) elaborators.stream().filter(x -> x instanceof $ifNone).findFirst().get()).generate(a);
    }
    final Optional<Elaborator> sep = elaborators.stream().filter(x -> x instanceof $separator).findFirst();
    final String separator = !sep.isPresent() ? "" : (($separator) sep.get()).separator;
    String $ = "";
    for (int i = 0; i < iterations; ++i) {
      a.forEachBeforeIteration(i);
      if (i > 0)
        $ += separator;
      $ += list.stream().map(x -> !(x instanceof GeneratingKeyword) ? x.token.text : ((GeneratingKeyword) x).generate(a)).reduce((x, y) -> x + y)
          .get();
    }
    a.forEachAfter();
    return $;
  }
  @Override public boolean mature() {
    return elaborators.size() == 2 || state == Automaton.Done;
  }
}
