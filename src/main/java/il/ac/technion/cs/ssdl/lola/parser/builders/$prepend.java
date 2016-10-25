package il.ac.technion.cs.ssdl.lola.parser.builders;

import java.io.*;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.utils.*;

public class $prepend extends ExecutableElaborator {
  public $prepend(final Token token) {
    super(token);
  }
  @Override public void execute(final Chain<Bunny, Lexi>.Interval i, final PythonAdapter a, final Parser p) {
    final ArrayList<Bunny> li = new ArrayList<>();
    for (final Node n : list)
      if (!(n instanceof GeneratingKeyword))
        li.add(new HostBunny(n.token));
      else
        try {
          li.addAll(p.string2bunnies(((GeneratingKeyword) n).generate(a)));
        } catch (final IOException e) {
          e.printStackTrace();
        }
    i.prepend(li);
    // TODO: create recursive algorithm to create bunnies out of nodes
  }
};
