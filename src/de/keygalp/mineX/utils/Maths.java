package de.keygalp.mineX.utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import de.keygalp.mineX.entities.Camera;

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector3f position, float rx, float ry, float rz, float scale) {

		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(position);
		matrix.rotate((float) Math.toRadians(rx), 1.0f, 0.0f, 0.0f);
		matrix.rotate((float) Math.toRadians(ry), 0.0f, 1.0f, 0.0f);
		matrix.rotate((float) Math.toRadians(rz), 0.0f, 0.0f, 1.0f);
		matrix.scale(scale);

		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(translation.x,translation.y,0);
		matrix.scale(scale.x,scale.y,1);
		return matrix;
	}

	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
		viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		viewMatrix.translate(negativeCameraPos);
		return viewMatrix;
	}
		
	public static float randomF(float min, float max) {
		return (float) (min + Math.random() * (max - min));
	}
	
}
