package il.ac.technion.cs.ssdl.lola.parser.re;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;
public class and extends Composite {
	public and(final List<RegExp> children) {
		this.children.addAll(children);
	}

	@Override
	public void apply(final PythonAdapter a) {
		for (final RegExp ¢ : children)
			¢.apply(a);
	}

	@Override
	public and clone() {
		final List<RegExp> childrenCopy = new ArrayList<>();
		for (final RegExp ¢ : children)
			childrenCopy.add(¢.clone());
		return new and(childrenCopy);
	}

	@Override
	public boolean eats(final Bunny b) {
		return children.stream().filter(re -> re.eats(b)).count() == children.size();
	}

	@Override
	public void feed(final Bunny b) {
		for (final RegExp ¢ : children)
			¢.feed(b);
	}

	@Override
	public boolean satiated() {
		return children.stream().filter(re -> re.satiated()).count() == children.size();
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
		return "and" + children.stream().map(x -> "\n+" + (x + "").replaceAll("\n", "\n|")).reduce("", (x, y) -> x + y);
	}
}
