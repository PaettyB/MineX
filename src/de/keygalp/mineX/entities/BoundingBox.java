package de.keygalp.mineX.entities;

import org.joml.Vector3f;

public class BoundingBox {

	private Vector3f offset;
	private Vector3f size;
	private Vector3f pos;

	public BoundingBox(Vector3f pos, Vector3f offset, Vector3f size) {
		super();
		this.offset = offset;
		this.size = size;
		this.pos = pos;
	}

	public boolean collide(BoundingBox other) {
		return (this.getMinX() <= other.getMaxX() && this.getMaxX() >= other.getMinX())
				&& (this.getMinY() <= other.getMaxY() && this.getMaxY() >= other.getMinY())
				&& (this.getMinZ() <= other.getMaxZ() && this.getMaxZ() >= other.getMinZ());
	}
	
	public boolean collideX(BoundingBox other) {
		return (this.getMinX() <= other.getMaxX() && this.getMaxX() >= other.getMinX());
	}
	
	public boolean collideY(BoundingBox other) {
		return (this.getMinY() <= other.getMaxY() && this.getMaxY() >= other.getMinY());
	}
	public boolean collideZ(BoundingBox other) {
		return (this.getMinZ() <= other.getMaxZ() && this.getMaxZ() >= other.getMinZ());
	}
	

	public Vector3f getOffset() {
		return offset;
	}

	public void setOffset(Vector3f offset) {
		this.offset = offset;
	}

	public Vector3f getSize() {
		return size;
	}

	public void setSize(Vector3f size) {
		this.size = size;
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public float getMinX() {
		return Math.min(pos.x + offset.x(), pos.x() + offset.x() + size.x());
	}

	public float getMaxX() {
		return Math.max(pos.x + offset.x(), pos.x() + offset.x() + size.x());
	}

	public float getMinY() {
		return Math.min(pos.y + offset.y(), pos.y() + offset.y() + size.y());
	}

	public float getMaxY() {
		return Math.max(pos.y + offset.y(), pos.y() + offset.y() + size.y());
	}

	public float getMinZ() {
		return Math.min(pos.z + offset.z(), pos.z() + offset.z() + size.z());
	}

	public float getMaxZ() {
		return Math.max(pos.z + offset.z(), pos.z() + offset.z() + size.z());
	}
}