package il.ac.technion.cs.ssdl.lola.main;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import il.ac.technion.cs.ssdl.lola.parser.Parser;
import il.ac.technion.cs.ssdl.lola.utils.format;
/**
 * @author Ori Marcovitch
 * @Since 2016
 * @mail sorimar@cs.technion.ac.il
 */
public class Lola {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: Lola <input file> <output file>");
			return;
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(args[1]));
			String code = format.code(new Parser(new FileReader(new File(args[0])))
					.parse().stream().reduce("", (s1, s2) -> s1 + s2));
			writer.write(code);
			System.out.println(code);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
