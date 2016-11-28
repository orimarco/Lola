package il.ac.technion.cs.ssdl.lola.parser.re;
import static org.junit.Assert.*;

import org.junit.Test;
import static il.ac.technion.cs.ssdl.lola.parser.re.RegExpFactory.*;
/**
 * @author Ori Marcovitch
 * @since Nov 24, 2016
 */
public class NoneOrMoreTest {
	@Test
	public void a0() {
		RegExp re = newRegExp("##Find\n ##NoneOrMore a");
		assertTrue(re.eats(bunny("a")));
		assertFalse(re.eats(bunny(",")));
	}

	@Test
	public void a1() {
		RegExp re = newRegExp("##Find\n ##NoneOrMore a\n ##delete");
		re.feed(bunny("a"));
		assertTrue(re.eats(bunny("a")));
		assertFalse(re.eats(bunny(",")));
	}

	@Test
	public void a2() {
		RegExp re = newRegExp("##Find\n ##NoneOrMore a\n ##delete");
		re.feed(bunny("a"));
		re.feed(bunny("a"));
		assertTrue(re.eats(bunny("a")));
		assertFalse(re.eats(bunny(",")));
	}

	@Test
	public void a3() {
		RegExp re = newRegExp("##Find\n ##NoneOrMore a\n ##delete");
		re.feed(bunny("a"));
		re.feed(bunny("a"));
		re.feed(identifier("a"));
		assertTrue(re.eats(bunny("a")));
		assertFalse(re.eats(bunny(",")));
	}

	@Test
	public void b0() {
		RegExp re = newRegExp("##Find\n ##NoneOrMore a\n		##separator ,\n		\n ##delete");
		assertTrue(re.eats(bunny("a")));
		assertFalse(re.eats(bunny(",")));
	}

	@Test
	public void b1() {
		RegExp re = newRegExp("##Find\n ##NoneOrMore a\n		##separator ,\n		\n ##delete");
		re.feed(bunny("a"));
		re.feed(bunny(","));
		assertTrue(re.eats(bunny("a")));
		assertFalse(re.eats(bunny(",")));
	}

	@Test
	public void b2() {
		RegExp re = newRegExp("##Find\n ##NoneOrMore a\n		##separator ,\n		\n ##delete");
		re.feed(bunny("a"));
		re.feed(bunny(","));
		re.feed(bunny("a"));
		assertFalse(re.eats(bunny("a")));
		assertTrue(re.eats(bunny(",")));
	}

	@Test
	public void c1() {
		RegExp re = newRegExp("##Find\n ##NoneOrMore ##Identifier(entry)\n		##separator ,\n		##closer ;\n ##delete");
		assertTrue(re.eats(identifier("a")));
		assertFalse(re.eats(bunny(",")));
	}

	@Test
	public void c2() {
		RegExp re = newRegExp("##Find\n ##NoneOrMore ##Identifier(entry)\n		##separator ,\n		##closer ;\n ##delete");
		re.feed(identifier("a"));
		assertFalse(re.eats(identifier("b")));
		assertTrue(re.eats(bunny(",")));
	}

	@Test
	public void c3() {
		RegExp re = newRegExp("##Find\n ##NoneOrMore ##Identifier(entry)\n		##separator ,\n		##closer ;\n ##delete");
		re.feed(identifier("a"));
		re.feed(bunny(","));
		assertTrue(re.eats(identifier("b")));
	}

	@Test
	public void c4() {
		RegExp re = newRegExp("##Find\n ##NoneOrMore a b");
		assertTrue(re.eats(identifier("a")));
		assertFalse(re.eats(identifier("b")));
		re.feed(identifier("a"));
		assertFalse(re.eats(identifier("a")));
		assertTrue(re.eats(identifier("b")));
		re.feed(identifier("b"));
		assertTrue(re.eats(identifier("a")));
		assertFalse(re.eats(identifier("b")));
		re.feed(identifier("a"));
		assertFalse(re.eats(identifier("a")));
		assertTrue(re.eats(identifier("b")));
		re.feed(identifier("b"));
		assertTrue(re.eats(identifier("a")));
		assertFalse(re.eats(identifier("b")));
		re.feed(identifier("a"));
		assertFalse(re.eats(identifier("a")));
		assertTrue(re.eats(identifier("b")));
		re.feed(identifier("b"));
	}
}
