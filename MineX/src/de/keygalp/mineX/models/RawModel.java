package de.keygalp.mineX.models;

public class RawModel {

	
	private int vaoID;
	private int vboID = 0;
	private int vertexCount;
	
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;		
	}
	
	public RawModel(int vaoID,int vboID, int vertexCount) {
		this.vaoID = vaoID;
		this.vboID = vboID;
		this.vertexCount = vertexCount;		
	}

	public int getVaoID() {
		return vaoID;
	}
	
	public int getVboID() {
		return vboID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public void setVertexCount(int count) {
		vertexCount = count;
	}
}
