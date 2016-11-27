package il.ac.technion.cs.ssdl.lola.parser.re;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
public class Branch implements Cloneable {
	public List<RegExp> res;
	private int idx;

	Branch(final List<RegExp> res, final int idx) {
		this.res = res;
		this.idx = idx;
	}

	public void apply(final PythonAdapter a) {
		for (final RegExp ¢ : res)
			¢.apply(a);
	}

	@Override
	public Branch clone() {
		final List<RegExp> newRes = new ArrayList<>();
		for (final RegExp ¢ : res)
			newRes.add(¢.clone());
		return new Branch(newRes, idx);
	}

	public boolean diverges(final Bunny b) {
		int divergents = 0;
		for (int ¢ = idx; ¢ < res.size(); ++¢) {
			if (res.get(¢).eats(b))
				++divergents;
			if (!res.get(¢).satiated())
				break;
		}
		return divergents > 1;
	}

	public boolean eats(final Bunny ¢) {
		System.out.println("seq: eats? [" + ¢.text() + "]" + eats_aux(¢));
		System.out.println(res.get(idx).getClass().getSimpleName());
		return eats_aux(¢);
	}

	private boolean eats_aux(final Bunny b) {
		for (int ¢ = idx; ¢ < res.size(); ++¢) {
			if (res.get(¢).eats(b))
				return true;
			if (!res.get(¢).satiated())
				break;
			// System.out.println("++");
		}
		return false;
	}

	/** Assuming eats(b) */
	public void feed(final Bunny ¢) {
		for (; idx < res.size(); ++idx)
			if (res.get(idx).eats(¢)) {
				res.get(idx).feed(¢);
				return;
			}
		res.get(res.get(idx).eats(¢) ? idx : ++idx).feed(¢);
	}

	public List<Branch> feedAndGetDivergents(final Bunny b) {
		assert eats(b);
		final ArrayList<Branch> $ = new ArrayList<>();
		for (; idx < res.size(); ++idx) {
			if (res.get(idx).eats(b)) {
				final Branch newBranch = clone();
				newBranch.feed(b);
				$.add(newBranch);
			}
			if (!res.get(idx).satiated())
				break;
		}
		return $;
	}

	public boolean satiated() {
		return idx + 1 == res.size() && res.get(idx).satiated();
	}

	public String text() {
		return res.stream().map(re -> re.text()).reduce("", (s1, s2) -> s1 + " " + s2);
	}
}
