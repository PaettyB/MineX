package de.keygalp.mineX.guis.rendering;

import org.joml.Vector2f;
import org.joml.Vector2i;

import de.keygalp.mineX.utils.Utils;

public class GUITexture {

	private int texture;
	private Vector2f positon;
	private Vector2f scale;
	
	private boolean visible = true;
	
	

	public GUITexture(int texture, Vector2f positon, Vector2f scale) {
		this.texture = texture;
		this.positon = positon;
		this.scale = scale;
	}
	
	public GUITexture(int texture, Vector2f positon, Vector2i scale) {
		this.texture = texture;
		this.positon = positon;
		this.scale = Utils.toNormalScale(scale);
	}
	public GUITexture(int texture, Vector2i positon, Vector2i scale) {
		this.texture = texture;
		this.positon = Utils.toNormalPosition(positon);
		this.scale = Utils.toNormalScale(scale);
	}
	
	public void setTexture(int id) {
		texture = id;
	}

	public int getTexture() {
		return texture;
	}

	public Vector2f getPositon() {
		return positon;
	}

	public Vector2f getScale() {
		return scale;
	}
	public boolean isVisibe() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
}
