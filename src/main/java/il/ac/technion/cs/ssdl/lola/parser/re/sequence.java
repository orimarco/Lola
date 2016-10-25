package il.ac.technion.cs.ssdl.lola.parser.re;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;

public class sequence extends Composite {
  public final List<Branch> branches = new ArrayList<>();
  private final String snippet;
  private String text = "";

  public sequence(final List<RegExp> res) {
    this(res, null);
  }
  public sequence(final List<RegExp> res, final String snippet) {
    children.addAll(res);
    this.snippet = snippet;
    branches.add(new Branch(res, 0));
  }
  @Override public void apply(final PythonAdapter a) {
    if (snippet != null)
      a.enterScope(snippet.substring(snippet.indexOf('(') + 1, snippet.lastIndexOf(')')), text());
    for (final Branch br : branches)
      if (br.satiated()) {
        br.apply(a);
        break;
      }
    if (snippet != null)
      a.exitScope();
  }
  @Override public sequence clone() {
    final sequence $ = new sequence(children, snippet);
    $.branches.clear();
    for (final Branch br : branches)
      $.branches.add(br.clone());
    return $;
  }
  @Override public boolean eats(final Bunny b) {
    // System.out.println("seq: eats? [" + b.text() + "]" + (b instanceof
    // TriviaBunny));
    /* check if any branch can accept token */
    for (final Branch br : branches)
      if (br.eats(b))
        return true;
    return b instanceof TriviaBunny;
  }
  @Override public void feed(final Bunny b) {
    List<Branch> newBranches = new ArrayList<>();
    final List<Branch> toRemove = new ArrayList<>();
    for (final Branch br : branches)
      if (!br.eats(b))
        toRemove.add(br);
      else if (!br.diverges(b))
        br.feed(b);
      else {
        newBranches.addAll(br.feedAndGetDivergents(b));
        toRemove.add(br); // already included inside the new branches
      }
    for (final Branch br : toRemove)
      branches.remove(br);
    branches.addAll(newBranches);
    text += b.text();
    // System.out.println("seq: " + branches.size());
  }
  @Override public boolean satiated() {
    for (final Branch b : branches)
      if (b.satiated())
        return true;
    return false;
  }
  @Override public String text() {
    for (final Branch br : branches)
      if (br.satiated())
        return br.text();
    return !branches.isEmpty() ? "Err: " + text : "DID_NOT_MATCH";
  }
}
// TODO: maybe not fully took care of epsilon movements... yes i did not... but
// inside Branch!
