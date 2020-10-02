package de.keygalp.mineX.entities;

import org.joml.Vector3f;
import org.joml.Vector3i;

import de.keygalp.mineX.models.TexturedModel;

public class Entity {

	
	protected TexturedModel model;
	protected Vector3f position;
	protected Vector3i blockPosition;
	
	protected float  rotX = 0, rotY = 0, rotZ = 0;
	protected float scale = 1;
	
	protected float pitch = 90, yaw = 0, roll = 0;
	
	protected Vector3f velocity;
	protected Vector3f accelleration;
	
	protected BoundingBox bounds;
	
	
	protected boolean onGround = false;
	
	
	public static float WALKING_FORCE = 0.01f;
	public static float SPRINTING_SPEED = 0.8f;
	public static float SENSITIVITY = 0.05f;
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, BoundingBox bounds) {
		this.model = model;
		this.position = position;
		this.blockPosition = new Vector3i((int) position.x, (int) position.y, (int) position.z);
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		
		this.velocity = new Vector3f();
		this.accelleration = new Vector3f();
		
		this.bounds = bounds;
	}
	
	public void tick() {
		bounds.setPos(new Vector3f(position).add(bounds.getOffset()));
	}	

	
	
	public void increasePosition(float dx, float dy, float dz) {
		position.x += dx; 
		position.y += dy; 
		position.z += dz; 
	}
	
	public void increasePosition(Vector3f dist) {
		position = position.add(dist);
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		rotX += dx; 
		rotY += dy; 
		rotZ += dz; 
	}
	
	
	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	public float getPitch() {
		return pitch;
	}
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	public float getRoll() {
		return roll;
	}
	public void setRoll(float roll) {
		this.roll = roll;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	
}
