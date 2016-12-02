package il.ac.technion.cs.ssdl.lola.parser.re;
import static il.ac.technion.cs.ssdl.lola.parser.re.RegExpFactory.bunny;
import static il.ac.technion.cs.ssdl.lola.parser.re.RegExpFactory.newRegExp;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
/**
 * @author Ori Marcovitch
 * @since Dec 2, 2016
 */
public class MatchTest {
	@Test
	public void a0() {
		RegExp re = newRegExp("##Find ##Match ##Any ##exceptFor ##Any, ##Any");
		assertTrue(re.satiated());
		assertTrue(re.eats(bunny("a")));
		re.feed(bunny("a"));
		assertTrue(re.satiated());
		assertTrue(re.eats(bunny(",")));
		re.feed(bunny(","));
		assertTrue(!re.satiated());
	}
}
