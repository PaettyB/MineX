package de.keygalp.mineX.renderEngine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.input.CharacterHandler;
import de.keygalp.mineX.input.KeyboardHandler;
import de.keygalp.mineX.input.MouseHandler;

public abstract class DisplayManager {

	protected static long window;

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int UPS = 60;
	public static final float TimePerTick = (1000000000 / 60.0f);

	private static final String TITLE = "MineX v." + Game.VERSION;

	
	private static GLFWKeyCallback keyCallback;
	private static GLFWCharCallback characterCallback;
	private static GLFWMouseButtonCallback mouseButtonCallback;

	public abstract void init();

	public abstract void tick();

	public abstract void render();

	public abstract void cleanAll();

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		init();
		updateDisplay();

		cleanAll();
		closeDisplay();

	}

	public static void createDisplay() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden
													// after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be
													// resizable

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		/*glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true); // We will detect this
														// in the rendering loop
		});*/
		GLFW.glfwSetKeyCallback(window, keyCallback = new KeyboardHandler());
		GLFW.glfwSetCharCallback(window, characterCallback = new CharacterHandler());
		GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback = new MouseHandler());

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);

		GL.createCapabilities();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

	}

	public void updateDisplay() {

		int frames = 0;
		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();
		double secsPerTick = 1 / (float)UPS;
		int tickCount = 0;
		boolean ticked = false;
		long measure = 0;

		while (!glfwWindowShouldClose(window)) {
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			unprocessedSeconds += passedTime / 1000000000.0;
			measure += passedTime;
			while (unprocessedSeconds > secsPerTick) {
				// TICK AND RENDER HERE
				tick();
				unprocessedSeconds -= secsPerTick;
				ticked = true;
				tickCount++;
				if (measure > 1000000000) {
					GLFW.glfwSetWindowTitle(window, TITLE + " | FPS: " + frames+ " | UPS: " + tickCount);
					frames = 0;
					tickCount = 0;
					measure = 0;
				}
			}

			if (ticked) {
				render();
				frames++;
			}
			/////////////////

			glfwSwapBuffers(window); // swap the color buffers

			glfwPollEvents();
		}

	}

	public void closeDisplay() {

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	public static long getWindowId() {
		return window;
	}
}
