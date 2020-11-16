package de.keygalp.mineX.input;

import de.keygalp.mineX.renderEngine.DisplayManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardHandler extends GLFWKeyCallback {

	public static boolean[] keys = new boolean[65536];
	public static boolean[] cooldowns = new boolean[65536];

	public static List<Integer> activeKeys = new ArrayList<Integer>();
	public static List<Integer> typedKeys = new ArrayList<Integer>();

	// public static final boolean COOLDOWN = 10;

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
			GLFW.glfwSetWindowShouldClose(DisplayManager.getWindowId(), true);
		keys[key] = action != GLFW_RELEASE;
		if (action != GLFW_RELEASE) {
			if (!activeKeys.contains(key))
				activeKeys.add(key);
		} else {
			int index = activeKeys.indexOf(key);
			activeKeys.remove(index);
			cooldowns[key] = false;
		}

	}

	public static void update() {
		typedKeys.clear();
		for (int k : activeKeys) {
			if (keyWasPressed(k)) {
				typedKeys.add(k);
			}
		}
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}

	public static boolean keyTyped(int keycode) {
		return (typedKeys.contains(keycode));
	}

	private static boolean keyWasPressed(int keycode) {
		if (!cooldowns[keycode]) {
			cooldowns[keycode] = true;
			return true;
		} else
			return false;
	}
}
