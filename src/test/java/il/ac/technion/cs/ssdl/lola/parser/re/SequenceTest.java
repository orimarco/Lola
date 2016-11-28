package il.ac.technion.cs.ssdl.lola.parser.re;
import static org.junit.Assert.*;

import org.junit.Test;
import static il.ac.technion.cs.ssdl.lola.parser.re.RegExpFactory.*;
/**
 * @author Ori Marcovitch
 * @since Nov 24, 2016
 */
public class SequenceTest {
	@Test
	public void a0() {
		RegExp re = newRegExp("##Find\n ##Sequence ##Any");
		assertTrue(re.eats(bunny("a")));
		assertTrue(re.eats(bunny("*")));
	}

	@Test
	public void a1() {
		RegExp re = newRegExp("##Find\n ##Sequence a ##Any");
		assertTrue(re.eats(bunny("a")));
		re.feed(bunny("a"));
		assertTrue(re.satiated());
	}

	@Test
	public void m0() {
		RegExp re = newRegExp("##Find\n ##Sequence case ##Match ##Any");
		assertTrue(re.eats(bunny("case")));
		re.feed(bunny("case"));
		assertTrue(re.eats(bunny("print")));
		assertTrue(re.eats(bunny("*")));
	}

	@Test
	public void m1() {
		RegExp re = newRegExp("##Match ##Any(body) ##exceptFor ##Sequence ##Any break; ##Any");
		System.out.println(re);
		assertTrue(re.eats(bunny("case")));
		re.feed(bunny("case"));
		assertTrue(re.eats(bunny("break")));
		re.feed(bunny("break"));
		assertTrue(re.eats(bunny(";")));
		re.feed(bunny(";"));
		assertFalse(re.satiated());
	}
}
