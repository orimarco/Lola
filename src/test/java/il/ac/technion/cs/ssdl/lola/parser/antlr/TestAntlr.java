package il.ac.technion.cs.ssdl.lola.parser.antlr;

import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;

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
		ParseTree tree = parser.expression();
		System.out.println(tree.getText());

	}
}

class DescriptiveErrorListener extends BaseErrorListener {
	public static DescriptiveErrorListener INSTANCE = new DescriptiveErrorListener();

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		System.out.println("ohhhh");
	}
}