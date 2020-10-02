package de.keygalp.mineX;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import de.keygalp.mineX.assets.Assets;
import de.keygalp.mineX.commands.CommandManager;
import de.keygalp.mineX.entities.Camera;
import de.keygalp.mineX.entities.ItemEntity;
import de.keygalp.mineX.entities.Light;
import de.keygalp.mineX.entities.Player;
import de.keygalp.mineX.events.EventExecutor;
import de.keygalp.mineX.fontRendering.TextMaster;
import de.keygalp.mineX.guis.HUDManager;
import de.keygalp.mineX.guis.rendering.GUIRenderer;
import de.keygalp.mineX.input.CharacterHandler;
import de.keygalp.mineX.input.KeyboardHandler;
import de.keygalp.mineX.input.MouseHandler;
import de.keygalp.mineX.models.Loader;
import de.keygalp.mineX.models.OBJLoader;
import de.keygalp.mineX.models.TexturedModel;
import de.keygalp.mineX.renderEngine.DisplayManager;
import de.keygalp.mineX.renderEngine.MasterRenderer;
import de.keygalp.mineX.textures.ModelTexture;
import de.keygalp.mineX.worlds.World;

public class Game extends DisplayManager {

	private static Loader loader;
	private static MasterRenderer renderer;
	private GUIRenderer guiRenderer;

	public static long frameCount = 0;
	public static float frameTimeMS;
	public static long lastTick;

	private HUDManager hudManager;
	
	private EventExecutor eventExecutor;

	private static World world;
	private Light light;
	private Camera camera;
	private static Player player;

	public static final String VERSION = "1.0";
	public static final String SRC = "src/de/keygalp/mineX/";
	private boolean playerFocused = true;
	
	
	private ItemEntity item;

	public void init() {

		createDisplay();

		loader = new Loader();
		TextMaster.init(loader);
		Assets.loadAssets(loader);

		light = new Light(new Vector3f(-1000000, 1200000, -1500000), new Vector3f(1, 1, 1));
		camera = new Camera(new Vector3f(0, 0, 0), 0f, 0f, 0f);
		renderer = new MasterRenderer();

		world = new World(renderer);
		world.startGeneration();

		CommandManager.registerAll();

		TexturedModel playerModel = new TexturedModel(loader.loadToVao(OBJLoader.loadOBJ("player")),
				new ModelTexture(loader.loadTexture("/textures/playerLayout")));

		player = new Player(this, playerModel, new Vector3f(0.5f, World.SUPERFLAT_HEIGHT+4, 0.5f), 0, 0, 0, 0.1f);

		guiRenderer = new GUIRenderer(loader);

		hudManager = new HUDManager(this, loader, guiRenderer);

		camera.setPitch(180);

		lastTick = System.nanoTime();

	}

	@Override
	public void tick() {
		KeyboardHandler.update();
		MouseHandler.update();
		renderer.tick();
		player.tick();
		player.clampCamera(camera);
		world.update();
		hudManager.update();
		CharacterHandler.clear();
	}

	@Override
	public void render() {
		long now = System.nanoTime();
		frameTimeMS = (now - lastTick) / 1000000;
		frameCount++;
		lastTick = now;
		renderer.render(light, camera);
		if (HUDManager.VISIBLE) {
			guiRenderer.render();

			TextMaster.render();
		}
	}

	public void cleanAll() {
		TextMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
	}

	public static void main(String[] args) {
		new Game().run();
	}

	public void setPlayerFocused(boolean bool) {
		playerFocused = bool;
		if (!bool) {
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
			GLFW.glfwSetCursorPos(window, DisplayManager.WIDTH / 2, DisplayManager.HEIGHT / 2);
		} else {
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
			GLFW.glfwSetCursorPos(window, DisplayManager.WIDTH / 2, DisplayManager.HEIGHT / 2);
		}
	}

	public boolean isPlayerFocused() {
		return playerFocused;
	}

	public static World getWorld() {
		return world;
	}

	public static Loader getLoader() {
		return loader;
	}

	public Camera getCamera() {
		return camera;
	}

	public static Player getPlayer() {
		return player;
	}

	public GUIRenderer getGUIRenderer() {
		return guiRenderer;
	}
	
	public static MasterRenderer getMasterRenderer() {
		return renderer;
	}

}
