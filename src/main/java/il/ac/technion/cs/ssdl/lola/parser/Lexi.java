package il.ac.technion.cs.ssdl.lola.parser;

import il.ac.technion.cs.ssdl.lola.parser.builders.*;
import il.ac.technion.cs.ssdl.lola.parser.re.*;

public class Lexi implements Bunny {
  public final Keyword keyword;

  Lexi(final Keyword keyword) {
    this.keyword = keyword;
  }
  public RegExp anchorToRegExp() {
    return (($anchor) keyword.elaborators().stream().filter(e -> e instanceof $anchor).findAny().get()).toRegExp();
  }
  public boolean hasAnchor() {
    return keyword.elaborators().stream().filter(e -> e instanceof $anchor).count() != 0;
  }
  public boolean isImmediate() {
    return keyword instanceof GeneratingKeyword;
  }
  @Override public String text() {
    return keyword.name() + (keyword.snippet() == null ? "" : keyword.snippet().text());
  }
  public RegExp toRegExp() {
    return ((RegExpable) keyword).toRegExp();
  }
}
