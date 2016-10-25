package il.ac.technion.cs.ssdl.lola.parser.re;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;

public class not extends Composite {
  private final RegExp child;
  private boolean childDeadYet = false;
  private String text = "";

  public not(final RegExp re) {
    child = re;
  }
  @Override public void apply(final PythonAdapter a) {
  }
  @Override public not clone() {
    final not $ = new not(child.clone());
    $.text = text;
    return $;
  }
  @Override public boolean eats(final Bunny b) {
    return true;
  }
  @Override public void feed(final Bunny b) {
    if (!childDeadYet && child.eats(b))
      child.feed(b);
    else
      childDeadYet = true;
    text += b.text();
  }
  @Override public boolean satiated() {
    return childDeadYet || !child.satiated();
  }
  @Override public String text() {
    return text;
  }
}
