package il.ac.technion.cs.ssdl.lola.parser.re;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
public class KlineBranch implements Cloneable {
	private final RegExp source;
	public List<RegExp> res = new ArrayList<>();
	private int idx;

	KlineBranch(final RegExp re) {
		res.add(re);
		source = re.clone();
	}

	public void apply(final PythonAdapter a) {
		for (final RegExp ¢ : res)
			¢.apply(a);
	}

	@Override
	public KlineBranch clone() {
		final List<RegExp> newRes = new ArrayList<>();
		for (final RegExp ¢ : res)
			newRes.add(¢.clone());
		final KlineBranch $ = new KlineBranch(source);
		$.idx = idx;
		$.res = newRes;
		return $;
	}

	public boolean diverges(final Bunny ¢) {
		return res.get(idx).satiated() && source.eats(¢) && res.get(idx).eats(¢);
	}

	public boolean eats(final Bunny ¢) {
		return res.get(idx).satiated() && source.eats(¢) || res.get(idx).eats(¢);
	}

	public void feed(final Bunny b) {
		if (res.get(idx).eats(b))
			res.get(idx).feed(b);
		else {
			final RegExp newRe = source.clone();
			newRe.feed(b);
			res.add(newRe);
			++idx;
		}
	}

	public KlineBranch feedAndGetDivergent(final Bunny ¢) {
		assert res.get(idx).eats(¢);
		assert res.get(idx).satiated() && source.eats(¢);
		// new branch
		final KlineBranch $ = clone();
		$.res.add(source.clone());
		++$.idx;
		$.feed(¢);
		// old one
		res.get(idx).feed(¢);
		return $;
	}

	public boolean satiated() {
		return res.get(idx).satiated();
	}

	public String text() {
		String $ = "";
		for (final RegExp ¢ : res)
			$ += ¢.text();
		return $;
	}
}
