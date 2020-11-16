package de.keygalp.mineX.guis.rendering;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.shaders.ShaderProgram;

public class GUIObjectShader extends ShaderProgram{

	
	private static final String VERTEX_FILE = Game.SRC + "guis/rendering/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = Game.SRC + "guis/rendering/guiObjectFragmentShader.txt";

	private int location_transformationMatrix;
	private int location_color;

	public GUIObjectShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadColor(Vector4f color) {
		super.load4f(location_color, color);
	}

	@Override
	public void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_color = super.getUniformLocation("col");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
