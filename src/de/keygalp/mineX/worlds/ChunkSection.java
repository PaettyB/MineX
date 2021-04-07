package de.keygalp.mineX.worlds;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.assets.SpriteSheet;
import de.keygalp.mineX.inventory.Material;
import de.keygalp.mineX.textures.TextureType;
import de.keygalp.mineX.utils.Vector3b;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ChunkSection {
    
    public static final int SIZE = 16;
    
    public static final int volume = SIZE * SIZE * SIZE;
    public static final int MAX_VERTICES = volume * 6 * 4;
    
    private ChunkState state = ChunkState.CREATED;
    
    private Vector3i position;
    
    private byte[][][] actives;
    private int[][][] blocks;
    
    private ChunkMesh mesh;
    
    private int blockCount = 0;
    
    // int vertexCountTotal;
    int vertexPointer = 0;
    int newVertices = 0;
    int newFaces = 0;
    
    private float[] vertices;
    private int[] indices;
    private float[] textureCoords;
    private float[] normals;
    
    IntBuffer indicesBuffer;
    FloatBuffer positionsBuffer;
    FloatBuffer texturesBuffer;
    FloatBuffer normalsBuffer;
    
    
    public ChunkSection(int x, int y, int z) {
        position = new Vector3i(x, y, z);
    }
    
    public void populate(byte[][] heights) {
        blocks = new int[SIZE][SIZE][SIZE];
        actives = new byte[SIZE][SIZE][SIZE];
        for (byte i = 0; i < SIZE; i++) {
            for (byte j = 0; j < SIZE; j++) {
                for (byte k = 0; k < SIZE; k++) {
                    
                    if (!World.SUPERFLAT) {
                        
                        if (position.y * SIZE + j <= World.MAX_BUILD_HEIGHT) {
                            if (position.y * SIZE + j == (0xff & heights[i][k])) {
                                
                                blocks[i][j][k] = Material.GRASS.getId();
                                blockCount++;
                            } else if (position.y * SIZE + j == (0xff & heights[i][k] - 1)) {
                                blocks[i][j][k] = Material.DIRT.getId();
                                blockCount++;
                            } else if (position.y * SIZE + j < (0xff & heights[i][k] - 1)) {
                                blocks[i][j][k] = Material.STONE.getId();
                                blockCount++;
                            }
                        }
                    } else {
                        //if (position.y * SIZE + j <= World.SUPERFLAT_HEIGHT) {
                        //    blocks[i][j][k] = Material.STONE.getId();
                        //    blockCount++;
                        //}
                        blocks[0][0][0] = Material.STONE.getId();
                        blockCount = 1;
                    }
                }
            }
        }
        if (blockCount == 0) {
            state = ChunkState.EMPTY;
        } else {
            state = ChunkState.POPULATED;
        }
    }
    
    public void load() {
        if (state != ChunkState.GENERATED && state != ChunkState.REGENERATED)
            return;
        
        mesh = Game.getLoader().loadChunkToVao(positionsBuffer, texturesBuffer, normalsBuffer, indicesBuffer);
        state = ChunkState.LOADED;
    }
    
    public void reload() {
        mesh.setIndexCount(indicesBuffer.capacity());
        //TODO: ENABLE CHUNK RELOADING
        //mesh.updateChunkInVao(positionsBuffer, texturesBuffer, normalsBuffer, indicesBuffer);
        state = ChunkState.LOADED;
        
    }
    
    public void unload() {
        if (state != ChunkState.LOADED)
            return;
        //GL15.glDeleteBuffers(mesh.getVbo());
        //GL15.glDeleteBuffers(mesh.getIbo());
        Game.getWorld().addVertexCount(-newVertices);
        Game.getWorld().addBlockCount(-blockCount);
        //System.out.println("Unloaded");
        state = ChunkState.GENERATED;
    }
    
    public void calculateActives() {
        for (byte i = 0; i < SIZE; i++) {
            for (byte j = 0; j < SIZE; j++) {
                for (byte k = 0; k < SIZE; k++) {
                    
                    if (isEverySecond(i, j, k) || isOnBorder(i, j, k)) {
                        Game.getWorld().setActives(position.x * SIZE + i, position.y * SIZE + j, position.z * SIZE + k);
                    }
                }
            }
        }
    }
    
    public void regenerateMesh(float[] vertices, int[] indices, float[] textureCoords, float[] normals) {
        if (state != ChunkState.UPDATED)
            return;
        newVertices = 0;
        newFaces = 0;
        this.vertices = vertices;
        this.indices = indices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        
        for (byte i = 0; i < SIZE; i++) {
            for (byte j = 0; j < SIZE; j++) {
                for (byte k = 0; k < SIZE; k++) {
                    Material type = Material.getMaterial(blocks[i][j][k]);
                    if (type == Material.AIR)
                        continue;
                    addBlockToMesh(i, j, k, type);
                }
            }
        }
        
        if (newVertices < 1) {
            return;
        }
        state = ChunkState.REGENERATED;
    
        positionsBuffer = Game.getLoader().storeDataInFloatBuffer(vertices, newVertices * 3);
        indicesBuffer = Game.getLoader().storeDataInIntBuffer(indices, blockCount * 6 * 6);
        texturesBuffer = Game.getLoader().storeDataInFloatBuffer(textureCoords, newVertices * 2);
        normalsBuffer = Game.getLoader().storeDataInFloatBuffer(normals, newVertices * 3);
        
    }
    
    public void generate(float[] vertices, int[] indices, float[] textureCoords, float[] normals) {
        if (state != ChunkState.POPULATED)
            return;
        
        newVertices = 0;
        newFaces = 0;
    
        this.vertices = vertices;
        this.indices = indices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        
        for (byte i = 0; i < SIZE; i++) {
            for (byte j = 0; j < SIZE; j++) {
                for (byte k = 0; k < SIZE; k++) {
                    Material type = Material.getMaterial(blocks[i][j][k]);
                    if (type == Material.AIR)
                        continue;
                    addBlockToMesh(i, j, k, type);
                }
            }
        }
        
        Game.getWorld().addVertexCount(newVertices);
        Game.getWorld().addBlockCount(blockCount);
        if (newVertices < 1) {
            /*
             * vertices = null; indices = null; textureCoords = null; normals = null;
             */
            return;
        }
    
        positionsBuffer = Game.getLoader().storeDataInFloatBuffer(vertices, newVertices * 3);
        indicesBuffer = Game.getLoader().storeDataInIntBuffer(indices, blockCount * 6 * 6);
        texturesBuffer = Game.getLoader().storeDataInFloatBuffer(textureCoords, newVertices * 2);
        normalsBuffer = Game.getLoader().storeDataInFloatBuffer(normals, newVertices * 3);
        state = ChunkState.GENERATED;
    
        vertices = null;
        indices = null;
        textureCoords = null;
        normals = null;
        
        System.out.println("new Faces: " + newFaces);
    }
    
    public void updateChunkBorderActives(int direction) {
        byte xStart = 0, yStart = 0, zStart = 0;
        byte xEnd = 0, yEnd = 0, zEnd = 0;
        if (direction == Direction.SOUTH) {
            xStart = 0;
            yStart = 0;
            zStart = 0;
            
            xEnd = Chunk.SIZE - 1;
            yEnd = Chunk.SIZE - 1;
            zEnd = 0;
        } else if (direction == Direction.NORTH) {
            xStart = 0;
            yStart = 0;
            zStart = Chunk.SIZE - 1;
            
            xEnd = Chunk.SIZE - 1;
            yEnd = Chunk.SIZE - 1;
            zEnd = Chunk.SIZE - 1;
        } else if (direction == Direction.EAST) {
            xStart = 0;
            yStart = 0;
            zStart = 0;
            
            xEnd = 0;
            yEnd = Chunk.SIZE - 1;
            zEnd = Chunk.SIZE - 1;
        } else if (direction == Direction.WEST) {
            xStart = Chunk.SIZE - 1;
            yStart = 0;
            zStart = 0;
            
            xEnd = Chunk.SIZE - 1;
            yEnd = Chunk.SIZE - 1;
            zEnd = Chunk.SIZE - 1;
        } else if (direction == Direction.TOP) {
            xStart = 0;
            yStart = Chunk.SIZE - 1;
            zStart = 0;
            
            xEnd = Chunk.SIZE - 1;
            yEnd = Chunk.SIZE - 1;
            zEnd = Chunk.SIZE - 1;
        } else if (direction == Direction.BOTTOM) {
            xStart = 0;
            yStart = 0;
            zStart = 0;
            
            xEnd = Chunk.SIZE - 1;
            yEnd = 0;
            zEnd = Chunk.SIZE - 1;
        }
        
        for (byte x = xStart; x <= xEnd; x++) {
            for (byte y = yStart; y <= yEnd; y++) {
                for (byte z = zStart; z <= zEnd; z++) {
                    if (isEverySecond(x, y, z)) {
                        Game.getWorld().setLocalBorderActivesForDirection(this, new Vector3b(x, y, z), direction);
                    }
                }
            }
        }
        if (state == ChunkState.LOADED || state == ChunkState.UPDATED) {
            state = ChunkState.UPDATED;
        } else if (blockCount <= 0) {
            state = ChunkState.EMPTY;
        } else {
            state = ChunkState.POPULATED;
        }
    }
    
    private void addBlockToMesh(int i, int j, int k, Material type) {
        // Console.print("" + actives[i][j][k]);
        // System.out.println(Integer.toBinaryString(actives[i][j][k]));
        if (isFaceActive(i, j, k, Direction.SOUTH))
            addFaceToMesh(i, j, k, type, Direction.SOUTH);
        if (isFaceActive(i, j, k, Direction.NORTH))
            addFaceToMesh(i, j, k, type, Direction.NORTH);
        if (isFaceActive(i, j, k, Direction.WEST))
            addFaceToMesh(i, j, k, type, Direction.WEST);
        if (isFaceActive(i, j, k, Direction.EAST))
            addFaceToMesh(i, j, k, type, Direction.EAST);
        if (isFaceActive(i, j, k, Direction.TOP))
            addFaceToMesh(i, j, k, type, Direction.TOP);
        if (isFaceActive(i, j, k, Direction.BOTTOM))
            addFaceToMesh(i, j, k, type, Direction.BOTTOM);
    }
    
    private void addFaceToMesh(int x, int y, int z, Material type, int direction) {
        byte[] faceVertices = ChunkBlock.getAllFaces(x, y, z)[direction];
        for (int faceIndex = 0; faceIndex < faceVertices.length; faceIndex += 3) {
            vertices[newVertices * 3 + 0] = faceVertices[faceIndex + 0]; // X
            vertices[newVertices * 3 + 1] = faceVertices[faceIndex + 1]; // Y
            vertices[newVertices * 3 + 2] = faceVertices[faceIndex + 2]; // Z
            
            newVertices++;
            vertexPointer++;
        }
        System.arraycopy(ChunkBlock.getIndices(newFaces * 4), 0, indices, newFaces * 6, 6);
        
        int tex = TextureType.getTextureType(type.getId(), direction);
        System.arraycopy(SpriteSheet.getTextureCoords(tex), 0, textureCoords, newFaces * 8, 8);
        for (int i = 0; i <= 9; i += 3) {
            normals[newFaces * 12 + i + 0] = ChunkBlock.getNormals()[direction][0];
            normals[newFaces * 12 + i + 1] = ChunkBlock.getNormals()[direction][1];
            normals[newFaces * 12 + i + 2] = ChunkBlock.getNormals()[direction][2];
        }
        newFaces++;
    }
    
    public boolean isEverySecond(int i, int j, int k) {
        int jMod = j % 2;
        if (((i + jMod) % 2 == 0 && k % 2 == 0) || ((i + jMod) % 2 == 1 && k % 2 == 1)) {
            return true;
        }
        return false;
    }
    
    public boolean isOnBorder(int i, int j, int k) {
        return (i == 0 || i == Chunk.SIZE - 1 || k == 0 || k == Chunk.SIZE - 1);
    }
    
    public byte getActiveFaces(int i, int j, int k) {
        return actives[i][j][k];
    }
    
    public boolean isFaceActive(int i, int j, int k, int dir) {
        byte b = (byte) Math.pow(2, dir);
        return ((actives[i][j][k] & b) == b);
    }
    
    public void addActiveFace(Vector3b indices, int dir) {
        actives[indices.x][indices.y][indices.z] = (byte) (actives[indices.x][indices.y][indices.z]
                | (byte) Math.pow(2, dir));
    }
    
    public void removeActiveFace(Vector3b indices, int dir) {
        if (isFaceActive(indices.x, indices.y, indices.z, dir)) {
            actives[indices.x][indices.y][indices.z] = (byte) (actives[indices.x][indices.y][indices.z]
                    ^ (byte) Math.pow(2, dir));
        }
    }
    
    public boolean setBlockAtGlobalPosition(int x, int y, int z, Material type) {
        Vector3b index = Chunk.getChunkIndexfromBlockPos(x, y, z);
        Material oldBlock = Material.getMaterial(blocks[index.x][index.y][index.z]);
        
        if (oldBlock == type) {
            return false;
        }
        
        if (oldBlock == Material.AIR && type != Material.AIR)
            blockCount++;
        else if (oldBlock != Material.AIR && type == Material.AIR)
            blockCount--;
        
        blocks[index.x][index.y][index.z] = type.getId();
        Game.getWorld().setActives(x, y, z);
        
        if (state == ChunkState.EMPTY) {
            state = ChunkState.POPULATED;
            return true;
        }
        
        
        //addBlockToMesh(index.x, index.y, index.z, type);
        
        state = ChunkState.UPDATED;
        return true;
    }
    
    public Material getBlockAtChunkPos(int i, int j, int k) {
        return Material.getMaterial(blocks[i][j][k]);
    }
    
    public Vector3i getPosition() {
        return position;
    }
    
    public void advanceState() {
        if (state == ChunkState.POPULATED)
            state = ChunkState.POPULATED;
        else if (state == ChunkState.LOADED)
            state = ChunkState.UPDATED;
        else if (state == ChunkState.GENERATED)
            state = ChunkState.POPULATED;
        else if (state == ChunkState.EMPTY) {
            state = (blockCount < 1) ? ChunkState.EMPTY : ChunkState.POPULATED;
        }
    }
    
    public ChunkState getState() {
        return state;
    }
    
    public void setState(ChunkState state) {
        this.state = state;
    }
    
    public ChunkMesh getMesh() {
        return mesh;
    }
    
    public int getBlockCount() {
        return blockCount;
    }
    
}
