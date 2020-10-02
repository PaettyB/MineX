package de.keygalp.mineX.utils;

public class Vector3b {

	public byte x, y, z;

	public Vector3b() {
		x = 0;
		y = 0;
		y = z;
	}
	
	public Vector3b(byte x, byte y, byte z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3b add(Vector3b v) {
		return new Vector3b((byte)(x + v.x), (byte)(y + v.y), (byte)(z+v.z));
	}

}
