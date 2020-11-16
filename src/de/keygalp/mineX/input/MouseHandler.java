package de.keygalp.mineX.input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import de.keygalp.mineX.renderEngine.DisplayManager;

public class MouseHandler extends GLFWMouseButtonCallback {
	
	public static boolean[] buttons = new boolean[10];
	public static boolean[] cooldowns = new boolean[10];
	
	public static List<Integer> activeButtons = new ArrayList<Integer>();
	public static List<Integer> typedButtons = new ArrayList<Integer>();
	
	@Override
	public void invoke(long window, int button, int action, int mods) {
		buttons[button] = action == GLFW.GLFW_PRESS;
		if (action != GLFW_RELEASE) {
			if (!activeButtons.contains(button))
				activeButtons.add(button);
		} else {
			int index = activeButtons.indexOf(button);
			activeButtons.remove(index);
			cooldowns[button] = false;
		}
	}
	
	public static void update() {
		typedButtons.clear();
		for (int k : activeButtons) {
			if (buttonWasPressed(k)) {
				typedButtons.add(k);
			}
		}
	}
	
	
	public static boolean isButtonDown(int code) {
		return buttons[code];
	}

	public static boolean buttonTyped(int code) {
		return (typedButtons.contains(code));
	}

	private static boolean buttonWasPressed(int code) {
		if (!cooldowns[code]) {
			cooldowns[code] = true;
			return true;
		} else
			return false;
	}
	
	public static Vector2f getMovement() {
		DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

		GLFW.glfwGetCursorPos(DisplayManager.getWindowId(), x, y);
		x.rewind();
		y.rewind();

		float newX = (float) x.get();
		float newY = (float) y.get();

		float deltaX = (newX - DisplayManager.WIDTH / 2);
		float deltaY = (newY - DisplayManager.HEIGHT / 2);

		GLFW.glfwSetCursorPos(DisplayManager.getWindowId(), DisplayManager.WIDTH / 2, DisplayManager.HEIGHT / 2);

		return new Vector2f(deltaX, deltaY);
	}

	

	

}
