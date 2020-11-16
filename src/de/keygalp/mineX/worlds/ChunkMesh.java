package de.keygalp.mineX.worlds;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import de.keygalp.mineX.Game;

public class ChunkMesh {

	private int vaoID;
	private int vertexCount;

	private int indVBO;
	private int posVBO;
	private int texVBO;
	private int norVBO;

	public ChunkMesh(int vaoID, int vertexCount, int indVBO, int posVBO, int texVBO, int norVBO) {
		this.vaoID = vaoID;
		this.indVBO = indVBO;
		this.posVBO = posVBO;
		this.texVBO = texVBO;
		this.norVBO = norVBO;
		this.vertexCount = vertexCount;
	}

	public void updateChunkInVao(ByteBuffer positions, FloatBuffer textureCoords, ByteBuffer normals,
			IntBuffer indices) {
		Game.getLoader().updateChunkInVao(vaoID, posVBO, indVBO, texVBO, norVBO, positions, textureCoords, normals,
				indices);
		
	}

	public int getIndVBO() {
		return indVBO;
	}

	public int getPosVBO() {
		return posVBO;
	}

	public int getTexVBO() {
		return texVBO;
	}

	public int getNorVBO() {
		return norVBO;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public void setVertexCount(int count) {
		vertexCount = count;
	}

}
