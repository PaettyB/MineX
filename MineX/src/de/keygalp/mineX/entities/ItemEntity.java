package de.keygalp.mineX.entities;

import org.joml.Vector3f;

import de.keygalp.mineX.models.TexturedModel;

public class ItemEntity extends Entity{

	static BoundingBox box = new BoundingBox(new Vector3f(1,1,1), new Vector3f(1,1,1), new Vector3f(1,1,1));
	
	public ItemEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale, box);
	}

}
