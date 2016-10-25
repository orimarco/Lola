package il.ac.technion.cs.ssdl.lola.utils;

public class Pair<X, Y> {
  public final X x;
  public final Y y;

  public Pair(final X x, final Y y) {
    this.x = x;
    this.y = y;
  }
  @Override public boolean equals(final Object o) {
    if (o == this)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    final Pair<?, ?> tuple = (Pair<?, ?>) o;
    return x != null ? !x.equals(tuple.x) : tuple.x == null && (y == null ? tuple.y == null : y.equals(tuple.y));
  }
  @Override public int hashCode() {
    return 31 * (x == null ? 0 : x.hashCode()) + (y == null ? 0 : y.hashCode());
  }
}
