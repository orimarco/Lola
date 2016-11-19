package il.ac.technion.cs.ssdl.lola.parser;
import java.io.*;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.parser.tokenizer.*;
/**
 * The entire existence of this class is to supply balancing service for the
 * parser. The class is initialized by the JFlexer. Each map entry is of the
 * form [begin : List<end>]
 */
public enum Balancing {
	;
	private static final Map<String, String> map = new HashMap<>();
	private static final Set<String> closers = new HashSet<>();

	public static void addBalancing(final String begin, final String end) {
		map.put(begin, end);
		closers.add(end);
	}

	/** Can the text ever be balanced? (By adding more tokens to it) */
	public static boolean hasHope(final String text) {
		final Stack<String> stack = new Stack<>();
		try {
			checkIfBalanced(text, stack);
		} catch (final UnbalancedException e) {
			return false;
		}
		return true;
	}

	/** Is the text balanced according to the configuration file? */
	public static boolean isBalanced(final String text) {
		final Stack<String> stack = new Stack<>();
		try {
			checkIfBalanced(text, stack);
		} catch (final UnbalancedException e) {
			return false;
		}
		return stack.isEmpty();
	}

	private static void checkIfBalanced(final String text, final Stack<String> s)
			throws UnbalancedException {
		for (final Token ¢ : new Tokenizer(new StringReader(text)))
			if (!closers.contains(¢.text)) {
				if (map.keySet().contains(¢.text))
					s.push(¢.text);
			} else {
				if (s.isEmpty() || !map.get(s.peek()).equals(¢.text))
					throw new UnbalancedException();
				s.pop();
			}
	}
	private static class UnbalancedException extends Exception {
		private static final long serialVersionUID = 1L;

		private UnbalancedException() {
		}
	}
}
