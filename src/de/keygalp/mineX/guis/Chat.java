package de.keygalp.mineX.guis;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.input.KeyboardHandler;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

public class Chat {

	private Game game;
	private final Vector2i position;

	public static boolean visible = false;

	public static float FONT_SIZE = 0.03f;

	private Console console;
	private ChatEntryField chatEntryField;

	public Chat(Game game, Vector2i position) {
		super();
		this.game = game;
		this.position = position;
		init();
	}

	private void init() {

		chatEntryField = new ChatEntryField(game, position, new Vector2i(500, 30));
		console = new Console(game, new Vector2i(0, -40).add(position), new Vector2i(500, 500));
	}

	public void update() {
		if ((KeyboardHandler.keyTyped(GLFW.GLFW_KEY_T) && !visible)
				|| (KeyboardHandler.keyTyped(GLFW.GLFW_KEY_TAB) && visible)) {
			visible = !visible;
			game.setPlayerFocused(!visible);
			console.setVisible(visible);
			chatEntryField.setVisible(visible);
			chatEntryField.reset();
			return;
		}
		if(visible)
			chatEntryField.update();
	}

}
