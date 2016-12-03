package il.ac.technion.cs.ssdl.lola.parser.lexer;
import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.CategoriesHierarchy.*;
public class Token extends java_cup.runtime.Symbol {
	public static Token newSnippetToken(final Token ¢) {
		final Token $ = new Token(¢.row, ¢.column, ¢.text, CategoriesHierarchy.getCategory("snippet"));
		$.isSnippet = true;
		return $;
	}
	public final int row;
	public int column;
	public String text;
	public final Category category;
	public final String snippet;
	private boolean isSnippet;

	public Token(final int row, final int column, final String text, final Category category) {
		super(0); // just to make it possible to inherit Symbol
		this.row = row;
		this.column = column;
		this.text = text;
		this.category = category;
		snippet = null;
	}

	public Token(final int row, final int column, final String text, final Category category, final String snippet) {
		super(0); // just to make it possible to inherit Symbol
		this.row = row;
		this.column = column;
		this.text = text;
		this.category = category;
		this.snippet = snippet;
	}

	public boolean isHost() {
		return !isTrivia() && !isKeyword() && !isSnippet();
	}

	public boolean isKeyword() {
		return category.isKeyword;
	}

	public boolean isSnippet() {
		return isSnippet;
	}

	public boolean isTrivia() {
		return category.isTrivia;
	}

	@Override
	public String toString() {
		return text;
	}
}
