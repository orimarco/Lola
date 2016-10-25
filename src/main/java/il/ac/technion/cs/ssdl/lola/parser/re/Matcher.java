package il.ac.technion.cs.ssdl.lola.parser.re;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.utils.*;

public class Matcher {
  public RegExp re;
  public final Lexi lexi;
  protected final Chain<Bunny, Lexi>.Location from;
  protected Chain<Bunny, Lexi>.Location to;
  protected Chain<Bunny, Lexi> chain;
  protected String text = "";

  public Matcher(final Lexi lexi, final Chain<Bunny, Lexi> chain, final Chain<Bunny, Lexi>.Location from) {
    this.lexi = lexi;
    re = lexi.toRegExp();
    to = this.from = from;
    this.chain = chain;
  }
  /* revert the last feeding */
  // public abstract void unfeed(AST.Node token);
  /** can the RE be fed by token */
  public boolean eats(final Bunny b) {
    return re.eats(b);
  }
  /* feed RE with token */
  public void feed(final Bunny b) {
    to = to.next();
    re.feed(b);
    text += b.text();
  }
  /* assuming satiated returned true, returns the interval that matched */
  public Chain<Bunny, Lexi>.Interval interval() {
    return chain.new Interval(from, to, lexi);
  }
  /* does the RE match? */
  public boolean satiated() {
    return re.satiated();
  }
  public boolean startsBeforeOrTogether(final Matcher m) {
    return from.lessThanOrEquals(m.from);
  }
  public String text() {
    return text;
  }
}
