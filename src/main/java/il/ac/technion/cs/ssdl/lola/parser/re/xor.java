package il.ac.technion.cs.ssdl.lola.parser.re;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;

public class xor extends Composite {
  public xor(final List<RegExp> res) {
    children.addAll(res);
  }
  @Override public void apply(final PythonAdapter a) {
    for (final RegExp re : children)
      if (re.satiated()) {
        re.apply(a);
        return;
      }
  }
  @Override public xor clone() {
    final List<RegExp> childrenCopy = new ArrayList<>();
    for (final RegExp re : children)
      childrenCopy.add(re.clone());
    return new xor(childrenCopy);
  }
  @Override public boolean eats(final Bunny b) {
    for (final RegExp re : children)
      if (re.eats(b))
        return true;
    return false;
  }
  @Override public void feed(final Bunny b) {
    for (final RegExp re : children)
      if (re.eats(b))
        re.feed(b);
      else
        children.remove(re);
  }
  @Override public boolean satiated() {
    return children.stream().filter(re -> re.satiated()).count() == 1;
  }
  @Override public String text() {
    for (final RegExp re : children)
      if (re.satiated())
        return re.text();
    return "DID_NOT_MATCH";
  }
}
