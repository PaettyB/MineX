package de.keygalp.mineX.worlds;

import org.joml.Vector2i;

import de.keygalp.mineX.utils.ImprovedNoise;
import de.keygalp.mineX.utils.Utils;

public class TerrainGenerator {

	public static final int maxHeight = 130;
	public static final int minHeight = 70;
	public static final float detail = 150f;

	public static byte[][] GenerateNoiseMap(int mapWidth, int mapHeight, int seed, float scale, int octaves,
			float persistance, float lacunarity, Vector2i offset) {
		byte[][] noiseMap = new byte[mapWidth][mapHeight];

		//Random prng = new Random(seed);
		Vector2i[] octaveOffsets = new Vector2i[octaves];
		for (int i = 0; i < octaves; i++) {
			// int offsetX = prng.nextInt(10000) + offset.x;
			// int offsetY = prng.nextInt(100000) + offset.y;
			int offsetX = offset.x;
			int offsetY = offset.y;

			octaveOffsets[i] = new Vector2i(offsetX, offsetY);
		}

		if (scale <= 0) {
			scale = 0.0001f;
		}

		float maxNoiseHeight = Float.MIN_VALUE;
		float minNoiseHeight = Float.MAX_VALUE;

		//float halfWidth = mapWidth * 0.5f;
		//float halfHeight = mapHeight * 0.5f;

		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				float amplitude = 1;
				float frequency = 1;
				float noiseHeight = 0;

				for (int i = 0; i < octaves; i++) {

					//float sampleX = (x - halfWidth) / scale * frequency + octaveOffsets[i].x;
					//float sampleY = (y - halfHeight) / scale * frequency + octaveOffsets[i].y;

					float perlinValue = (float) Math
							.abs(ImprovedNoise.noise((octaveOffsets[i].x * Chunk.SIZE + x) / scale * frequency,
									(octaveOffsets[i].y * Chunk.SIZE + y) / scale * frequency, 0.1) * 2);
					noiseHeight += perlinValue * amplitude;
					// System.out.println(sampleX);
					amplitude *= persistance;
					frequency *= lacunarity;
				}
				if (noiseHeight > maxNoiseHeight)
					maxNoiseHeight = noiseHeight;
				else if (noiseHeight < minNoiseHeight)
					minNoiseHeight = noiseHeight;

				double h = Utils.mapDouble(noiseHeight, minNoiseHeight, maxNoiseHeight, minHeight, maxHeight);
				noiseMap[x][y] = (byte) Math.round(h);
			}
		}

		// System.out.println("Max: " + maxNoiseHeight + "\nMin: " + minNoiseHeight);

		return noiseMap;
	}

	public static byte[][] getHeights(int x, int z) {
		byte[][] heights = new byte[Chunk.SIZE][Chunk.SIZE];
		for (int i = 0; i < Chunk.SIZE; i++) {
			for (int k = 0; k < Chunk.SIZE; k++) {

				double h = ((ImprovedNoise.noise((x * Chunk.SIZE + i) / detail, (z * Chunk.SIZE + k) / detail, 1)));

				double val = Utils.mapDouble(h, -0.5, 0.5, minHeight, maxHeight);

				heights[i][k] = (byte) Math.round(val);

			}
		}
		return heights;
	}

}
