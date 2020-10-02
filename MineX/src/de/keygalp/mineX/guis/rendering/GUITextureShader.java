package de.keygalp.mineX.guis.rendering;

import org.joml.Matrix4f;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.shaders.ShaderProgram;

public class GUITextureShader extends ShaderProgram {

	private static final String VERTEX_FILE = Game.SRC + "guis/rendering/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = Game.SRC + "guis/rendering/guiFragmentShader.txt";

	private int location_transformationMatrix;

	public GUITextureShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	public void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
