package de.keygalp.mineX.models;

public class VertexArrayPointers {

	public int vao;
	public int posVBO;
	public int texVBO;

	public VertexArrayPointers(int vao, int posVBO, int texVBO) {
		this.vao = vao;
		this.posVBO = posVBO;
		this.texVBO = texVBO;
	}
	
	public VertexArrayPointers() {
		
	}
}
