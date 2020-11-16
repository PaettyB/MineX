package de.keygalp.mineX.worlds;

import org.joml.Vector3i;

public class Direction {

	// HEADING z -> -1
	public static final int SOUTH = 0;
	// HEADING z -> +1
	public static final int NORTH = 1;
	// HEADING x -> -1
	public static final int EAST = 2;
	// HEADING x -> +1
	public static final int WEST = 3;
	// HEADING y -> +1
	public static final int TOP = 4;
	// HEADING y -> -1
	public static final int BOTTOM = 5;

	public static Vector3i SOUTH_VECTOR = new Vector3i(0, 0, -1);
	public static Vector3i NORTH_VECTOR = new Vector3i(0, 0, 1);
	public static Vector3i EAST_VECTOR = new Vector3i(-1, 0, 0);
	public static Vector3i WEST_VECTOR = new Vector3i(1, 0, 0);
	public static Vector3i TOP_VECTOR = new Vector3i(0, 1, 0);
	public static Vector3i BOTTOM_VECTOR = new Vector3i(0, -1, 0);

	public static Vector3i toVector(int dir) {
		switch (dir) {
		case SOUTH:
			return SOUTH_VECTOR;
		case NORTH:
			return NORTH_VECTOR;
		case WEST:
			return WEST_VECTOR;
		case EAST:
			return EAST_VECTOR;
		case TOP:
			return TOP_VECTOR;
		case BOTTOM:
			return BOTTOM_VECTOR;
		default:
			return SOUTH_VECTOR;
		}
	}

	/*
	 * public static Vector3i toVector(int dir) { if (dir == SOUTH) return new
	 * Vector3i(0, 0, -1); else if (dir == NORTH) return new Vector3i(0, 0, 1); else
	 * if (dir == WEST) return new Vector3i(1, 0, 0); else if (dir == EAST) return
	 * new Vector3i(-1, 0, 0); else if (dir == TOP) return new Vector3i(0, 1, 0);
	 * else if (dir == BOTTOM) return new Vector3i(0, -1, 0); else return new
	 * Vector3i(0, 0, 0); }
	 */

	public static int facingDirection(float angle) {
		if (angle >= 45 && angle <= 135)
			return WEST;
		else if (angle >= 135 || angle <= -135)
			return NORTH;
		else if (angle >= -135 && angle <= -45)
			return EAST;
		else
			return SOUTH;
	}

	public static String getName(int dir) {
		switch (dir) {
		case SOUTH:
			return "SOUTH";
		case NORTH:
			return "NORTH";
		case WEST:
			return "WEST";
		case EAST:
			return "EAST";
		case TOP:
			return "TOP";
		case BOTTOM:
			return "BOTTOM";
		default:
			return "UNKNOWN";

		}
	}

	public static int inverse(int dir) {
		switch (dir) {
		case SOUTH:
			return NORTH;
		case NORTH:
			return SOUTH;
		case WEST:
			return EAST;
		case EAST:
			return WEST;
		case TOP:
			return BOTTOM;
		case BOTTOM:
			return TOP;
		}
		return -1;
	}
}
