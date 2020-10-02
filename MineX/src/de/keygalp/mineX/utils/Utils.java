package de.keygalp.mineX.utils;

import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3i;

import de.keygalp.mineX.renderEngine.DisplayManager;
import de.keygalp.mineX.worlds.ChunkSection;

public class Utils {

	public static void printMap(Map<Vector3i, ChunkSection> map) {
		for (Map.Entry<Vector3i, ChunkSection> entry : map.entrySet()) {
			System.out.println(entry.getKey().x + ", " + entry.getKey().y + ", " + entry.getKey().z);
		}
	}

	public static int mapInt(int input, int input_start, int input_end, int output_start, int output_end) {
		return (int) Math
				.round(output_start + ((double) (output_end - output_start) / (double) (input_end - input_start))
						* (double) (input - input_start));
	}

	public static double mapDouble(double input, double input_start, double input_end, double output_start,
			double output_end) {
		return output_start + ((output_end - output_start) / (input_end - input_start)) * (input - input_start);
	}

	public static Vector2f toNormalScale(Vector2i scale) {
		float x = scale.x * 2 / (float) DisplayManager.WIDTH;
		float y = scale.y * 2 / (float) DisplayManager.HEIGHT;
		return new Vector2f(x, y);
	}
	
	public static float toNormalLineWidth(int scale) {
		return (scale / (float) DisplayManager.WIDTH);
	}

	
	public static float toNormalScale(int scale) {
		return (scale* 2 / (float) DisplayManager.WIDTH);
	}
	
	public static Vector2f toNormalPosition(Vector2i pos) {
		// float x = pos.x * 2 / (float) DisplayManager.WIDTH - 1;
		// float y = -pos.y * 2 / (float) DisplayManager.HEIGHT + 1;
		float x = (float) mapDouble(pos.x, 0, DisplayManager.WIDTH, -1,1);
		float y = (float) mapDouble(pos.y, 0, DisplayManager.HEIGHT, 1, -1);
		return new Vector2f(x, y);
	}
	
	public static byte addDirection(byte b, int dir) {
		return (byte) (b | (byte) Math.pow(2, dir));
	}
	
	public static boolean isDirection(byte b, int dir) {
		byte test = (byte) Math.pow(2, dir);
		return ((b & test) == test);
	}
	
	public static byte removeDirection(byte b, int dir) {
		if(isDirection(b, dir))
			return (byte) (b ^ (byte)Math.pow(2, dir));
		return b;
	}
}
