package il.ac.technion.cs.ssdl.lola.parser.builders;

import il.ac.technion.cs.ssdl.lola.parser.re.*;

public interface Unbalanceable extends RegExpable {
  void setUnbalanced();
}
