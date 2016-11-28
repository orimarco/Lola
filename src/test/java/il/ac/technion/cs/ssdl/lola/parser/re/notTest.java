package il.ac.technion.cs.ssdl.lola.parser.re;
import static org.junit.Assert.*;

import org.junit.Test;
import static il.ac.technion.cs.ssdl.lola.parser.re.RegExpFactory.*;
/**
 * @author Ori Marcovitch
 * @since Nov 24, 2016
 */
public class notTest {
	@Test
	public void a0() {
		RegExp re = newRegExp("##Find\n ##not a");
		assertTrue(re.eats(bunny("a")));
		assertTrue(re.eats(bunny("whatever")));
	}

	@Test
	public void a1() {
		RegExp re = newRegExp("##Find\n ##not a");
		re.feed(bunny("a"));
		assertFalse(re.satiated());
		re.feed(bunny("b"));
		assertTrue(re.satiated());
	}

	@Test
	public void a2() {
		RegExp re = newRegExp("##Find\n ##not ##sequence a ##any");
		re.feed(bunny("a"));
		assertFalse(re.satiated());
		re.feed(bunny("b"));
		assertFalse(re.satiated());
	}
}
