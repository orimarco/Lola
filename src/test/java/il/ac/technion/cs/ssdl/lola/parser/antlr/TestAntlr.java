package il.ac.technion.cs.ssdl.lola.parser.antlr;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
/**
 * @author Ori Marcovitch
 * @Since 2016
 * @mail sorimar@cs.technion.ac.il
 */
public class TestAntlr {
	public static void main(String args[]) throws IOException {
		Lexer lexer = new JavaLexer(new ANTLRInputStream("omg(a,b  , c)"));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaParser parser = new JavaParser(tokens);
		lexer.removeErrorListeners();
		parser.removeErrorListeners();
		parser.addErrorListener(DescriptiveErrorListener.INSTANCE);
		System.out.println(parser.expression().getText());
	}
}

class DescriptiveErrorListener extends BaseErrorListener {
	public static DescriptiveErrorListener INSTANCE = new DescriptiveErrorListener();

	@Override
	public void syntaxError(Recognizer<?, ?> __, Object offendingSymbol, int line, int charPositionInLine, String msg,
			RecognitionException x) {
		System.out.println("ohhhh");
	}
}