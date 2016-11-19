package il.ac.technion.cs.ssdl.lola.parser.builders;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import il.ac.technion.cs.ssdl.lola.parser.*;
import il.ac.technion.cs.ssdl.lola.parser.builders.AST.*;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
public class $Include extends UnacceptableGeneratingKeyword {
	public $Include(final Token token) {
		super(token);
	}

	@Override
	public boolean accepts(final Node b) {
		return b.token.isSnippet() || b.token.isTrivia();
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
		final String fileName = snippet.getExpression();
		try {
			return new String(Files.readAllBytes(Paths.get(fileName)),
					StandardCharsets.UTF_8);
			// TODO: Should we worry about encoding?
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
