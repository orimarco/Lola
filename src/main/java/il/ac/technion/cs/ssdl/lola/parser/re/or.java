package il.ac.technion.cs.ssdl.lola.parser.re;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;
public class or extends Composite {
	public or(final List<RegExp> res) {
		children.addAll(res);
	}

	public or(final RegExp... res) {
		for (final RegExp ¢ : res)
			children.add(¢);
	}

	@Override
	public void apply(final PythonAdapter a) {
		// TODO: several children might satiate, we choose the first.
		for (final RegExp ¢ : children)
			if (¢.satiated()) {
				¢.apply(a);
				return;
			}
	}

	@Override
	public or clone() {
		final List<RegExp> childrenCopy = new ArrayList<>();
		for (final RegExp ¢ : children)
			childrenCopy.add(¢.clone());
		return new or(childrenCopy);
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
		final List<RegExp> toRemove = new ArrayList<>();
		for (final RegExp ¢ : children)
			if (¢.eats(b))
				¢.feed(b);
			else
				toRemove.add(¢);
		for (final RegExp ¢ : toRemove)
			children.remove(¢);
	}

	@Override
	public boolean satiated() {
		for (final RegExp ¢ : children)
			if (¢.satiated())
				return true;
		return false;
	}

	@Override
	public String text() {
		for (final RegExp ¢ : children)
			if (¢.satiated())
				return ¢.text();
		return "DID_NOT_MATCH";
	}
}
