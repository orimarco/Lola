package il.ac.technion.cs.ssdl.lola.parser.tokenizer;
import static org.junit.Assert.*;

import java.io.*;
import java.text.*;

import org.junit.*;

import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
public class TokenizerTest {
	Tokenizer tokenizer;

	@Test
	public void test1() throws IOException, ParseException {
		final Reader stream = new StringReader("##If(x==7) 42 ##else 6*7");
		tokenizer = new Tokenizer(stream);
		Token token = tokenizer.next_token();
		assertEquals("##If", token.text);
		assertEquals("##If", token.category.name);
		assertEquals(token.isKeyword(), true);
		assertEquals(token.isTrivia(), false);
		assertEquals(token.isHost(), false);
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals("(x==7)", token.text);
		assertEquals("snippet", token.category.name);
		assertEquals(token.isKeyword(), false);
		assertEquals(token.isTrivia(), false);
		assertEquals(token.isHost(), false);
		assertEquals(token.isSnippet(), true);
		token = tokenizer.next_token();
		assertEquals(" ", token.text);
		assertEquals("ignore", token.category.name);
		assertEquals(token.isKeyword(), false);
		assertEquals(token.isTrivia(), true);
		assertEquals(token.isHost(), false);
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals("42", token.text);
		assertEquals("ldecimal", token.category.name);
		assertEquals(token.isKeyword(), false);
		assertEquals(token.isTrivia(), false);
		assertEquals(token.isHost(), true);
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals(" ", token.text);
		assertEquals("ignore", token.category.name);
		assertEquals(token.isKeyword(), false);
		// assertEquals(bunny.isTrivia(), true);
		// assertEquals(bunny.isHost(), false);
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals("##else", token.text);
		assertEquals("##else", token.category.name);
		assertEquals(token.isKeyword(), true);
		assertEquals(token.isTrivia(), false);
		assertEquals(token.isHost(), false);
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals(" ", token.text);
		assertEquals("ignore", token.category.name);
		assertEquals(token.isKeyword(), false);
		// assertEquals(bunny.isTrivia(), true);
		// assertEquals(bunny.isHost(), false);
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals("6", token.text);
		assertEquals("ldecimal", token.category.name);
		assertEquals(token.isKeyword(), false);
		assertEquals(token.isTrivia(), false);
		assertEquals(token.isHost(), true);
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals("*", token.text);
		assertEquals("star", token.category.name);
		assertEquals(token.isKeyword(), false);
		assertEquals(token.isTrivia(), false);
		assertEquals(token.isHost(), true);
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals("7", token.text);
		assertEquals("ldecimal", token.category.name);
		assertEquals(token.isKeyword(), false);
		assertEquals(token.isTrivia(), false);
		assertEquals(token.isHost(), true);
		assertEquals(token.isSnippet(), false);
		assertEquals(null, tokenizer.next_token());
	}

	@Test
	public void test3() throws IOException, ParseException {
		final Reader stream = new StringReader("(3)");
		tokenizer = new Tokenizer(stream);
		Token token = tokenizer.next_token();
		assertEquals("(", token.text);
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals(token.isSnippet(), false);
	}

	@Test
	public void testEnterN() throws IOException {
		final Reader stream = new StringReader("x\ny");
		tokenizer = new Tokenizer(stream);
		Token token = tokenizer.next_token();
		assertEquals("x", token.text);
		token = tokenizer.next_token();
		assertEquals("\n", token.text);
		token = tokenizer.next_token();
		assertEquals("y", token.text);
	}

	@Test
	public void testEnterR() throws IOException {
		final Reader stream = new StringReader("x\ry");
		tokenizer = new Tokenizer(stream);
		Token token = tokenizer.next_token();
		assertEquals("x", token.text);
		token = tokenizer.next_token();
		assertEquals("\r", token.text);
		token = tokenizer.next_token();
		assertEquals("y", token.text);
	}

	@Test
	public void testEnterRNN() throws IOException {
		final Reader stream = new StringReader("x\r\n\nz");
		tokenizer = new Tokenizer(stream);
		Token token = tokenizer.next_token();
		assertEquals("x", token.text);
		token = tokenizer.next_token();
		assertEquals("enter", token.category.name);
		token = tokenizer.next_token();
		assertEquals("z", token.text);
	}

	@Test
	public void testImportExpression() throws IOException {
		final Reader stream = new StringReader("##Import std.Expression");
		tokenizer = new Tokenizer(stream);
		Token token = tokenizer.next_token();
		assertEquals("##Import", token.text);
		assertEquals("std.Expression", token.snippet);
	}

	@Test
	public void testImportSnippet() throws IOException {
		final Reader stream = new StringReader("##Import java.il.omg");
		tokenizer = new Tokenizer(stream);
		Token token = tokenizer.next_token();
		assertEquals("##Import", token.text);
		assertEquals("java.il.omg", token.snippet);
	}

	@Test
	public void testInclude() throws IOException {
		final Reader stream = new StringReader("##Include \"file.testInclude\"");
		tokenizer = new Tokenizer(stream);
		Token token = tokenizer.next_token();
		assertEquals("##Include", token.text);
		assertEquals("##Include", token.category.name);
		assertEquals(token.isKeyword(), true);
		assertEquals(token.isTrivia(), false);
		assertEquals(token.isHost(), false);
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals(" ", token.text);
		assertEquals("ignore", token.category.name);
		assertEquals(token.isKeyword(), false);
		assertEquals(token.isTrivia(), true);
		assertEquals(token.isHost(), false);
		assertEquals(token.isSnippet(), false);
		token = tokenizer.next_token();
		assertEquals("\"file.testInclude\"", token.text);
		assertEquals("snippet", token.category.name);
		assertEquals(false, token.isKeyword());
		assertEquals(token.isTrivia(), false);
		assertEquals(token.isHost(), false);
		assertEquals(token.isSnippet(), true);
	}
}
