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
		RegExp re = newRegExp("##Find\n ##Sequence ##Any ##delete");
		assertTrue(re.eats(bunny("a")));
		assertTrue(re.eats(bunny("*")));
	}
}