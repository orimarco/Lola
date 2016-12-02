package il.ac.technion.cs.ssdl.lola.parser.re;
import static il.ac.technion.cs.ssdl.lola.parser.re.RegExpFactory.bunny;
import static il.ac.technion.cs.ssdl.lola.parser.re.RegExpFactory.newRegExp;
import static org.junit.Assert.*;

import org.junit.Test;
/**
 * @author Ori Marcovitch
 * @since Dec 2, 2016
 */
public class orTest {
	@Test
	public void a0() {
		RegExp re = newRegExp("##Find ##Either a ##or a");
		System.out.println(re);
		assertFalse(re.satiated());
		assertTrue(re.eats(bunny("a")));
		re.feed(bunny("a"));
		assertTrue(re.satiated());
		assertFalse(re.eats(bunny("a")));
	}
}
