package il.ac.technion.cs.ssdl.lola.parser.re;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;
public class OneOrMore extends Composite {
	public final List<KlineBranch> branches = new ArrayList<>();
	private final RegExp source;

	public OneOrMore(final RegExp re) {
		source = re.clone();
		branches.add(new KlineBranch(re));
	}

	@Override
	public void apply(final PythonAdapter a) {
		for (final KlineBranch ¢ : branches)
			if (¢.satiated()) {
				¢.apply(a);
				return;
			}
	}

	@Override
	public OneOrMore clone() {
		final OneOrMore $ = new OneOrMore(source);
		$.branches.clear();
		for (final KlineBranch ¢ : branches)
			$.branches.add(¢.clone());
		return $;
	}

	@Override
	public boolean eats(final Bunny b) {
		/* check if any branch can accept token */
		for (final KlineBranch ¢ : branches)
			if (¢.eats(b))
				return true;
		return false;
	}

	@Override
	public void feed(final Bunny b) {
		KlineBranch newBranch = null;
		final List<KlineBranch> toRemove = new ArrayList<>();
		for (final KlineBranch ¢ : branches)
			if (!¢.eats(b))
				toRemove.add(¢);
			else if (!¢.diverges(b))
				¢.feed(b);
			else
				newBranch = ¢.feedAndGetDivergent(b);
		if (newBranch != null)
			branches.add(newBranch);
		for (final KlineBranch ¢ : toRemove)
			branches.remove(¢);
	}

	@Override
	public boolean satiated() {
		for (final KlineBranch ¢ : branches)
			if (¢.satiated())
				return true;
		return false;
	}

	@Override
	public String text() {
		for (final KlineBranch ¢ : branches)
			if (¢.satiated())
				return ¢.text();
		return "DID_NOT_MATCH";
	}

	@Override
	public String toString() {
		return "oneOrMore" + "\n+" + source.toString().replaceAll("\n", "\n-");
	}
}