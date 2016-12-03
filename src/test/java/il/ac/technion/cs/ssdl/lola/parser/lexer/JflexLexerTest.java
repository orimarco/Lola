package il.ac.technion.cs.ssdl.lola.parser.lexer;
import static org.junit.Assert.*;

import java.io.*;
import java.text.*;

import org.junit.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
public class JflexLexerTest {
	private JflexLexer jfl;

	@Test
	public void test$If() throws IOException, ParseException {
		final Reader stream = new StringReader("##If(x) 42;");
		jfl = new JflexLexer(stream);
		assertNextSymbol("##If", "##If");
		assertNextSymbol("openparen", "(");
		assertNextSymbol("identifier", "x");
		assertNextSymbol("closeparen", ")");
		assertNextSymbol("ignore", " ");
		assertNextSymbol("ldecimal", "42");
		assertNextSymbol("semicolon", ";");
		assertEquals(JflexLexer.YYEOF, jfl.next_token().sym);
	}

	@Test
	public void test$Include() throws IOException, ParseException {
		final Reader stream = new StringReader("##Include \"file.p4\"");
		jfl = new JflexLexer(stream);
		assertNextSymbol("##Include", "##Include");
		assertNextSymbol("ignore", " ");
		assertNextSymbol("lstring", "\"file.p4\"");
		assertEquals(JflexLexer.YYEOF, jfl.next_token().sym);
	}

	@Test
	public void test1() throws IOException, ParseException {
		final Reader stream = new StringReader("abstract = ...");
		jfl = new JflexLexer(stream);
		assertNextSymbol("identifier", "abstract");
		assertNextSymbol("ignore", " ");
		assertNextSymbol("eq", "=");
		assertNextSymbol("ignore", " ");
		assertNextSymbol("tridot", "...");
		assertEquals(JflexLexer.YYEOF, jfl.next_token().sym);
	}

	@Test
	public void testAssignment() throws IOException, ParseException {
		final Reader stream = new StringReader("int x = 7;");
		jfl = new JflexLexer(stream);
		assertNextSymbol("int", "int");
		assertNextSymbol("ignore", " ");
		assertNextSymbol("identifier", "x");
		assertNextSymbol("ignore", " ");
		assertNextSymbol("eq", "=");
		assertNextSymbol("ignore", " ");
		assertNextSymbol("ldecimal", "7");
		assertNextSymbol("semicolon", ";");
		assertEquals(JflexLexer.YYEOF, jfl.next_token().sym);
	}

	@Test
	public void testImportSnippet() throws IOException {
		final Reader stream = new StringReader("##Import java.il.omg");
		jfl = new JflexLexer(stream);
		assertEquals("##Import", ((Token) jfl.next_token()).text);
	}

	@Test
	public void testNumbersClassifiedAsLiterals() throws IOException, ParseException {
		Reader stream = new StringReader("2");
		jfl = new JflexLexer(stream);
		Token token = (Token) jfl.next_token();
		assertEquals(true, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
		stream = new StringReader("77");
		jfl = new JflexLexer(stream);
		token = (Token) jfl.next_token();
		assertEquals(true, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
		stream = new StringReader("0x7");
		jfl = new JflexLexer(stream);
		token = (Token) jfl.next_token();
		assertEquals(true, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
		stream = new StringReader("010b");
		jfl = new JflexLexer(stream);
		token = (Token) jfl.next_token();
		assertEquals(true, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
	}

	@Test
	public void testSignsAndHostNotClassifiedAsLiteral() throws IOException, ParseException {
		Reader stream = new StringReader(";");
		jfl = new JflexLexer(stream);
		Token token = (Token) jfl.next_token();
		assertEquals(false, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
		stream = new StringReader(":");
		jfl = new JflexLexer(stream);
		token = (Token) jfl.next_token();
		assertEquals(false, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
		stream = new StringReader("!");
		jfl = new JflexLexer(stream);
		token = (Token) jfl.next_token();
		assertEquals(false, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
		stream = new StringReader("_");
		jfl = new JflexLexer(stream);
		token = (Token) jfl.next_token();
		assertEquals(false, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
		stream = new StringReader("omg");
		jfl = new JflexLexer(stream);
		token = (Token) jfl.next_token();
		assertEquals(false, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
	}

	@Test
	public void testStringLiteralsClassifiedAsLiteral() throws IOException, ParseException {
		Reader stream = new StringReader("\"omg\"");
		jfl = new JflexLexer(stream);
		Token token = (Token) jfl.next_token();
		assertEquals(true, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
		stream = new StringReader("\"oo\" 2");
		jfl = new JflexLexer(stream);
		token = (Token) jfl.next_token();
		assertEquals(true, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
	}

	@Test
	public void testTabulation() throws IOException, ParseException {
		final Reader stream = new StringReader("	x\n7");
		jfl = new JflexLexer(stream);
		Token token = (Token) jfl.next_token();
		assertEquals(0, token.column);
		token = (Token) jfl.next_token();
		assertEquals(4, token.column);
		token = (Token) jfl.next_token();
		assertEquals(5, token.column);
		token = (Token) jfl.next_token();
		assertEquals(0, token.column);
	}

	@Test
	public void testTriviaNotClassifiedAsLiteral() throws IOException, ParseException {
		Reader stream = new StringReader("\n");
		jfl = new JflexLexer(stream);
		Token token = (Token) jfl.next_token();
		assertEquals(false, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
		stream = new StringReader(" ");
		jfl = new JflexLexer(stream);
		token = (Token) jfl.next_token();
		assertEquals(false, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
		stream = new StringReader("\t");
		jfl = new JflexLexer(stream);
		token = (Token) jfl.next_token();
		assertEquals(false, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
		stream = new StringReader("\r");
		jfl = new JflexLexer(stream);
		token = (Token) jfl.next_token();
		assertEquals(false, CategoriesHierarchy.isClassifiedAs(token.category.name, "literal"));
	}

	@Test
	public void testTriviaseparation() throws IOException, ParseException {
		final Reader stream = new StringReader("\"omg omg\" ");
		jfl = new JflexLexer(stream);
		final Token token = (Token) jfl.next_token();
		assertEquals("lstring", token.category.name);
		assertEquals("\"omg omg\"", token.text);
	}

	@Test
	public void testUserDefinedKeyword() throws IOException {
		final Reader stream = new StringReader("##Omg");
		jfl = new JflexLexer(stream);
		assertEquals("##Omg", ((Token) jfl.next_token()).text);
	}

	private void assertNextSymbol(final String categoryName, final String text) throws IOException {
		final Token token = (Token) jfl.next_token();
		assertEquals(text, token.text);
		assertEquals(categoryName, token.category.name);
	}
}