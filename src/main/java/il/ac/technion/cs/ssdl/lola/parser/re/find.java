package il.ac.technion.cs.ssdl.lola.parser.re;
import java.util.ArrayList;
import java.util.List;

import il.ac.technion.cs.ssdl.lola.parser.PythonAdapter;
/**
 * @author Ori Marcovitch
 * @since Nov 24, 2016
 */
public class find extends sequence {
	public find(List<RegExp> res) {
		super(res);
	}

	/**
	 * @param res
	 * @param text
	 */
	public find(ArrayList<RegExp> res, String snippet) {
		super(res, snippet);
	}

	@Override
	public void apply(final PythonAdapter a) {
		if (snippet != null)
			a.enterGlobalScope(snippet.substring(snippet.indexOf('(') + 1, snippet.lastIndexOf(')')), text());
		for (final Branch ¢ : branches)
			if (¢.satiated()) {
				¢.apply(a);
				break;
			}
		if (snippet != null)
			a.exitScope();
	}
}
