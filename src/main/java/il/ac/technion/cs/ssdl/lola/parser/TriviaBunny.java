package il.ac.technion.cs.ssdl.lola.parser;
import il.ac.technion.cs.ssdl.lola.parser.lexer.*;
public class TriviaBunny implements Bunny {
	private final Token token;

	public TriviaBunny(final Token token) {
		this.token = token;
	}

	@Override
	public String text() {
		return token.text;
	}
}
