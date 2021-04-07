package de.keygalp.mineX.worlds;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ChunkMesh {

	private int indexCount;

	private int vbo, ibo;
	private int nOffset, tOffset;

	public ChunkMesh(int vbo, int ibo, int indexCount, int nOffset, int tOffset) {
		this.ibo = ibo;
		this.vbo = vbo;
		this.nOffset = nOffset;
		this.tOffset = tOffset;
		this.indexCount = indexCount;
	}
	
	
	
	public void updateChunkInVao(FloatBuffer positions, FloatBuffer textureCoords, FloatBuffer normals,
								 IntBuffer indices) {
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
	
	public int getIndexCount() {
		return indexCount;
	}
	
	public void setIndexCount(int count) {
		indexCount = count;
	}

}
