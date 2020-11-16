package de.keygalp.mineX.utils;

public class Measure {

	public static long start;
	public static long stop;

	public static void start() {
		start = System.nanoTime();
	}

	public static void stop() {
		stop = System.nanoTime();
		int delta = Math.round((stop - start) / 1000.0f);
		System.out.println(String.format("Time: " + "% 5d s-6", delta));
		start = 0;
	}

}
