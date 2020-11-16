package de.keygalp.mineX.worlds;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.fontMeshCreator.GUIText;
import de.keygalp.mineX.fontMeshCreator.TextMeshCreator;
import de.keygalp.mineX.inventory.Material;
import de.keygalp.mineX.renderEngine.DisplayManager;
import de.keygalp.mineX.renderEngine.MasterRenderer;
import de.keygalp.mineX.utils.Utils;
import de.keygalp.mineX.utils.Vector3b;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class World {

	public static final int RENDER_DISTANCE = 2;
	public static final int MAX_BUILD_HEIGHT = (Chunk.HEIGHT * ChunkSection.SIZE) - 1;
	public static final int SUPERFLAT_HEIGHT = 1;
	
	public static final float GRAVITY = -0.03f;
	public static final float AIR_RESITANCE = 0.95f;
	
	public static final boolean SUPERFLAT = true;

	private Thread thread;

	private int cX, cZ;

	private Map<Vector2i, Chunk> chunks = new ConcurrentHashMap<Vector2i, Chunk>();
	private List<Vector2i> chunksToPopulate = Collections.synchronizedList(new ArrayList<Vector2i>());
	private List<Vector2i> chunksToGenerate = Collections.synchronizedList(new ArrayList<Vector2i>());
	private List<Vector2i> chunksToLoad = Collections.synchronizedList(new ArrayList<Vector2i>());

	private List<Vector3i> chunkSectionsToRegenerate = Collections.synchronizedList(new ArrayList<Vector3i>());
	private List<Vector3i> chunkSectionsToReload = Collections.synchronizedList(new ArrayList<Vector3i>());

	private MasterRenderer renderer;

	private long vertexCount = 0;
	private long blockCount = 0;

	private GUIText chunkText, generateText, populateText, xText, yText, zText, vertexText, chunkRenderText;
	private static float fontSize = 0.025f;

	private byte[] load_vertices;
	private int[] load_indices;
	private float[] load_textureCoords;
	private byte[] load_normals;

	public World(MasterRenderer renderer) {
		this.renderer = renderer;

		chunkText = new GUIText("Chunks: " + chunks.size(), fontSize, new Vector2i(0, 0));
		populateText = new GUIText("Chunks to Populate: ", fontSize,
				new Vector2i(0, TextMeshCreator.getPixelLineHeight(fontSize)));
		generateText = new GUIText("Chunks to Generate: " + chunksToGenerate.size(), fontSize,
				new Vector2i(0, TextMeshCreator.getPixelLineHeight(fontSize) * 2));
		chunkRenderText = new GUIText("Chunks in Renderer: " + renderer.getChunkCount(), fontSize,
				new Vector2i(0, TextMeshCreator.getPixelLineHeight(fontSize) * 3));
		xText = new GUIText("X: 0000", fontSize, new Vector2i(0, TextMeshCreator.getPixelLineHeight(fontSize) * 4));
		yText = new GUIText("Y: 0000", fontSize, new Vector2i(0, TextMeshCreator.getPixelLineHeight(fontSize) * 5));
		zText = new GUIText("Z: 0000", fontSize, new Vector2i(0, TextMeshCreator.getPixelLineHeight(fontSize) * 6));
		vertexText = new GUIText("Vertices/Faces/Blocks: 0000", fontSize,
				new Vector2i(0, TextMeshCreator.getPixelLineHeight(fontSize) * 7));

		load_vertices = new byte[ChunkSection.MAX_VERTICES * 3];
		load_indices = new int[ChunkSection.volume * 6 * 3 * 3];
		load_textureCoords = new float[ChunkSection.MAX_VERTICES * 2];
		load_normals = new byte[ChunkSection.MAX_VERTICES * 3];
	}

	public void update() {

		// UNLOAD
		for (Map.Entry<Vector2i, Chunk> entry : chunks.entrySet()) {
			if (!chunkShouldBeLoaded(entry.getKey().x, entry.getKey().y)) {
				unloadChunk(entry.getKey());
			}
		}

		// CREATE CHUNKS
		Vector3f camPos = Game.getPlayer().getPosition();
		cX = (int) (camPos.x / (float) (ChunkSection.SIZE * 1.0f));
		cZ = (int) (camPos.z / (float) (ChunkSection.SIZE * 1.0f));

		for (int i = cX - RENDER_DISTANCE; i < cX + RENDER_DISTANCE; i++) {
			for (int k = cZ - RENDER_DISTANCE; k < cZ + RENDER_DISTANCE; k++) {
				if (!chunkExists(i, k)) {
					createChunk(i, k);
				}
			}
		}

		/*
		 * if (!chunkExists(0, 0)) { createChunk(0, 0); }
		 */

		// LOAD CHUNKS TO VAO
		if (chunksToLoad.size() > 0) {
			Vector2i pos = chunksToLoad.remove(0);
			try {
				chunks.get(pos).load();
			} catch (Exception e) {
				// chunksToLoad.add(pos);
				System.out.println("Could not load Chunk(" + pos.x + "," + pos.y + ")");

			}
		}

		// RELOAD CHUNK SECTIONS
		if (chunkSectionsToReload.size() > 0) {
			Vector3i pos = chunkSectionsToReload.remove(0);
			ChunkSection cs = getChunkSection(pos);
			if (cs.getState() == ChunkState.REGENERATED || cs.getState() == ChunkState.LOADED) {
				cs.reload();
			} else {
				cs.load();
			}
		}

		chunkText.update("Chunks: " + chunks.size());
		populateText.update("Chunks to Populate: " + chunksToPopulate.size());
		generateText.update("Chunks to Generate: " + chunksToGenerate.size());
		chunkRenderText.update("Chunks in Renderer: " + renderer.getChunkCount());
		String.format("X: %f / %d", Game.getPlayer().getPosition().x, Game.getPlayer().getBlockPosition().x);
		xText.update(String.format("X: %.2f / %d", Game.getPlayer().getPosition().x, Game.getPlayer().getBlockPosition().x));
		yText.update(String.format("Y: %.2f / %d", Game.getPlayer().getPosition().y, Game.getPlayer().getBlockPosition().y));
		zText.update(String.format("Z: %.2f / %d", Game.getPlayer().getPosition().z, Game.getPlayer().getBlockPosition().z));
		vertexText.update("Vertices / Faces / Blocks: " + vertexCount + " / " + vertexCount / 4 + " / " + blockCount);
	}

	public void startGeneration() {
		thread = new Thread() {
			@Override
			public void run() {
				while (!glfwWindowShouldClose(DisplayManager.getWindowId())) {
					generate();
				}
			}
		};
		thread.start();
		thread.setPriority(Thread.MIN_PRIORITY);
	}

	private void generate() {
		// POPULATE
		while (chunksToPopulate.size() > 0) {
			Vector2i pos = chunksToPopulate.remove(0);
			// System.out.println(pos.x+ ", "+pos.y);
			byte[][] heights = TerrainGenerator.getHeights(pos.x, pos.y);
			try {
				chunks.get(pos).populate(heights);

			} catch (Exception e) {
				// chunksToPopulate.add(pos);
				System.out.println("Could not populate Chunk(" + pos.x + "," + pos.y + ")");
				continue;
			}
			chunksToGenerate.add(pos);
			recalculateNeighbourBorders(pos);

		}
		// GENERATE
		if (chunksToGenerate.size() > 0) {
			Vector2i pos = chunksToGenerate.remove(0);
			try {
				chunks.get(pos).generate(load_vertices, load_indices, load_textureCoords, load_normals);
			} catch (Exception e) {
				// if(chunkShouldBeLoaded(pos.x,pos.y)) {
				// unloadChunk(pos);
				// createChunk(pos.x, pos.y);
				// }
				System.out.println("Could not generate Chunk(" + pos.x + "," + pos.y + ") " + e.toString());
				return;
			}
			chunksToLoad.add(pos);
		}

		// REGENERATE SECTIONS
		if (chunkSectionsToRegenerate.size() > 0) {
			Vector3i pos = chunkSectionsToRegenerate.remove(0);
			ChunkSection cs = getChunkSection(pos);
			if (cs.getState() == ChunkState.UPDATED) {
				cs.regenerateMesh(load_vertices, load_indices, load_textureCoords, load_normals);
			} else {
				cs.generate(load_vertices, load_indices, load_textureCoords, load_normals);
			}
			chunkSectionsToReload.add(pos);
		}
	}

	// CHUNK MANAGIN METHODS

	public void recalculateNeighbourBorders(Vector2i pos) {
		Chunk toUpdate;
		// Vector2i vec = new Vector2i();
		toUpdate = getChunk(new Vector2i(pos.x + Direction.SOUTH_VECTOR.x, pos.y + Direction.SOUTH_VECTOR.z));
		if (toUpdate != null) {
			toUpdate.updateChunkBorderActives(Direction.NORTH);
			chunksToGenerate.add(toUpdate.getPosition());
		}

		toUpdate = getChunk(new Vector2i(pos.x + Direction.NORTH_VECTOR.x, pos.y + Direction.NORTH_VECTOR.z));
		if (toUpdate != null) {
			toUpdate.updateChunkBorderActives(Direction.SOUTH);
			chunksToGenerate.add(toUpdate.getPosition());
		}

		toUpdate = getChunk(new Vector2i(pos.x + Direction.EAST_VECTOR.x, pos.y + Direction.EAST_VECTOR.z));
		if (toUpdate != null) {
			toUpdate.updateChunkBorderActives(Direction.WEST);
			chunksToGenerate.add(toUpdate.getPosition());
		}

		toUpdate = getChunk(new Vector2i(pos.x + Direction.WEST_VECTOR.x, pos.y + Direction.WEST_VECTOR.z));
		if (toUpdate != null) {
			toUpdate.updateChunkBorderActives(Direction.EAST);
			chunksToGenerate.add(toUpdate.getPosition());
		}
	}

	public void setActives(int x, int y, int z) {
		ChunkSection cs = getChunkSectionAtBlockPos(x, y, z);
		Vector3b indices = Chunk.getChunkIndexfromBlockPos(x, y, z);

		ChunkSection neighbour = getChunkSectionAtBlockPos(x, y, z - 1);
		Vector3b nIndices = Chunk.getChunkIndexfromBlockPos(x, y, z - 1);
		setActivesforDirection(cs, indices, neighbour, nIndices, Direction.SOUTH);

		neighbour = getChunkSectionAtBlockPos(x, y, z + 1);
		nIndices = Chunk.getChunkIndexfromBlockPos(x, y, z + 1);
		setActivesforDirection(cs, indices, neighbour, nIndices, Direction.NORTH);

		neighbour = getChunkSectionAtBlockPos(x - 1, y, z);
		nIndices = Chunk.getChunkIndexfromBlockPos(x - 1, y, z);
		setActivesforDirection(cs, indices, neighbour, nIndices, Direction.EAST);

		neighbour = getChunkSectionAtBlockPos(x + 1, y, z);
		nIndices = Chunk.getChunkIndexfromBlockPos(x + 1, y, z);
		setActivesforDirection(cs, indices, neighbour, nIndices, Direction.WEST);

		neighbour = getChunkSectionAtBlockPos(x, y + 1, z);
		nIndices = Chunk.getChunkIndexfromBlockPos(x, y + 1, z);
		setActivesforDirection(cs, indices, neighbour, nIndices, Direction.TOP);

		neighbour = getChunkSectionAtBlockPos(x, y - 1, z);
		nIndices = Chunk.getChunkIndexfromBlockPos(x, y - 1, z);
		setActivesforDirection(cs, indices, neighbour, nIndices, Direction.BOTTOM);
	}

	public void setActivesforDirection(ChunkSection cs, Vector3b indices, ChunkSection neighbour, Vector3b nIndices,
			int direction) {
		if (neighbour == null) {
			// if(indices.y == World.MAX_BUILD_HEIGHT && direction == Direction.TOP)
			// cs.addActiveFace(indices, direction);
			// else
			cs.removeActiveFace(indices, direction);
		} else {

			Material neighbourBlockType = neighbour.getBlockAtChunkPos(nIndices.x, nIndices.y, nIndices.z);
			Material csBlockType = cs.getBlockAtChunkPos(indices.x, indices.y, indices.z);

			if (neighbourBlockType == Material.AIR) {
				if (csBlockType != Material.AIR) {
					cs.addActiveFace(indices, direction);
				}
			} else {
				cs.removeActiveFace(indices, direction);
			}
			if (csBlockType == Material.AIR) {
				if (neighbourBlockType != Material.AIR) {
					neighbour.addActiveFace(nIndices, Direction.inverse(direction));
				}
			} else {
				neighbour.removeActiveFace(nIndices, Direction.inverse(direction));
			}
		}
		// ystem.out.println(Integer.toBinaryString(i));
	}

	public boolean setLocalBorderActivesForDirection(ChunkSection cs, Vector3b indices, int direction) {
		if(cs == null) {
			return false;
		}
		Vector3i vec = Direction.toVector(direction);
		ChunkSection neighbour = getChunkSection(cs.getPosition().x + vec.x, cs.getPosition().y + vec.y,
				cs.getPosition().z + vec.z);

		int nX = cs.getPosition().x * Chunk.SIZE + indices.x + vec.x;
		int nY = cs.getPosition().y * Chunk.SIZE + indices.y + vec.y;
		int nZ = cs.getPosition().z * Chunk.SIZE + indices.z + vec.z;
		Vector3b nIndices = Chunk.getChunkIndexfromBlockPos(nX, nY, nZ);

		if (neighbour == null) {
			cs.removeActiveFace(indices, direction);
		} else {
			Material neighbourBlockType = neighbour.getBlockAtChunkPos(nIndices.x, nIndices.y, nIndices.z);
			Material csBlockType = cs.getBlockAtChunkPos(indices.x, indices.y, indices.z);

			if (neighbourBlockType == Material.AIR) {
				if (csBlockType != Material.AIR) {
					cs.addActiveFace(indices, direction);
					return true;
				}
			} else {
				cs.removeActiveFace(indices, direction);
				return true;
			}
		}
		return false;
	}

	public ChunkSection getChunkSection(int x, int y, int z) {
		if (!chunkExists(x, z)) {
			return null;
		}
		return chunks.get(new Vector2i(x, z)).getSection(y);
	}

	public ChunkSection getChunkSection(Vector3i pos) {
		if (!chunkExists(pos)) {
			return null;
		}
		return chunks.get(new Vector2i(pos.x, pos.z)).getSection(pos.y);
	}

	public ChunkSection getChunkSectionAtBlockPos(int x, int y, int z) {
		if (y < 0 || y > MAX_BUILD_HEIGHT)
			return null;
		Vector3i chunkPos = getChunkLoc(x, y, z);
		return getChunkSection(chunkPos.x, chunkPos.y, chunkPos.z);
	}

	public boolean isChunkSectionEmptyAtBlockPos(int x, int y, int z) {
		ChunkSection cs = getChunkSectionAtBlockPos(x, y, z);
		if (cs == null || cs.getState() == ChunkState.EMPTY) {
			return true;
		}
		return false;
	}

	public boolean isChunkSectionEmpty(int x, int y, int z) {
		ChunkSection cs = getChunkSection(x, y, z);
		if (cs == null || cs.getState() == ChunkState.EMPTY) {
			return true;
		}
		return false;
	}

	public boolean chunkExistsAtBlockPos(int x, int y, int z) {
		Vector3i chunkPos = getChunkLoc(x, y, z);
		return chunkExists(chunkPos.x, chunkPos.z);
	}

	public Material getBlock(int x, int y, int z) {
		if (!chunkExistsAtBlockPos(x, y, z) || y < 0 || y > MAX_BUILD_HEIGHT) {
			return Material.AIR;
		}
		Vector3i chunkPos = getChunkLoc(x, y, z);
		ChunkSection cs = chunks.get(new Vector2i(chunkPos.x, chunkPos.z)).getSection(chunkPos.y);
		if (cs == null)
			return Material.AIR;
		Vector3b chunkIndices = Chunk.getChunkIndexfromBlockPos(x, y, z);
		return cs.getBlockAtChunkPos(chunkIndices.x, chunkIndices.y, chunkIndices.z);

	}

	public Material getBlock(Vector3i pos) {
		return getBlock(pos.x, pos.y, pos.z);
	}

	public static Vector3i getChunkLoc(int x, int y, int z) {
		if (x < 0)
			x -= Chunk.SIZE - 1;
		if (z < 0)
			z -= Chunk.SIZE - 1;

		byte chunkI = (byte) (x / (float) (Chunk.SIZE * 1.0f));
		byte chunkJ = (byte) (y / (float) (Chunk.SIZE * 1.0f));
		byte chunkK = (byte) (z / (float) (Chunk.SIZE * 1.0f));

		return new Vector3i(chunkI, chunkJ, chunkK);
	}

	public Chunk getChunkAtBlockPos(int x, int y, int z) {
		Vector3i loc = getChunkLoc(x, y, z);
		return getChunk(new Vector2i(loc.x, loc.z));
	}

	private boolean chunkExists(int x, int z) {
		return (chunks.containsKey(new Vector2i(x, z)));
	}

	private boolean chunkExists(Vector2i pos) {
		return (chunks.containsKey(pos));
	}

	private boolean chunkExists(Vector3i pos) {
		return (chunks.containsKey(new Vector2i(pos.x, pos.z)));
	}

	public void createChunk(int x, int z) {
		Vector2i pos = new Vector2i(x, z);
		Chunk c = new Chunk(pos, renderer);
		chunks.put(pos, c);
		chunksToPopulate.add(pos);
	}
	
	public void createChunk(Vector2i pos) {
		createChunk(pos.x,pos.y);
	}

	public void unloadChunk(Vector2i pos) {
		Chunk c = chunks.get(pos);
		c.unload();
		chunks.remove(pos);
	}

	public boolean chunkShouldBeLoaded(int x, int z) {
		return (!(x > cX + RENDER_DISTANCE || x < cX - RENDER_DISTANCE || z > cZ + RENDER_DISTANCE
				|| z < cZ - RENDER_DISTANCE));
	}

	public void setBlock(int x, int y, int z, Material type) {
		ChunkSection cs = getChunkSectionAtBlockPos(x, y, z);
		boolean changed = cs.setBlockAtGlobalPosition(x, y, z, type);
		if (changed) {
			updateAjacentChunkBorders(x, y, z);
			chunkSectionsToRegenerate.add(cs.getPosition());
		}
	}

	public void updateAjacentChunkBorders(int x, int y, int z) {
		Vector3b indices = Chunk.getChunkIndexfromBlockPos(x, y, z);
		ChunkSection toUpdate;
		Vector3b updateIndices = new Vector3b();
		if (indices.z == Chunk.SIZE - 1) { // NORTH
			toUpdate = getChunkSectionAtBlockPos(x, y, z + 1);
			updateIndices = Chunk.getChunkIndexfromBlockPos(x, y, z + 1);
			if (setLocalBorderActivesForDirection(toUpdate, updateIndices, Direction.SOUTH)) {
				chunkSectionsToRegenerate.add(toUpdate.getPosition());
				toUpdate.advanceState();
			}

		}
		if (indices.z == 0) { // SOUTH
			toUpdate = getChunkSectionAtBlockPos(x, y, z - 1);
			updateIndices = Chunk.getChunkIndexfromBlockPos(x, y, z - 1);
			if (setLocalBorderActivesForDirection(toUpdate, updateIndices, Direction.NORTH)) {
				chunkSectionsToRegenerate.add(toUpdate.getPosition());
				toUpdate.advanceState();
			}
		}
		if (indices.x == Chunk.SIZE - 1) { // WEST
			toUpdate = getChunkSectionAtBlockPos(x + 1, y, z);
			updateIndices = Chunk.getChunkIndexfromBlockPos(x + 1, y, z);
			if (setLocalBorderActivesForDirection(toUpdate, updateIndices, Direction.EAST)) {
				chunkSectionsToRegenerate.add(toUpdate.getPosition());
				toUpdate.advanceState();
			}
		}
		if (indices.x == 0) { // EAST
			toUpdate = getChunkSectionAtBlockPos(x - 1, y, z);
			updateIndices = Chunk.getChunkIndexfromBlockPos(x - 1, y, z);
			if (setLocalBorderActivesForDirection(toUpdate, updateIndices, Direction.WEST)) {
				chunkSectionsToRegenerate.add(toUpdate.getPosition());
				toUpdate.advanceState();
			}
		}
		if (indices.y == Chunk.SIZE - 1) { // TOP
			toUpdate = getChunkSectionAtBlockPos(x, y + 1, z);
			updateIndices = Chunk.getChunkIndexfromBlockPos(x, y + 1, z);
			if (setLocalBorderActivesForDirection(toUpdate, updateIndices, Direction.BOTTOM)) {
				chunkSectionsToRegenerate.add(toUpdate.getPosition());
				toUpdate.advanceState();
			}
		}
		if (indices.y == 0) { // BOTTOM
			toUpdate = getChunkSectionAtBlockPos(x, y - 1, z);
			updateIndices = Chunk.getChunkIndexfromBlockPos(x, y - 1, z);
			if (setLocalBorderActivesForDirection(toUpdate, updateIndices, Direction.TOP)) {
				chunkSectionsToRegenerate.add(toUpdate.getPosition());
				toUpdate.advanceState();
			}
		}
	}

	public byte getAjacentChunkBorders(int x, int y, int z) {
		byte b = 0;
		Vector3b indices = Chunk.getChunkIndexfromBlockPos(x, y, z);
		if (indices.z == Chunk.SIZE - 1)
			b = Utils.addDirection(b, Direction.NORTH);
		if (indices.z == 0)
			b = Utils.addDirection(b, Direction.SOUTH);
		if (indices.x == 0)
			b = Utils.addDirection(b, Direction.WEST);
		if (indices.x == Chunk.SIZE - 1)
			b = Utils.addDirection(b, Direction.EAST);
		if (indices.y == 0)
			b = Utils.addDirection(b, Direction.BOTTOM);
		if (indices.x == Chunk.SIZE - 1)
			b = Utils.addDirection(b, Direction.TOP);

		return b;
	}

	public void setBlock(Vector3i pos, Material type) {
		setBlock(pos.x, pos.y, pos.z, type);
	}

	public void addVertexCount(int val) {
		vertexCount += val;
	}

	public void addBlockCount(int val) {
		blockCount += val;
	}

	public Chunk getChunk(Vector2i pos) {
		return chunks.get(pos);
	}

}
