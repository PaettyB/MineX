package de.keygalp.mineX.utils;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class Ray {

	public static float INCREMENT = 0.05f;

	public static Vector3f cast3D(Vector3f pos, Vector3f dir, float dist) {
		Vector3f vec = new Vector3f();

		dir.mul(dist * INCREMENT, vec);
		vec.add(pos, vec);

		return vec;
	}

	public static Vector3i blockCast(Vector3f pos, Vector3f dir, float dist) {
		Vector3f vec = new Vector3f();

		dir.mul(dist * INCREMENT, vec);
		vec = vec.add(pos);

		Vector3i ints = new Vector3i((int)Math.floor(vec.x), (int)Math.floor(vec.y), (int)Math.floor(vec.z));
		// Vector3i ints = new Vector3i((int) (vec.x), (int) (vec.y), (int) (vec.z));

		return ints;
	}

}
