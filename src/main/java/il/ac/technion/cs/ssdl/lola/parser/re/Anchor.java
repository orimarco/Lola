package il.ac.technion.cs.ssdl.lola.parser.re;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.utils.*;

public class Anchor extends Matcher {
  public Anchor(final Lexi lexi, final Chain<Bunny, Lexi> chain, final Chain<Bunny, Lexi>.Location from) {
    super(lexi, chain, from);
    re = lexi.anchorToRegExp();
  }
  public void explode() {
    throw new RuntimeException("Anchor Error: while matching to [" + text + "]");
  }
}
