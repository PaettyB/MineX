package de.keygalp.mineX;

import org.joml.Vector3i;

import de.keygalp.mineX.inventory.Material;
import de.keygalp.mineX.worlds.World;

public class Block {
	
	private Vector3i pos;
	private Material material;
	
	
	public Block(Vector3i pos, Material material) {
		super();
		this.pos = pos;
		this.material = material;
	}
	
	public Vector3i getChunkSection() {
		return World.getChunkLoc(pos.x, pos.y, pos.z);
	}
	
	public Vector3i getPos() {
		return pos;
	}
	
	public int getX() {
		return pos.x;
	}
	
	public int getY() {
		return pos.y;
	}
	
	public int getZ() {
		return pos.z;
	}
	
	public void setPos(Vector3i pos) {
		this.pos = pos;
	}
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}

	

}
