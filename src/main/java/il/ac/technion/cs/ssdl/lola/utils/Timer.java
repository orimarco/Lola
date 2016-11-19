package il.ac.technion.cs.ssdl.lola.utils;
/**
 * Created by sorimar on 9/21/2016.
 */
public class Timer {
	private static long start;

	public static void start() {
		start = System.currentTimeMillis();
	}

	public static long end() {
		return (System.currentTimeMillis() - start) / 1000;
	}
}
