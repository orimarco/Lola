package il.ac.technion.cs.ssdl.lola.parser.re;
import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.re.RegExp.*;
public class not extends Composite {
	private final RegExp child;
	private boolean childDeadYet;
	private String text = "";

	public not(final RegExp re) {
		child = re;
	}

	@Override
	public void apply(final PythonAdapter __) {
	}

	@Override
	public not clone() {
		final not $ = new not(child.clone());
		$.text = text;
		return $;
	}

	@Override
	public boolean eats(final Bunny __) {
		return true;
	}

	@Override
	public void feed(final Bunny ¢) {
		if (!childDeadYet && child.eats(¢))
			child.feed(¢);
		else
			childDeadYet = true;
		text += ¢.text();
	}

	@Override
	public boolean satiated() {
		return childDeadYet || !child.satiated();
	}

	@Override
	public String text() {
		return text;
	}

	@Override
	public String toString() {
		return "not" + "\n+" + child;
	}
}
