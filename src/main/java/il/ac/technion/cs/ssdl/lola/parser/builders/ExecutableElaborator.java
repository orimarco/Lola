package il.ac.technion.cs.ssdl.lola.parser.builders;
import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.utils.*;
public abstract class ExecutableElaborator extends Elaborator {
	public ExecutableElaborator(final Token t) {
		super(t);
		state = Automaton.List;
	}

	@Override
	public boolean accepts(final AST.Node b) {
		return state == Automaton.List && !(b instanceof Elaborator)
				&& !(b instanceof $Find);
	}

	@Override
	public void adopt(final AST.Node b) {
		if (state == Automaton.List)
			list.add(b);
	}

	@Override
	public Node done() {
		if (!list.isEmpty()
				&& "enter".equals(list.get(list.size() - 1).categoryName()))
			list.remove(list.size() - 1);
		return this;
	}

	public abstract void execute(Chain<Bunny, Lexi>.Interval i, PythonAdapter a,
			Parser p);
}
