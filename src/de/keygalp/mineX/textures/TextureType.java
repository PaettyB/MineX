package de.keygalp.mineX.textures;

import de.keygalp.mineX.assets.SpriteSheet;
import de.keygalp.mineX.inventory.Material;
import de.keygalp.mineX.worlds.Direction;

import java.util.HashMap;
import java.util.Map;

public class TextureType {

	public static int TYPE_1 = 1;
	public static int TYPE_2 = 2;
	public static int TYPE_3 = 3;

	public static Map<Integer, int[]> textureTypes = new HashMap<Integer, int[]>();

	public static void loadTextureTypes() {
		textureTypes.put(Material.DIRT.getId(), new int[] { SpriteSheet.DIRT });
		textureTypes.put(Material.GRASS.getId(),
				new int[] { SpriteSheet.GRASS_SIDE, SpriteSheet.GRASS_TOP, SpriteSheet.DIRT });
		textureTypes.put(Material.STONE.getId(), new int[] { SpriteSheet.STONE });
		textureTypes.put(Material.OAK_LOG.getId(), new int[] { SpriteSheet.OAK_LOG_SIDE, SpriteSheet.OAK_LOG_TOP });
	}

	public static int getTextureType(int blockID, int direction) {
		int[] arr = textureTypes.get(blockID);
		if (direction == Direction.SOUTH || direction == Direction.NORTH || direction == Direction.WEST
				|| direction == Direction.EAST) {
			return arr[0];
		}
		if (arr.length == 1) {
			return arr[0];
		} else if (arr.length == 2) {
			if (direction == Direction.TOP || direction == Direction.BOTTOM) {
				return arr[1];
			}
		} else if (arr.length == 3) {
			if(direction == Direction.TOP) {
				return arr[1];
			} else if(direction == Direction.BOTTOM) {
				return arr[2];
			}
		}
		return -1;
	}
}
