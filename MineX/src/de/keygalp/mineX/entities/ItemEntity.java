package de.keygalp.mineX.entities;

import org.joml.Vector3f;

import de.keygalp.mineX.models.TexturedModel;

public class ItemEntity extends Entity{

	public ItemEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
			BoundingBox bounds) {
		super(model, position, rotX, rotY, rotZ, scale, bounds);
	}

}
