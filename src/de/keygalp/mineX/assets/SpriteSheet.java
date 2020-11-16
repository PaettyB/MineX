package de.keygalp.mineX.assets;

public class SpriteSheet {
	
	
	private static int width = 128;
	//private static int height = 128;
	private static int gridSize = 16;
	private static int cols = width / gridSize;
	
	private static float tileSizeF = 1 / (float)cols;
	
	public static final int DIRT = 0;
	public static final int GRASS_SIDE = 1;
	public static final int GRASS_TOP = 2;
	public static final int STONE = 3;
	public static final int SAND = 4;
	public static final int OAK_LOG_SIDE = 5;
	public static final int OAK_LOG_TOP = 6;
	public static final int GRAVEL = 7;
	public static final int OAK_LEAVES = 8;
	
	
	public static float[] getTextureCoords(int id) {
		int colX = id % cols;
		int colY = (int) Math.floor(id / cols);
		
		float x0 = colX * tileSizeF;
		float y0 =  -1 + (colY) * tileSizeF;
		
		float[] coords = new float[]{
			x0, y0 + tileSizeF,
			x0, y0,
			x0 + tileSizeF, y0,
			x0 + tileSizeF, y0 + tileSizeF,
		};
		return coords;
	}

}
