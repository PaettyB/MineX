package de.keygalp.mineX.guis;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.guis.rendering.GUIRenderer;
import de.keygalp.mineX.guis.rendering.GUITexture;
import de.keygalp.mineX.input.KeyboardHandler;
import de.keygalp.mineX.models.Loader;
import de.keygalp.mineX.renderEngine.DisplayManager;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

public class HUDManager {

	private Game game;
	private GUIRenderer renderer;

	public static boolean VISIBLE = true;

	private FrameTimeGraph frameTimeGraph;
	private Chat chat;
	private GUITexture crosshair;

	private Hotbar hotbar;

	private static boolean crosshairVisible = true;

	public HUDManager(Game game, Loader loader, GUIRenderer renderer) {
		super();
		this.game = game;
		this.renderer = renderer;

		crosshair = new GUITexture(loader.loadTexture("/textures/crosshair"), new Vector2i((int)(DisplayManager.WIDTH * 0.5f - 16),
				(int)(DisplayManager.HEIGHT * 0.5f +16)), new Vector2i(32, 32));
		renderer.addTexture(crosshair);
		frameTimeGraph = new FrameTimeGraph(new Vector2i(DisplayManager.WIDTH - 130, DisplayManager.HEIGHT - 10),
				renderer, loader);
		chat = new Chat(game, new Vector2i(10, DisplayManager.HEIGHT - 10));
		hotbar = new Hotbar(new Vector2i((int) (DisplayManager.WIDTH * 0.5f - Hotbar.WIDTH * 0.5f),
				DisplayManager.HEIGHT), renderer, loader);
	}

	public void update() {
		if (KeyboardHandler.keyTyped(GLFW.GLFW_KEY_F3))
			VISIBLE = !VISIBLE;

		if (!VISIBLE)
			return;

		///crosshair.setVisible(crosshairVisible);

		frameTimeGraph.update();
		chat.update();
		hotbar.update();
	}

	public static void setCrosshairVisible(boolean b) {
		crosshairVisible = b;
	}
}
