package il.ac.technion.cs.ssdl.lola.parser.re;

import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;

public interface RegExp {
  public void apply(PythonAdapter a);
  public RegExp clone();
  /** can the RE be fed by token */
  public boolean eats(Bunny b);
  /** feed RE with token */
  public void feed(Bunny b);
  /** does the RE match? */
  public boolean satiated();
  public String text();

  public interface Atomic extends RegExp {
    public class Any implements Atomic, Cloneable {
      protected final String snippet;
      protected String text = "";

      public Any(final String snippet) {
        this.snippet = snippet;
      }
      public Any(final String snippet, final boolean unbalanced) {
        this.snippet = snippet;
      }
      @Override public void apply(final PythonAdapter a) {
        if (snippet != null)
          a.addIdentifier(snippet.substring(snippet.indexOf('(') + 1, snippet.lastIndexOf(')')), "'" + text + "'");
        // TODO: might not work if the text doesn't evaluate
      }
      @Override public Any clone() {
        final Any $ = new Any(snippet);
        $.text = text;
        return $;
      }
      @Override public boolean eats(final Bunny b) {
        return true;
      }
      @Override public void feed(final Bunny b) {
        text += b.text();
      }
      @Override public boolean satiated() {
        return true;
      }
      @Override public String text() {
        return text;
      }
    }

    public class BalancedAny extends Any {
      public BalancedAny(final String snippet) {
        super(snippet);
      }
      @Override public BalancedAny clone() {
        final BalancedAny $ = new BalancedAny(snippet);
        $.text = text;
        return $;
      }
      @Override public boolean eats(final Bunny b) {
        return Balancing.hasHope(text + b.text());
      }
      @Override public boolean satiated() {
        return Balancing.isBalanced(text);
      }
    }

    public class Empty implements Atomic, Cloneable {
      public Empty() {
      }
      @Override public void apply(final PythonAdapter a) {
      }
      @Override public Empty clone() {
        return new Empty();
      }
      @Override public boolean eats(final Bunny b) {
        return false;
      }
      @Override public void feed(final Bunny b) {
      }
      @Override public boolean satiated() {
        return true;
      }
      @Override public String text() {
        return "";
      }
    }

    public class Expression implements Atomic, Cloneable {
      private String text = null;
      private final String snippet;

      public Expression(final String snippet) {
        this.snippet = snippet;
      }
      @Override public void apply(final PythonAdapter a) {
        if (snippet != null)
          a.addVariable(snippet, text);
      }
      @Override public Expression clone() {
        final Expression $ = new Expression(snippet);
        $.text = text;
        return $;
      }
      @Override public boolean eats(final Bunny b) {
        throw new RuntimeException("expressions not supported yet");
      }
      @Override public void feed(final Bunny b) {
        throw new RuntimeException("expressions not supported yet");
      }
      @Override public boolean satiated() {
        throw new RuntimeException("expressions not supported yet");
      }
      @Override public String text() {
        return text;
      }
    }

    public class Host implements Atomic, Cloneable {
      private final String text;
      private boolean done = false;

      public Host(final String text) {
        this.text = text;
      }
      @Override public void apply(final PythonAdapter a) {
      }
      @Override public Host clone() {
        final Host $ = new Host(text);
        $.done = done;
        return $;
      }
      @Override public boolean eats(final Bunny b) {
        return !done && b instanceof HostBunny && ((HostBunny) b).token.text.equals(text);
      }
      @Override public void feed(final Bunny b) {
        done = true;
      }
      @Override public boolean satiated() {
        return done;
      }
      @Override public String text() {
        return text;
      }
    }

    public class Identifier implements Atomic, Cloneable {
      private String text = null;
      private final String snippet;

      public Identifier(final String snippet) {
        this.snippet = snippet;
      }
      @Override public void apply(final PythonAdapter a) {
        if (snippet != null)
          a.addIdentifier(snippet.substring(snippet.indexOf('(') + 1, snippet.lastIndexOf(')')), "'" + text + "'");
      }
      @Override public Identifier clone() {
        final Identifier $ = new Identifier(snippet);
        $.text = text;
        return $;
      }
      @Override public boolean eats(final Bunny b) {
        return !(b instanceof TriviaBunny) && text == null && "identifier".equals(((HostBunny) b).token.category.name);
      }
      @Override public void feed(final Bunny b) {
        text = ((HostBunny) b).token.text;
      }
      @Override public boolean satiated() {
        return text != null;
      }
      @Override public String text() {
        return text;
      }
    }

    public class Literal implements Atomic, Cloneable {
      private String text = null;
      private final String snippet;

      public Literal(final String snippet) {
        this.snippet = snippet;
      }
      @Override public void apply(final PythonAdapter a) {
        if (snippet == null)
          return;
        final String name = snippet.substring(snippet.indexOf('(') + 1, snippet.lastIndexOf(')'));
        if (text.startsWith("'"))
          a.addCharVariable(name, text);
        else 
        if (!text.startsWith("\""))
          a.addVariable(name, text);
        else
          a.addStringVariable(name, text);
      }
      @Override public Literal clone() {
        final Literal $ = new Literal(snippet);
        $.text = text;
        return $;
      }
      @Override public boolean eats(final Bunny b) {
        return !(b instanceof TriviaBunny) && text == null
            && CategoriesHierarchy.isClassifiedAs(((HostBunny) b).token.category, CategoriesHierarchy.getCategory("literal"));
      }
      @Override public void feed(final Bunny b) {
        text = ((HostBunny) b).token.text;
      }
      @Override public boolean satiated() {
        return text != null;
      }
      @Override public String text() {
        return text;
      }
    }
    // TODO: Trivia...

    public class Trivia implements Atomic, Cloneable {
      private String text = null;
      private boolean done = false;

      public Trivia(final String text) {
        this.text = text;
      }
      @Override public void apply(final PythonAdapter a) {
      }
      @Override public Trivia clone() {
        final Trivia $ = new Trivia(text);
        $.done = done;
        return $;
      }
      @Override public boolean eats(final Bunny b) {
        return text == null && b instanceof TriviaBunny && ((TriviaBunny) b).text().equals(text);
      }
      @Override public void feed(final Bunny b) {
        done = true;
      }
      @Override public boolean satiated() {
        return done;
      }
      @Override public String text() {
        return text == null ? "" : text;
      }
    }

    public class TriviaPlaceHolder implements Atomic, Cloneable {
      String text = "";

      public TriviaPlaceHolder() {
      }
      @Override public void apply(final PythonAdapter a) {
      }
      @Override public TriviaPlaceHolder clone() {
        final TriviaPlaceHolder $ = new TriviaPlaceHolder();
        $.text = text;
        return $;
      }
      @Override public boolean eats(final Bunny b) {
        return b instanceof TriviaBunny;
      }
      @Override public void feed(final Bunny b) {
        text += b.text();
      }
      @Override public boolean satiated() {
        return true;
      }
      @Override public String text() {
        return text == null ? "" : text;
      }
    }
  }

  abstract class Composite implements RegExp, Cloneable {
    public final ArrayList<RegExp> children = new ArrayList<>();

    public Composite() {
    }
    @Override public abstract Composite clone();
  }
}
