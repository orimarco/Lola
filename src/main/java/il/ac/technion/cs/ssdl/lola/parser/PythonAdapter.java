package il.ac.technion.cs.ssdl.lola.parser;
import java.util.*;

import org.python.util.*;

import il.ac.technion.cs.ssdl.lola.utils.*;
public class PythonAdapter {
	private static String generateVriable() {
		return "val" + (int) (Math.random() * Integer.MAX_VALUE);
	}
	private final PythonInterpreter pi = new PythonInterpreter();
	private final Stack<String> scope = new Stack<>();
	Stack<String> forEachScopes = new Stack<>();
	Stack<String> forEachIterationScopes = new Stack<>();
	private Set<String> globalVariables = new HashSet<>();

	public PythonAdapter() {
		exec("class Scope(object):\n\tdef __str__(self):\n\t\treturn self.name");
		exec("class Identifier(object):\n\tdef __str__(self):\n\t\treturn self.name");
	}

	public void addCharVariable(final String name, final String content) {
		addVariable(name, "\"" + content + "\"");
	}

	public void addIdentifier(final String name, final String matching) {
		exec(scope() + name + "= Identifier()");
		if (!scope.empty()) {
			exec(scope() + name + ".name = " + matching);
			exec("if not hasattr(" + scopeName() + ", '" + name + "s'):\n\t" + scope() + name + "s = list()");
			exec(scope() + name + "s.append(" + scope() + name + ")");
		} else {
			exec(name + ".name = " + matching);
			exec("if '" + name + "s' not in locals():\n\t" + name + "s = list()");
			exec(name + "s.append(" + name + ")");
			registerGlobal(name);
		}
	}

	public void addStringVariable(final String name, final String content) {
		addVariable(name, "\'" + content + "\'");
	}

	public void addVariable(final String name, final String content) {
		if (!scope.empty()) {
			exec(scope() + name + " = " + content);
			exec("if not hasattr(" + scopeName() + ", '" + name + "s'):\n\t" + scope() + name + "s = list()");
			exec(scope() + name + "s.append(" + scope() + name + ")");
		}
		registerGlobal(name);
		exec(name + " = " + content);
		exec("if '" + name + "s' not in locals():\n\t" + name + "s = list()");
		exec(name + "s.append(" + name + ")");
	}

	public String eavluateStringExpression(final String e) {
		final String val = generateVriable();
		exec(val + "= " + e + "\nif isinstance(" + val + ",Identifier) or isinstance(" + val + ",Scope):\n\t" + val + " = "
				+ val + ".name\n" + val + " = " + val + ".__str__()\n");
		return pi.get(val).asString();
	}

	public void enterScope(final String name, final String matching) {
		exec(scope() + name + "= Scope()");
		final String escapedMatching = matching.replace("'", "\\'").replace("\n", "\\n").replace("\r", "\\r");
		exec(scope() + name + ".name = '" + escapedMatching + "'");
		if (!scope.empty()) {
			exec("if not hasattr(" + scopeName() + ", '" + name + "s'):\n\t" + scope() + name + "s = list()");
			exec(scope() + name + "s.append(" + scope() + name + ")");
		} else {
			exec("if '" + name + "s' not in locals():\n\t" + name + "s = list()");
			registerGlobal(name + "s");
			exec(name + "s.append(" + name + ")");
		}
		scope.push(name);
	}

	/**
	 * @param name
	 */
	private void registerGlobal(String name) {
		globalVariables.add(name);
	}

	public boolean evaluateBooleanExpression(final String e) {
		exec("if " + e + ":\n\tval=1\nelse:\n\tval=0");
		return pi.get("val").asInt() == 1;
	}

	public void exitScope() {
		scope.pop();
	}

	public void forEachAfter() {
		forEachScopes.pop();
		if (!forEachIterationScopes.isEmpty())
			forEachIterationScopes.pop();
		if (!forEachIterationScopes.isEmpty())
			exec(forEachIterationScopes.peek());
	}

	public int forEachBeforeAndGetIterationsNum(final String e) {
		final String var = generateVriable();
		forEachScopes.push(var);
		// exec("print locals()");
		exec(var + " = " + e);
		return pi.eval("len(" + var + ")").asInt();
	}

	public void forEachBeforeIteration(final int i) {
		final String assignment = "_=" + forEachScopes.peek() + "[" + i + "]";
		if (i != 0)
			forEachIterationScopes.pop();
		forEachIterationScopes.push(assignment);
		exec(assignment);
	}

	public void runShellCode(final String code) {
		exec(code);
	}

	public String scope() {
		String $ = "";
		for (final String ¢ : scope)
			$ += ¢ + ".";
		return $;
	}

	private void exec(final String ¢) {
		Printer.printCommand(¢);
		pi.exec(¢);
	}

	private String scopeName() {
		final String tmp = scope();
		return tmp.substring(0, tmp.length() - 1);
	}

	/**
	 * 
	 */
	public void afterLexi() {
		for (String ¢ : globalVariables)
			exec("del " + ¢);
		globalVariables = new HashSet<>();
	}

	/**
	 * @param substring
	 * @param text
	 */
	public void enterGlobalScope(String name, String matching) {
		exec(scope() + name + "= Scope()");
		final String escapedMatching = matching.replace("'", "\\'").replace("\n", "\\n").replace("\r", "\\r");
		exec(scope() + name + ".name = '" + escapedMatching + "'");
		if (scope.empty()) {
			exec("if '" + name + "s' not in locals():\n\t" + name + "s = list()");
			exec(name + "s.append(" + name + ")");
		} else {
			exec("if not hasattr(" + scopeName() + ", '" + name + "s'):\n\t" + scope() + name + "s = list()");
			exec(scope() + name + "s.append(" + scope() + name + ")");
		}
		scope.push(name);
	}
}
