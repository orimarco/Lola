package il.ac.technion.cs.ssdl.lola.parser.re;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;

public class Branch implements Cloneable {
  public List<RegExp> res;
  private int idx;

  Branch(final List<RegExp> res, final int idx) {
    this.res = res;
    this.idx = idx;
  }
  public void apply(final PythonAdapter a) {
    for (final RegExp re : res)
      re.apply(a);
  }
  @Override public Branch clone() {
    final List<RegExp> newRes = new ArrayList<>();
    for (final RegExp re : res)
      newRes.add(re.clone());
    return new Branch(newRes, idx);
  }
  public boolean diverges(final Bunny b) {
    int divergents = 0;
    int i = idx;
    while (i < res.size()) {
      if (res.get(i).eats(b))
        ++divergents;
      if (!res.get(i).satiated())
        break;
      ++i;
    }
    return divergents > 1;
  }
  public boolean eats(final Bunny b) {
    int i = idx;
    while (i < res.size()) {
      if (res.get(i).eats(b))
        return true;
      if (!res.get(i).satiated())
        return false;
      ++i;
    }
    return false;
  }
  /** Assuming eats(b) */
  public void feed(final Bunny b) {
    while (idx < res.size()) {
      if (res.get(idx).eats(b)) {
        res.get(idx).feed(b);
        return;
      }
      ++idx;
    }
    res.get(res.get(idx).eats(b) ? idx : ++idx).feed(b);
  }
  public List<Branch> feedAndGetDivergents(final Bunny b) {
    assert eats(b);
    final ArrayList<Branch> $ = new ArrayList<>();
    while (idx < res.size()) {
      if (res.get(idx).eats(b)) {
        final Branch newBranch = clone();
        newBranch.feed(b);
        $.add(newBranch);
      }
      if (!res.get(idx).satiated())
        break;
      ++idx;
    }
    return $;
  }
  public boolean satiated() {
    return idx + 1 == res.size() && res.get(idx).satiated();
  }
  public String text() {
    String $ = "";
    for (final RegExp re : res)
      $ += re.text();
    return $;
  }
}
