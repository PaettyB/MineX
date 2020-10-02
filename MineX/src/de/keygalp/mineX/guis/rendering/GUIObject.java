package de.keygalp.mineX.guis.rendering;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import de.keygalp.mineX.models.Loader;
import de.keygalp.mineX.models.RawModel;
import de.keygalp.mineX.utils.Utils;

public class GUIObject {

	private Vector2f positon;
	private Vector2f scale;
	private RawModel mesh;

	private Vector4f color;

	private float width = 1, height = 1;

	private Loader loader;
	
	private boolean visible = true;

	// private float[] positions = { -width, height, -width, -height, width, height,
	// width, -height };
	private float[] positions = { 0, height, 0, 0, width, height, width, 0 };

	public GUIObject(Vector2f positon, Vector2f scale, Loader loader) {
		this.positon = positon;
		this.scale = scale;
		this.loader = loader;
		color = new Vector4f(0, 0, 0, 0.5f);
		mesh = loader.loadToVAO(positions);
	}

	public GUIObject(Vector2i positon, Vector2i scale, Loader loader) {
		this.positon = Utils.toNormalPosition(positon);
		this.scale = Utils.toNormalScale(scale);
		this.loader = loader;
		this.color = new Vector4f(0, 0, 0, 0.5f);
		mesh = loader.loadToVAO(positions);
	}

	public void updateMesh() {
		// width = (float)(Math.cos(Game.frameCount % 5 * 0.5));
		// height = (float)(Math.sin(Game.frameCount % 5 * 0.5));
		// positions = new float[] { -width, height, -width, -height, width, height,
		// width, -height };
		// loader.updateVao(mesh.getVaoID(), mesh.getVboID(), positions);
		// mesh.setVertexCount(positions.length / 2);
	}

	public void setVisible(boolean bool) {
		visible = bool;
	}
	
	public boolean getVisible() {
		return visible;
	}

	public Vector2f getPosition() {
		return positon;
	}
	
	public void setPosition(Vector2i pos) {
		positon = Utils.toNormalPosition(pos);
	}
	public void setPosition(Vector2f pos) {
		positon = pos;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setColor(Vector4f c) {
		this.color = c;
	}

	public Vector4f getColor() {
		return color;
	}

	public void setScale(Vector2i scale) {
		this.scale = Utils.toNormalScale(scale);
	}

	public RawModel getMesh() {
		return mesh;
	}

	public void setMesh(RawModel mesh) {
		this.mesh = mesh;
	}
}
