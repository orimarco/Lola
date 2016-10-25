package il.ac.technion.cs.ssdl.lola.parser.builders;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;

public class $NoneOrMore extends RegExpKeyword implements RegExpable {
  public $NoneOrMore(final Token token) {
    super(token);
    expectedElaborators = new ArrayList<>(Arrays.asList(new String[] { "$separator", "$opener", "$closer", "$ifNone" }));
  }
  @Override public boolean accepts(final AST.Node b) {
    switch (state) {
      case Elaborators:
        return expectedElaborators.contains(b.name());
      case List:
        return !(b instanceof Keyword) || b instanceof RegExpKeyword || expectedElaborators.contains(b.name());
      default:
        return false;
    }
  }
  @Override public void adopt(final AST.Node b) {
    switch (state) {
      case Elaborators:
        adoptElaborator((Builder) b);
        break;
      case List:
        if (!expectedElaborators.contains(b.name()))
          list.add(b);
        else {
          adoptElaborator((Builder) b);
          state = Automaton.Elaborators;
        }
        break;
      default:
        break;
    }
  }
  @Override public RegExp toRegExp() {
    final ArrayList<RegExp> seqRes = new ArrayList<>();
    for (final Node n : list)
      if (!n.token.isTrivia())
        seqRes.add(((RegExpable) n).toRegExp());
    for (final Elaborator e : elaborators)
      seqRes.add(((RegExpable) e).toRegExp());
    for (int i = 0; i < seqRes.size(); i += 2)
      seqRes.add(i, new RegExp.Atomic.TriviaPlaceHolder());
    return new or(Arrays.asList(new RegExp[] { new Atomic.Empty(), new OneOrMore(new sequence(seqRes)) }));
  }
  private void adoptElaborator(final Builder b) {
    elaborators.add((Elaborator) b);
  }
};
