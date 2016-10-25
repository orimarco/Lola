package il.ac.technion.cs.ssdl.lola.parser;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;
import org.junit.*;
import org.python.util.*;

import il.ac.technion.cs.ssdl.lola.parser.builders.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.tokenizer.*;

/**
 * Unit tests for {@link Parser}
 * 
 * @author Ori Marcovitch
 * @since 2016
 */
public class ParserTest {
	static Parser parser;

	// @BeforeClass
	// public static void stall() {
	// try {
	// System.in.read();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	@Test
	public void pyTest() {
		final PythonInterpreter pi = new PythonInterpreter();
		pi.exec("x = [1,2,3]");
		pi.exec("class dummy(object):\n\tpass");
		pi.exec("");
	}

	@Test
	public void test$example() throws IOException {
		final Reader stream = new StringReader("##Find ##Literal(lit) ##replace 7 ##example \"omg\" ##resultsIn 7");
		parser = new Parser(stream);
		parser.parse();
	}

	@Test
	public void test$Literal() throws IOException {
		final Reader stream = new StringReader("##Find ##Literal(lit) ##replace 7\n \"omg\"");
		parser = new Parser(stream);
		aux.assertTEquals("7", aux.list2string(parser.parse()));
	}

	@Test
	public void test$OneOrMore() throws IOException {
		final Reader stream = new StringReader(
				"##Find ##OneOrMore ##Literal(lit)\n\t; ##replace ##ForEach(lits) ##(_)7\n \"omg\" \"ooo\";");
		parser = new Parser(stream);
		aux.assertTEquals("\"omg\"7 \"ooo\"7", aux.list2string(parser.parse()));
	}

	@Test
	public void testAnchor() {
		try {
			aux.runStringTest("##Find #anchored b #to\n\t##anchor #anchored\n#anchored 77");
			fail("RuntimeError wasn't thrown!");
		} catch (final RuntimeException e) {
			assertEquals("Anchor Error: while matching to [#anchored ]", e.getMessage());
		}
	}

	@Test
	public void testAnchoredVisitor() {
		try {
			aux.runStringTest("##Import nPatterns.rangeBasedCase\n #Case x #of 1-7 #then 8 #then",
					"#Case x #of 1-7 # then 8 #then");
			fail("RuntimeError wasn't thrown!");
		} catch (final RuntimeException e) {
			aux.assertTEquals("Anchor Error: while matching to [#Case x #of 1-7 # then 8 #then]", e.getMessage());
		}
	}

	@Test
	public void testAnyMatchesOnlyBalanced() throws IOException {
		final Reader stream = new StringReader(
				"##Find 1 ##Any 2 ##replace replaced\n##example 1(this)2 1((not this)2 1 this 2\n##resultsIn replaced 1((not this)2 replaced");
		parser = new Parser(stream);
		parser.parse();
	}

	@Test
	public void testBalancing() {
		assertEquals(Balancing.isBalanced("("), false);
		assertEquals(Balancing.isBalanced(")"), false);
		assertEquals(Balancing.isBalanced(")("), false);
		assertEquals(Balancing.isBalanced("()"), true);
		assertEquals(Balancing.isBalanced("()()"), true);
		assertEquals(Balancing.isBalanced("(()()((())))"), true);
		assertEquals(Balancing.isBalanced("(()())(((())))"), true);
		assertEquals(Balancing.isBalanced("(()())(((()))()"), false);
		assertEquals(Balancing.hasHope("(()())(((()))()"), true);
		assertEquals(Balancing.isBalanced("([)(])"), false);
		assertEquals(Balancing.isBalanced("([]{})"), true);
		assertEquals(Balancing.isBalanced("([]{{[()]}})"), true);
		assertEquals(Balancing.isBalanced("([{]{{[()]}})"), false);
		assertEquals(Balancing.hasHope("([{]{{[()]}})"), false);
		assertEquals(Balancing.isBalanced("(omg)"), true);
		assertEquals(Balancing.isBalanced("(this)[is]{so}balanced"), true);
		assertEquals(Balancing.isBalanced("(this)[is]{no}t)"), false);
		assertEquals(Balancing.hasHope("((this)[might]{still}have[hope]"), true);
	}

	@Test
	public void testBugsHuge() throws IOException {
		final Reader stream = new StringReader("##Find sizeof ##Sequence Bugs\n"
				+ "               ?huge ##replace ##If(random() >= 0.5) 42"
				+ "										  ##else 6 *" + "											7");
		parser = new Parser(stream);
		final List<Lexi> directives = parser.directives();
		// printAST(directives.get(0).keyword);
		assertEquals(directives.size(), 1);
		final Node dir = directives.get(0).keyword;
		assertEquals(dir.snippet(), null);
		assertEquals(dir.list().size(), 4);
		assertEquals(((TriviaToken) dir.list().get(0)).getText(), " ");
		assertEquals(((HostToken) dir.list().get(1)).getText(), "sizeof");
		assertEquals(((TriviaToken) dir.list().get(0)).getText(), " ");
	}

	@Test
	public void testCaseOfRangeThen1() {
		aux.runStringTest("##Find(Range) ##Literal(begin)-##Literal(end)\n"
				+ "##Find  #Case ##Identifier(c) ##Sequence(option) #of ##Range(r) #then ##Literal(exp) #otherwise ##Literal(oexp) #done; ##replace ##(option.r.begin)*##(option.r.end)\n"
				+ "##example int kind= #Case c #of 'a'-'z' #then 1 #otherwise 2 #done; ##resultsIn int kind= 'a'*'z'");
		aux.runStringTest("##Find(Range) ##Literal(begin)-##Literal(end)\n"
				+ "##Find  #Case ##Identifier(c) ##Sequence(option) #of ##Range(r) #then ##Literal(exp) #otherwise ##Literal(oexp) #done; ##replace ##(option.r.begin)*##(option.r.end)\n"
				+ "##example int kind= #Case c #of '0'-'9' #then 1 #otherwise 2 #done; ##resultsIn int kind= '0'*'9'");
	}

	@Test
	public void testCharIsLiteral() {
		aux.runStringTest("##Find ##Literal(lit) ##replace replaced\n##example 'a' 'b'\n##resultsIn replaced replaced");
	}

	@Test
	public void testFindAppend() throws IOException {
		final Reader stream = new StringReader("##Find 1 ##append 3\n" + "2 1 1");
		parser = new Parser(stream);
		assertEquals("2 1 3 1 3", aux.list2string(parser.parse()));
	}

	@Test
	public void testFindDelete() throws IOException {
		final Reader stream = new StringReader("##Find 1\n##delete \n 1 2 1");
		parser = new Parser(stream);
		assertEquals(" 2 ", aux.list2string(parser.parse()));
	}

	@Test
	public void testFindPrepend() throws IOException {
		final Reader stream = new StringReader("##Find 1 ##prepend 3\n" + "2 1 1");
		parser = new Parser(stream);
		aux.assertTEquals("2 31 31", aux.list2string(parser.parse()));
	}

	@Test
	public void testFindReplaceParsing() throws IOException {
		final Reader stream = new StringReader("##Find 2 ##replace 3\n" + "2 2 2");
		parser = new Parser(stream);
		final List<Lexi> directives = parser.directives();
		assertEquals(directives.size(), 1);
		assertEquals(directives.get(0).keyword.elaborators().get(0).getClass(), $replace.class);
		assertEquals(directives.get(0).keyword.elaborators().get(0).list().size(), 2);
	}

	@Test
	public void testFindReplaceReplacement() throws IOException {
		final Reader stream = new StringReader("##Find 2 ##replace 3\n" + "2 2 2");
		parser = new Parser(stream);
		assertEquals(" 3  3  3", aux.list2string(parser.parse()));
	}

	@Test
	public void testFindReplaceReplacementTwoFinds() throws IOException {
		final Reader stream = new StringReader("##Find 1 ##replace 3\n" + "##Find 2 ##replace 4\n" + "2 1 2");
		parser = new Parser(stream);
		aux.assertTEquals("4 3 4", aux.list2string(parser.parse()));
	}

	@Ignore
	@Test
	public void testFluentList() throws IOException {
		try (final Reader stream = new FileReader(
				new File("./src/il/ac/technion/cs/ssdl/lola/parser/tests/fluentList.lola"))) {
			parser = new Parser(stream);
			aux.assertTEquals("", aux.list2string(parser.parse()));
		}
	}

	@Test
	public void testForEach() throws IOException {
		try (final Reader stream = new StringReader("##ForEach([x for x in xrange(1,42)]) 42")) {
			parser = new Parser(stream);
			final List<Lexi> directives = parser.directives();
			assertEquals(directives.size(), 1);
			final Node dir = directives.get(0).keyword;
			assertEquals(dir.snippet().getText(), "([x for x in xrange(1,42)])");
			assertEquals(dir.elaborators().size(), 0);
			assertEquals(dir.list().size(), 2);
			assertEquals(((TriviaToken) dir.list().get(0)).getText(), " ");
			assertEquals(((HostToken) dir.list().get(1)).getText(), "42");
		}
	}

	@Test
	public void testForEachGenration() throws IOException {
		final Reader stream = new StringReader("##Find 4 ##replace ##ForEach([1,2,3]) ##(_) \n4");
		parser = new Parser(stream);
		assertEquals("  1   2   3 ", aux.list2string(parser.parse()));
	}

	@Test
	public void testForEachGenrationWithIfNone() throws IOException {
		final Reader stream = new StringReader("##Find 4 ##replace ##ForEach([]) ##(_) ##ifNone 8\n4");
		parser = new Parser(stream);
		aux.assertTEquals("  8", aux.list2string(parser.parse()));
	}

	@Test
	public void testForEachGenrationWithSeparator() throws IOException {
		final Reader stream = new StringReader("##Find 4 ##replace ##ForEach([1,2,3]) ##(_) ##separator ,\n4");
		parser = new Parser(stream);
		assertEquals("  1 , 2 , 3", aux.list2string(parser.parse()));
	}

	@Test
	public void testForEachIfNone() throws IOException {
		final Reader stream = new StringReader("##ForEach([x for x in xrange(1,42)]) 42 ##ifNone 24");
		parser = new Parser(stream);
		final List<Lexi> directives = parser.directives();
		assertEquals(directives.size(), 1);
		final Node dir = directives.get(0).keyword;
		assertEquals(dir.snippet().getText(), "([x for x in xrange(1,42)])");
		assertEquals(dir.list().size(), 3);
		assertEquals(((TriviaToken) dir.list().get(0)).getText(), " ");
		assertEquals(((HostToken) dir.list().get(1)).getText(), "42");
		assertEquals(((TriviaToken) dir.list().get(0)).getText(), " ");
		// check elaborator
		assertEquals(1, dir.elaborators().size());
		final Elaborator e = dir.elaborators().get(0);
		assertEquals(e.name(), "$ifNone");
		assertEquals(e.list().size(), 2);
		assertEquals(((TriviaToken) e.list().get(0)).getText(), " ");
		assertEquals(((HostToken) e.list().get(1)).getText(), "24");
	}

	@Test
	public void testForEachIfNoneSeparator() throws IOException {
		final Reader stream = new StringReader("##ForEach([x for x in xrange(1,42)]) 42 ##ifNone 24 ##separator ,");
		parser = new Parser(stream);
		final List<Lexi> directives = parser.directives();
		assertEquals(directives.size(), 1);
		final Node dir = directives.get(0).keyword;
		assertEquals(dir.snippet().getText(), "([x for x in xrange(1,42)])");
		assertEquals(dir.list().size(), 3);
		assertEquals(((TriviaToken) dir.list().get(0)).getText(), " ");
		assertEquals(((HostToken) dir.list().get(1)).getText(), "42");
		assertEquals(((TriviaToken) dir.list().get(0)).getText(), " ");
		// check elaborator
		assertEquals(2, dir.elaborators().size());
		Elaborator e = dir.elaborators().get(0);
		assertEquals(e.name(), "$ifNone");
		assertEquals(3, e.list().size());
		assertEquals(((TriviaToken) e.list().get(0)).getText(), " ");
		assertEquals(((HostToken) e.list().get(1)).getText(), "24");
		assertEquals(((TriviaToken) e.list().get(2)).getText(), " ");
		e = dir.elaborators().get(1);
		assertEquals(e.name(), "$separator");
		assertEquals(e.list().size(), 2);
		assertEquals(((TriviaToken) e.list().get(0)).getText(), " ");
		assertEquals(((HostToken) e.list().get(1)).getText(), ",");
	}

	@Test
	public void testIdentifierReplaceWith$() throws IOException {
		final Reader stream = new StringReader("##Find ##Identifier(id) ##replace ##(id) ##example x ##resultsIn x");
		parser = new Parser(stream);
		parser.parse();
	}

	@Test
	public void testIdentifierReplaceWith2$() throws IOException {
		final Reader stream = new StringReader(
				"##Find ##Identifier(id) ##replace ##(id) ##(id) ##example x ##resultsIn x x");
		parser = new Parser(stream);
		parser.parse();
	}

	@Test
	public void testIfElse() throws IOException {
		final Reader stream = new StringReader("##If(x==7) 42 ##else 6*7");
		parser = new Parser(stream);
		assertEquals(parser.directives().size(), 1);
	}

	@Test
	public void testIfElseExecution1() throws IOException {
		final Reader stream = new StringReader("##If(3 + 3 == 6) 6 ##else 7\n4 2");
		parser = new Parser(stream);
		aux.assertTEquals(" 6 4 2", aux.list2string(parser.parse()));
	}

	@Test
	public void testIfElseExecution2() throws IOException {
		final Reader stream = new StringReader("##If(3 + 2 == 6) 6 ##else 7\n4 2");
		parser = new Parser(stream);
		aux.assertTEquals(" 7\n4 2", aux.list2string(parser.parse()));
	}

	@Test
	public void testIfElseExecution3() throws IOException {
		final Reader stream = new StringReader("##If(3 + 2 == 6) 6 ##elseIf(7 == 9) 7 ##else 2\n4 2");
		parser = new Parser(stream);
		aux.assertTEquals(" 2\n4 2", aux.list2string(parser.parse()));
	}

	@Test
	public void testIfElseIfElse() throws IOException {
		final Reader stream = new StringReader("##If(x==7) 42 ##elseIf(x==4) 6*7 ##else 3*14");
		parser = new Parser(stream);
		final List<Lexi> directives = parser.directives();
		assertEquals(directives.size(), 1);
		final Node dir = directives.get(0).keyword;
		assertEquals(dir.snippet().getText(), "(x==7)");
		assertEquals(dir.elaborators().size(), 2);
		Elaborator e = dir.elaborators().get(0);
		assertEquals(e.name(), "$elseIf");
		assertEquals(e.elaborators().size(), 0);
		assertEquals(5, e.list().size());
		assertEquals(((TriviaToken) e.list().get(0)).getText(), " ");
		assertEquals(((HostToken) e.list().get(1)).getText(), "6");
		assertEquals(((HostToken) e.list().get(2)).getText(), "*");
		assertEquals(((HostToken) e.list().get(3)).getText(), "7");
		assertEquals(((TriviaToken) e.list().get(4)).getText(), " ");
		e = dir.elaborators().get(1);
		assertEquals(e.name(), "$else");
		assertEquals(e.list().size(), 4);
		assertEquals(((TriviaToken) e.list().get(0)).getText(), " ");
		assertEquals(((HostToken) e.list().get(1)).getText(), "3");
		assertEquals(((HostToken) e.list().get(2)).getText(), "*");
		assertEquals(((HostToken) e.list().get(3)).getText(), "14");
	}

	@Test
	public void testIncludeExecution() throws IOException {
		final String fileName = "file.testInclude";
		final PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		writer.println("host1 host2");
		writer.close();
		final Reader stream = new StringReader("##Include \"file.testInclude\"");
		parser = new Parser(stream);
		final List<Lexi> directives = parser.directives();
		assertEquals(directives.size(), 1);
		final GeneratingKeyword include = (GeneratingKeyword) directives.get(0).keyword;
		final Reader r = new StringReader(include.generate(null));
		final Tokenizer tokenizer = new Tokenizer(r);
		Token token = tokenizer.next_token();
		assertEquals("host1", token.text);
		token = tokenizer.next_token();
		assertEquals(" ", token.text);
		token = tokenizer.next_token();
		assertEquals("host2", token.text);
		new File(fileName).delete();
	}

	@Test
	public void testIncludeIncludexecution() throws IOException {
		final String fileName1 = "file.testInclude";
		final String fileName2 = "file2.testInclude";
		PrintWriter writer = new PrintWriter(fileName1, "UTF-8");
		writer.print("##Include \"file2.testInclude\"");
		writer.close();
		writer = new PrintWriter(fileName2, "UTF-8");
		writer.println("host1 host2");
		writer.close();
		final Reader stream = new StringReader("##Include \"file.testInclude\"");
		parser = new Parser(stream);
		final List<Lexi> directives = parser.directives();
		assertEquals(directives.size(), 1);
		final GeneratingKeyword include = (GeneratingKeyword) directives.get(0).keyword;
		final Reader r = new StringReader(include.generate(null));
		final Tokenizer tokenizer = new Tokenizer(r);
		Token token = tokenizer.next_token();
		assertEquals("##Include", token.text);
		token = tokenizer.next_token();
		assertEquals(" ", token.text);
		token = tokenizer.next_token();
		assertEquals("\"file2.testInclude\"", token.text);
		new File(fileName1).delete();
		new File(fileName2).delete();
	}

	@Test
	public void testIncludeParsing() throws IOException {
		final Reader stream = new StringReader("##Include \"file.testInclude\"");
		parser = new Parser(stream);
		final List<Lexi> directives = parser.directives();
		assertEquals(directives.size(), 1);
		assertEquals(directives.get(0).keyword.snippet().getText(), "\"file.testInclude\"");
	}

	@Ignore
	@Test
	public void testLibs() {
		for (final File f : FileUtils.listFiles(new File("./lola_libs"), new RegexFileFilter(".*\\.lola"),
				DirectoryFileFilter.DIRECTORY))
			aux.runFileTest(f);
	}

	@Test
	public void testLiteralReplaceWith$() throws IOException {
		final Reader stream = new StringReader("##Find ##Literal(lit) ##replace ##(lit) ##example 7 ##resultsIn 7");
		parser = new Parser(stream);
		parser.parse();
	}

	@Test
	public void testLiteralReplaceWith2$() throws IOException {
		final Reader stream = new StringReader(
				"##Find ##Literal(lit) ##replace ##(lit) ##(lit) \n##example 7 ##resultsIn 7 7");
		parser = new Parser(stream);
		parser.parse();
	}

	@Test
	public void testNullConditionalMemberAccess() throws IOException {
		aux.runLibTest("operators/optionalSign");
	}

	@Test
	public void testOneOrMoreRanges() {
		aux.runStringTest("##Find(Range) ##Literal(begin)-##Literal(end)\n"
				+ "##Find ##OneOrMore ##Sequence(option) ##Range(r) ##replace ##ForEach(options) ##(_.r.begin)"
				+ "##example 'a'-'b' 'c'-'d' ##resultsIn 'a' 'c'");
	}

	@Test
	public void testOneOrMoreRangesOpenerCloser() {
		aux.runStringTest("##Find(Range) ##Literal(begin)-##Literal(end)\n"
				+ "##Find ##OneOrMore ##Range(r) ##opener d ##closer d ##replace ##ForEach(rs) ##(_.begin)"
				+ "\n##example d 'a'-'b'  'c'-'d' d ##resultsIn 'a' 'c'");
	}

	@Test
	public void testRange() {
		aux.runStringTest(
				"##Find(Range) ##Literal(begin)-##Literal(end) ##replace ##(begin)*##(end)\n##example 'a'-'b'\n##resultsIn 'a'*'b'");
		aux.runStringTest(
				"##Find(Range) ##Literal(begin)-##Literal(end) ##replace ##(begin)*##(end)\n##example 1-7\n##resultsIn 1*7");
	}

	@Test
	public void testRun() throws IOException {
		final Reader stream = new StringReader(
				"##Find 1 ##run {if 'x' not in locals():\n\t x = 0\nelse:\n\tx +=1}\n1 1 1");
		parser = new Parser(stream);
		aux.assertListEquals(parser.parse(), new ArrayList<>(Arrays.asList(new String[] { "1", " ", "1", " ", "1" })));
	}

	@Test
	public void testSequence() throws IOException {
		final Reader stream = new StringReader("##Sequence(identifier) 1 ##followedBy 2 ##followedBy 3");
		parser = new Parser(stream);
		final List<Lexi> directives = parser.directives();
		assertEquals(directives.size(), 1);
		final Node dir = directives.get(0).keyword;
		assertEquals(dir.snippet().getText(), "(identifier)");
		assertEquals(dir.list().size(), 3);
		assertEquals(((TriviaToken) dir.list().get(0)).getText(), " ");
		assertEquals(((HostToken) dir.list().get(1)).getText(), "1");
		assertEquals(((TriviaToken) dir.list().get(0)).getText(), " ");
		// check elaborator
		assertEquals(2, dir.elaborators().size());
		// first
		Elaborator e = dir.elaborators().get(0);
		assertEquals(e.name(), "$followedBy");
		assertEquals(3, e.list().size());
		assertEquals(((TriviaToken) e.list().get(0)).getText(), " ");
		assertEquals(((HostToken) e.list().get(1)).getText(), "2");
		assertEquals(((TriviaToken) e.list().get(2)).getText(), " ");
		// second
		e = dir.elaborators().get(1);
		assertEquals(e.name(), "$followedBy");
		assertEquals(2, e.list().size());
		assertEquals(((TriviaToken) e.list().get(0)).getText(), " ");
		assertEquals(((HostToken) e.list().get(1)).getText(), "3");
	}

	@Test
	public void testSimpleExample() throws IOException {
		aux.runLibTest("examples/SimpleExample");
	}

	@Test
	public void testSquared() throws IOException {
		aux.runLibTest("examples/squared");
	}

	@Test
	public void testStritch() throws IOException {
		final Reader stream = new FileReader(new File("./lola_libs/examples/stritch.lola"));
		parser = new Parser(stream);
		aux.assertTEquals("if(str.equals(\"omg\")){ print(7);} if(str.equals(\"ooo\")){ print(8);}",
				aux.list2string(parser.parse()));
	}

	@Test
	public void testTwoSeparatedAny() {
		aux.runStringTest("##Find ##Any b c ##Any d\n ##replace \n7 b c 7 d", "");
	}

	@Test
	public void testUnbalancedAnyMatchesEverything() throws IOException {
		aux.runStringTest(
				"##Find 1 ##Unbalanced ##Any 2 ##replace replaced\n##example 1(this)2 1((not this)2 1 this 2\n##resultsIn replaced replaced replaced");
	}

	@Test
	public void testUserDefinedFind() throws IOException {
		aux.runStringTest(
				"##Find(Lol) ##OneOrMore lol \n##Find ##Lol ##replace hah\n##example lol lol lol\n##resultsIn hah hah hah");
	}

	@Test
	public void testUserDefinedRange() {
		aux.runStringTest(
				"##Find(Range) ##Literal(begin)-##Literal(end)\n##Find ##Range(r) ##replace ##(r.begin)*##(r.end)\n##example here: 'a'-'b' ##resultsIn here: 'a'*'b'");
	}

	enum aux {
		;
		static void assertListEquals(final List<?> li1, final List<?> li2) {
			assertEquals(li1.size(), li2.size());
			for (int i = 0; i < li1.size(); ++i)
				assertEquals(li2.get(i), li1.get(i));
		}

		static void assertTEquals(final String s1, final String s2) {
			assertEquals(s1.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", ""),
					s2.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", ""));
		}

		static String list2string(final List<String> ss) {
			return ss.stream().reduce("", (s1, s2) -> s1 + s2);
		}

		static void runFileTest(final File f) {
			// System.out.println("***" + f.getName());
			Reader stream;
			try {
				stream = new FileReader(f);
				parser = new Parser(stream);
				aux.assertTEquals("", aux.list2string(parser.parse()));
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		static void runFileTest(final String fileName) {
			runFileTest(new File("./src/il/ac/technion/cs/ssdl/lola/parser/tests/" + fileName + ".lola"));
		}

		static void runLibTest(final String fileName) {
			runFileTest(new File("./lola_libs/" + fileName + ".lola"));
		}

		static void runStringTest(final String s) {
			runStringTest(s, "");
		}

		static void runStringTest(final String s, final String result) {
			final Reader stream = new StringReader(s);
			parser = new Parser(stream);
			try {
				aux.assertTEquals(result, aux.list2string(parser.parse()));
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		void printList(final List<String> ss) {
			for (final String s : ss)
				System.out.println(s);
		}
	}
}
