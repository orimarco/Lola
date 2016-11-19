package il.ac.technion.cs.ssdl.lola.parser.tokenizer;
import java.io.*;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.utils.Printer;
import java_cup.runtime.*;
public class Tokenizer implements Iterable<Token> {
	public static String lolaEscapingCharacter = JflexLexer.lolaEscapingCharacter;
	private ArrayList<Token> tokens = new ArrayList<>();
	private int idx;

	public Tokenizer(final Reader stream) {
		try {
			tokens = getTokens(stream);
			Printer.printTokens(this);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void addTokens(final Reader stream) throws IOException {
		tokens.addAll(idx, getTokens(stream));
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
			System.out.print(
					"<" + (¢.isTrivia() ? " " : ¢.text + "," + ¢.category.name)
							+ ">");
		System.out.println("");
	}

	public void unget() {
		--idx;
	}

	private Token consumeKeywordAndGetNext(final JflexLexer l,
			final ArrayList<Token> ts, final Token t) throws IOException {
		ts.add(t);
		if (t.snippet == null)
			return consumeSnippetAndGetNext(l, ts);
		ts.add(Token.newSnippetToken(new Token(t.row, t.column + t.text.length(),
				t.snippet, CategoriesHierarchy.getCategory("package_snippet"))));
		return l.next_token_safe();
	}

	private Token consumeSnippetAndGetNext(final JflexLexer l,
			final ArrayList<Token> ts) throws IOException {
		Token t = l.next_token_safe();
		if (t == null || !t.isTrivia())
			return t == null ? null : "(".equals(t.text)
					? getPraenthesesSnippet(l, ts, t)
					: "{".equals(t.text) ? getCurlyBracketsSnippet(l, ts, t) : t;
		t = consumeTriviaAndGetNext(l, ts, t);
		return t == null ? null : "{".equals(t.text)
				? getCurlyBracketsSnippet(l, ts, t)
				: "lstring".equals(t.category.name) ? getLstringSnippet(l, ts, t) : t;
	}

	/**
	 * [[SuppressWarningsSpartan]]
	 */
	/*
	 * consumes all following trivia, concatenates into one token and returns the
	 * next token
	 */
	private Token consumeTriviaAndGetNext(final JflexLexer l,
			final ArrayList<Token> tokens, final Token t) throws IOException {
		Token $;
		while (($ = l.next_token_safe()) != null) {
			tokens.add(t);
			if (!$.isTrivia())
				return $;
		}
		return null;
	}

	private Token getBracketsSnippet(final JflexLexer l,
			final ArrayList<Token> ts, Token t, final String opener,
			final String closer) throws IOException {
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
		ts.add(t);
		return l.next_token_safe();
	}

	private Token getCurlyBracketsSnippet(final JflexLexer l,
			final ArrayList<Token> ts, final Token t) throws IOException {
		return getBracketsSnippet(l, ts, t, "{", "}");
	}

	private Token getLstringSnippet(final JflexLexer l, final ArrayList<Token> ts,
			final Token t) throws IOException {
		ts.add(Token.newSnippetToken(t));
		return l.next_token_safe();
	}

	private Token getPraenthesesSnippet(final JflexLexer l,
			final ArrayList<Token> ts, final Token t) throws IOException {
		return getBracketsSnippet(l, ts, t, "(", ")");
	}

	/* reads all tokens :) */
	private ArrayList<Token> getTokens(final Reader stream) throws IOException {
		final ArrayList<Token> $ = new ArrayList<>();
		final JflexLexer jfl = new JflexLexer(stream);
		for (Token token = jfl.next_token_safe(); token != null
				&& token.sym != JflexLexer.YYEOF;) {
			if (isKeyword(token)) {
				token = consumeKeywordAndGetNext(jfl, $, token);
				continue;
			}
			if (token.isTrivia()) {
				token = consumeTriviaAndGetNext(jfl, $, token);
				continue;
			}
			$.add(token);
			Symbol tmp;
			if ((tmp = jfl.next_token()).sym == JflexLexer.YYEOF)
				break;
			token = (Token) tmp;
		}
		return $;
	}

	private boolean isKeyword(final Token ¢) {
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
