package de.keygalp.mineX.worlds;

import de.keygalp.mineX.Game;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ChunkMesh {

	private int vertexCount;

	private int vbo, ibo;
	private int nOffset, tOffset;

	public ChunkMesh(int vbo, int ibo, int vertexCount, int nOffset, int tOffset) {
		this.ibo = ibo;
		this.vbo = vbo;
		this.nOffset = nOffset;
		this.tOffset = tOffset;
		this.vertexCount = vertexCount;
	}
	
	public void updateChunkInVao(byte[] positions, float[] textureCoords, byte[] normals, int[] indices) {
		//Game.getLoader().updateChunkInVao(vaoID, posVBO, indVBO, texVBO, norVBO, positions, textureCoords, normals,
		//		indices);
		
	}
	
	public int getVbo() {
		return vbo;
	}
	
	public int getIbo() {
		return ibo;
	}
	
	public int getNOffset() {
		return nOffset;
	}
	
	public int getTOffset() {
		return tOffset;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public void setVertexCount(int count) {
		vertexCount = count;
	}

}
