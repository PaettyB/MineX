package de.keygalp.mineX.entities;

import org.joml.Vector3f;

public class Camera {

	private Vector3f position;
	private float pitch;
	private float yaw;
	private float roll;


	public Camera(Vector3f pos, float pitch, float yaw, float roll) {
		this.position = pos;
		this.pitch = pitch;
		this.yaw = yaw;
		this.roll = roll;
	}

	

	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f pos) {
		position = pos;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float val) {
		yaw = val;
	}

	public void setPitch(float val) {
		pitch = val;
	}
	
	public void setRoll(float val) {
		this.roll = val;
	}

	public float getRoll() {
		return roll;
	}

}
