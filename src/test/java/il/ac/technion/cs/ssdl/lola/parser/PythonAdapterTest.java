package il.ac.technion.cs.ssdl.lola.parser;

import static org.junit.Assert.*;

import org.junit.*;

public class PythonAdapterTest {
  @Test public void testBooleanExpression() {
    assertEquals(true, new PythonAdapter().evaluateBooleanExpression("True"));
    assertEquals(false, new PythonAdapter().evaluateBooleanExpression("False"));
    assertEquals(false, new PythonAdapter().evaluateBooleanExpression("3 == 2"));
    assertEquals(false, new PythonAdapter().evaluateBooleanExpression("2 + 3 != 5"));
  }
  @Test public void testStringExpression() {
    assertEquals("7", new PythonAdapter().eavluateStringExpression("7"));
    assertEquals("6", new PythonAdapter().eavluateStringExpression("2 + 2/2 + 3"));
    assertEquals("[1, 2, 3]", new PythonAdapter().eavluateStringExpression("[x for x in [1,2,3]]"));
  }
}
