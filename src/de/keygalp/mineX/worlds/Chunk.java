package de.keygalp.mineX.worlds;

import org.joml.Vector2i;

import de.keygalp.mineX.renderEngine.MasterRenderer;
import de.keygalp.mineX.utils.Vector3b;

public class Chunk {

	public static final int HEIGHT = 16;
	public static final int SIZE = 16;

	// private Game game;
	private MasterRenderer renderer;
	private Vector2i position;

	private ChunkSection[] sections = new ChunkSection[SIZE];

	public Chunk(Vector2i position, MasterRenderer renderer) {
		// this.game = game;
		this.position = position;
		this.renderer = renderer;
		// System.out.println("Created");
	}

	public void populate(byte[][] heights) {

		for (int i = 0; i < HEIGHT; i++) {
			sections[i] = new ChunkSection(position.x, i, position.y);
			sections[i].populate(heights);
		}
	}

	public void generate(byte[] load_vertices, int[] load_indices, float[] load_textureCoords, byte[] load_normals) {
		for (int i = SIZE - 1; i >= 0; i--) {
			if (sections[i].getState() == ChunkState.UPDATED) {
				sections[i].regenerateMesh(load_vertices, load_indices, load_textureCoords, load_normals);
			} else {
				sections[i].calculateActives();
				sections[i].generate(load_vertices, load_indices, load_textureCoords, load_normals);
			}
		}
	}

	public void load() {
		for (int i = 0; i < HEIGHT; i++) {
			if (sections[i].getState() == ChunkState.REGENERATED || sections[i].getState() == ChunkState.LOADED) {
				sections[i].reload();
			} else {
				sections[i].load();
			}
			renderer.processChunk(sections[i]);

		}
	}

	public void unload() {
		for (int i = 0; i < HEIGHT; i++) {
			if (sections[i] != null) {
				sections[i].unload();
				renderer.removeChunk(sections[i]);
			}
		}
	}

	public void updateChunkBorderActives(int direction) {
		for (int i = 0; i < HEIGHT; i++) {
			if (sections[i] != null) {
				if (sections[i].getState() != ChunkState.CREATED && sections[i].getState() != ChunkState.POPULATED)
					sections[i].updateChunkBorderActives(direction);
			}
		}
	}

	public static Vector3b getChunkIndexfromBlockPos(int x, int y, int z) {
		byte bx = (byte) (x % Chunk.SIZE);
		byte by = (byte) (y % Chunk.SIZE);
		byte bz = (byte) (z % Chunk.SIZE);
		if (bx < 0)
			bx = (byte) (16 + bx);
		if (bz < 0)
			bz = (byte) (16 + bz);
		if (bz >= Chunk.SIZE || bx >= Chunk.SIZE || by >= Chunk.SIZE) {
			System.err.println(x + ", " + y + ", " + z);
			System.exit(2);
		}

		return new Vector3b(bx, by, bz);
	}

	public ChunkSection getSection(int y) {
		return sections[y];
	}

	public Vector2i getPosition() {
		return position;
	}
}
