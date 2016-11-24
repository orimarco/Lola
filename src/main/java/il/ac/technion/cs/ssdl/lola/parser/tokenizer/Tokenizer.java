package il.ac.technion.cs.ssdl.lola.parser.tokenizer;
import java.io.*;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.SnippetToken;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.utils.Printer;
import java_cup.runtime.*;
import static il.ac.technion.cs.ssdl.lola.utils.wizard.*;
public class Tokenizer implements Iterable<Token> {
	public static String lolaEscapingCharacter = JflexLexer.lolaEscapingCharacter;
	private ArrayList<Token> tokens = new ArrayList<>();
	private int idx;


	public Tokenizer(final Reader stream) {
		try {
			addTokens(stream);
			Printer.printTokens(this);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Iterator<Token> iterator() {
		final TokenizerIterator $ = new TokenizerIterator();
		$.tokenizer = this;
		return $;
	}

	public Token next_token() {
		return idx >= tokens.size() ? null : tokens.get(idx++);
	}

	public void printTokens() {
		for (final Token ¢ : tokens)
			System.out.print("<" + (¢.isTrivia() ? " " : ¢.text + "," + ¢.category.name) + ">");
		System.out.println("");
	}

	public void unget() {
		--idx;
	}

	private Token consumeKeywordAndGetNext(final JflexLexer l, final Token t) throws IOException {
		tokens.add(t);
		if (t.snippet == null && acceptsSnippet(t))
			return consumeSnippetAndGetNext(l);
		if (acceptsSnippet(t))
			tokens.add(newSnippetToken(t));
		return l.next_token_safe();
	}

	private static Token newSnippetToken(final Token ¢) {
		return Token.newSnippetToken(
				new Token(¢.row, ¢.column + ¢.text.length(), ¢.snippet, CategoriesHierarchy.getCategory("package_snippet")));
	}

	private static boolean acceptsSnippet(final Token ¢) {
		return keywordExists(¢)
				&& newKeyword(¢)
						.accepts(new SnippetToken(new Token(0, 0, "(dummy)", CategoriesHierarchy.getCategory("snippet"))))
				|| !keywordExists(¢);
	}

	private Token consumeSnippetAndGetNext(final JflexLexer l) throws IOException {
		Token t = l.next_token_safe();
		if (t == null || !t.isTrivia())
			return t == null
					? null
					: "(".equals(t.text) ? getPraenthesesSnippet(l, t) : "{".equals(t.text) ? getCurlyBracketsSnippet(l, t) : t;
		t = consumeTriviaAndGetNext(l, t);
		return t == null
				? null
				: "{".equals(t.text)
						? getCurlyBracketsSnippet(l, t)
						: "lstring".equals(t.category.name) ? getLstringSnippet(l, t) : t;
	}

	/**
	 * [[SuppressWarningsSpartan]]
	 */
	/*
	 * consumes all following trivia, concatenates into one token and returns the
	 * next token
	 */
	private Token consumeTriviaAndGetNext(final JflexLexer l, final Token t) throws IOException {
		Token $;
		while (($ = l.next_token_safe()) != null) {
			tokens.add(t);
			if (!$.isTrivia())
				return $;
		}
		return null;
	}

	private Token getBracketsSnippet(final JflexLexer l, Token t, final String opener, final String closer)
			throws IOException {
		t = Token.newSnippetToken(t);
		String text = "";
		for (int lp_counter = 1; lp_counter != 0;) {
			// TODO: error when parenthesis not balanced
			text = ((Token) l.next_token()).text;
			t.text += text;
			if (opener.equals(text))
				++lp_counter;
			if (closer.equals(text))
				--lp_counter;
		}
		tokens.add(t);
		return l.next_token_safe();
	}

	private Token getCurlyBracketsSnippet(final JflexLexer l, final Token t) throws IOException {
		return getBracketsSnippet(l, t, "{", "}");
	}

	private Token getLstringSnippet(final JflexLexer l, final Token t) throws IOException {
		tokens.add(Token.newSnippetToken(t));
		return l.next_token_safe();
	}

	private Token getPraenthesesSnippet(final JflexLexer l, final Token t) throws IOException {
		return getBracketsSnippet(l, t, "(", ")");
	}

	public void pushTokens(final Reader stream) throws IOException {
		tokens.addAll(idx, new Tokenizer(stream).tokens);
	}

	/** reads all tokens :) */
	public void addTokens(final Reader stream) throws IOException {
		final JflexLexer jfl = new JflexLexer(stream);
		for (Token token = jfl.next_token_safe(); token != null && notEOF(token);) {
			if (isKeyword(token)) {
				token = consumeKeywordAndGetNext(jfl, token);
				continue;
			}
			if (token.isTrivia()) {
				token = consumeTriviaAndGetNext(jfl, token);
				continue;
			}
			tokens.add(token);
			Symbol tmp; // TODO: change to safe
			if ((tmp = jfl.next_token()).sym == JflexLexer.YYEOF)
				break;
			token = (Token) tmp;
		}
	}

	private static boolean notEOF(Token ¢) {
		return ¢.sym != JflexLexer.YYEOF;
	}

	private static boolean isKeyword(final Token ¢) {
		return ¢.text.startsWith(lolaEscapingCharacter);
	}
	public class TokenizerIterator implements Iterator<Token> {
		Tokenizer tokenizer;

		@Override
		public boolean hasNext() {
			final Token t = tokenizer.next_token();
			tokenizer.unget();
			return t != null;
		}

		@Override
		public Token next() {
			return tokenizer.next_token();
		}
	}
}
