package il.ac.technion.cs.ssdl.lola.parser.re;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;

public class or extends Composite {
  public or(final List<RegExp> res) {
    children.addAll(res);
  }
  public or(final RegExp... res) {
    for (final RegExp re : res)
      children.add(re);
  }
  @Override public void apply(final PythonAdapter a) {
    // TODO: several children might satiate, we choose the first.
    for (final RegExp re : children)
      if (re.satiated()) {
        re.apply(a);
        return;
      }
  }
  @Override public or clone() {
    final List<RegExp> childrenCopy = new ArrayList<>();
    for (final RegExp re : children)
      childrenCopy.add(re.clone());
    return new or(childrenCopy);
  }
  @Override public boolean eats(final Bunny b) {
    for (final RegExp re : children)
      if (re.eats(b))
        return true;
    return false;
  }
  @Override public void feed(final Bunny b) {
    final List<RegExp> toRemove = new ArrayList<>();
    for (final RegExp re : children)
      if (re.eats(b))
        re.feed(b);
      else
        toRemove.add(re);
    for (final RegExp re : toRemove)
      children.remove(re);
  }
  @Override public boolean satiated() {
    for (final RegExp re : children)
      if (re.satiated())
        return true;
    return false;
  }
  @Override public String text() {
    for (final RegExp re : children)
      if (re.satiated())
        return re.text();
    return "DID_NOT_MATCH";
  }
}
