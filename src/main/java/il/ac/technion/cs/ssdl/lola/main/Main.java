package il.ac.technion.cs.ssdl.lola.main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import il.ac.technion.cs.ssdl.lola.parser.Parser;

/**
 * @author Ori Marcovitch
 * @Since 2016
 * @mail sorimar@cs.technion.ac.il
 */
public class Main {
	public static void main(String[] args) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(args[1]));
			writer.write(
					new Parser(new FileReader(new File(args[0]))).parse().stream().reduce("", (s1, s2) -> s1 + s2));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
