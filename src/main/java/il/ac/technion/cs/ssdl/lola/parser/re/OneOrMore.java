package il.ac.technion.cs.ssdl.lola.parser.re;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;

public class OneOrMore extends Composite {
  public final List<KlineBranch> branches = new ArrayList<>();
  private final RegExp source;

  public OneOrMore(final RegExp re) {
    source = re.clone();
    branches.add(new KlineBranch(re));
  }
  @Override public void apply(final PythonAdapter a) {
    for (final KlineBranch b : branches)
      if (b.satiated()) {
        b.apply(a);
        return;
      }
  }
  @Override public OneOrMore clone() {
    final OneOrMore $ = new OneOrMore(source);
    $.branches.clear();
    for (final KlineBranch br : branches)
      $.branches.add(br.clone());
    return $;
  }
  @Override public boolean eats(final Bunny b) {
    /* check if any branch can accept token */
    for (final KlineBranch br : branches)
      if (br.eats(b))
        return true;
    return false;
  }
  @Override public void feed(final Bunny b) {
    KlineBranch newBranch = null;
    final List<KlineBranch> toRemove = new ArrayList<>();
    for (final KlineBranch br : branches)
      if (!br.eats(b))
        toRemove.add(br);
      else if (!br.diverges(b))
        br.feed(b);
      else
        newBranch = br.feedAndGetDivergent(b);
    if (newBranch != null)
      branches.add(newBranch);
    for (final KlineBranch br : toRemove)
      branches.remove(br);
  }
  @Override public boolean satiated() {
    for (final KlineBranch b : branches)
      if (b.satiated())
        return true;
    return false;
  }
  @Override public String text() {
    for (final KlineBranch br : branches)
      if (br.satiated())
        return br.text();
    return "DID_NOT_MATCH";
  }
}