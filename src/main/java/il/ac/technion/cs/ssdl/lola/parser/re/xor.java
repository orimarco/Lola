package il.ac.technion.cs.ssdl.lola.parser.re;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;
public class xor extends Composite {
	public xor(final List<RegExp> res) {
		children.addAll(res);
	}

	@Override
	public void apply(final PythonAdapter a) {
		for (final RegExp ¢ : children)
			if (¢.satiated()) {
				¢.apply(a);
				return;
			}
	}

	@Override
	public xor clone() {
		final List<RegExp> childrenCopy = new ArrayList<>();
		for (final RegExp ¢ : children)
			childrenCopy.add(¢.clone());
		return new xor(childrenCopy);
	}

	@Override
	public boolean eats(final Bunny b) {
		for (final RegExp ¢ : children)
			if (¢.eats(b))
				return true;
		return false;
	}

	@Override
	public void feed(final Bunny b) {
		for (final RegExp ¢ : children)
			if (¢.eats(b))
				¢.feed(b);
			else
				children.remove(¢);
	}

	@Override
	public boolean satiated() {
		return children.stream().filter(re -> re.satiated()).count() == 1;
	}

	@Override
	public String text() {
		for (final RegExp ¢ : children)
			if (¢.satiated())
				return ¢.text();
		return "DID_NOT_MATCH";
	}

	@Override
	public String toString() {
		return "xor" + children.stream().map(x -> "\n+" + (x + "").replaceAll("\n", "\n-")).reduce("", (x, y) -> x + y);
	}
}
