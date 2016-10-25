package il.ac.technion.cs.ssdl.lola.parser.builders;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;

public class $Match extends RegExpKeyword {
  public $Match(final Token token) {
    super(token);
    expectedElaborators = new ArrayList<>(Arrays.asList(new String[] { "$andAlso", "$exceptFor" }));
  }
  @Override public RegExp toRegExp() {
    final ArrayList<RegExp> res = new ArrayList<>();
    for (final Node n : list)
      res.add(((RegExpable) n).toRegExp());
    for (final Elaborator e : elaborators)
      res.add(e instanceof $andAlso ? ((RegExpable) e).toRegExp() : new not(((RegExpable) e).toRegExp()));
    return new and(res);
  }
}
