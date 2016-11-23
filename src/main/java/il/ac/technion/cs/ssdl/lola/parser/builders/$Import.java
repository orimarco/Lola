package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
import il.ac.technion.cs.ssdl.lola.utils.iz;
public class $Import extends UnacceptableGeneratingKeyword {
	static List<String> imported = new ArrayList<>();

	public $Import(final Token token) {
		super(token);
	}

	@Override
	public boolean accepts(final Node b) {
		return iz.snippetToken(b) && snippet == null || b.token.isTrivia();
	}

	@Override
	public void adopt(final Node b) {
		if (b.token.isTrivia()) {
			list.add(b);
			return;
		}
		snippet = (SnippetToken) b;
		state = Automaton.Done;
	}

	@Override
	public String generate(final PythonAdapter __) {
		/* get file name, cut the "" from the file name... */
		final String fileName = snippet.getExpression();
		if (imported.contains(fileName))
			return "";
		imported.add(fileName);
		try {
			return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
			// TODO: Should we worry about encoding?
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
