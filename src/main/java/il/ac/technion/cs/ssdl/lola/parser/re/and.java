package il.ac.technion.cs.ssdl.lola.parser.re;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;

public class and extends Composite {
  public and(final List<RegExp> children) {
    this.children.addAll(children);
  }
  @Override public void apply(final PythonAdapter a) {
    for (final RegExp re : children)
      re.apply(a);
  }
  @Override public and clone() {
    final List<RegExp> childrenCopy = new ArrayList<>();
    for (final RegExp re : children)
      childrenCopy.add(re.clone());
    return new and(childrenCopy);
  }
  @Override public boolean eats(final Bunny b) {
    return children.stream().filter(re -> re.eats(b)).count() == children.size();
  }
  @Override public void feed(final Bunny b) {
    for (final RegExp re : children)
      re.feed(b);
  }
  @Override public boolean satiated() {
    return children.stream().filter(re -> re.satiated()).count() == children.size();
  }
  @Override public String text() {
    for (final RegExp re : children)
      if (re.satiated())
        return re.text();
    return "DID_NOT_MATCH";
  }
}
