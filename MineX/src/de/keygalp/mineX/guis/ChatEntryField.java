package de.keygalp.mineX.guis;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.commands.CommandManager;
import de.keygalp.mineX.fontMeshCreator.GUIText;
import de.keygalp.mineX.fontMeshCreator.TextMeshCreator;
import de.keygalp.mineX.guis.rendering.GUIObject;
import de.keygalp.mineX.input.CharacterHandler;
import de.keygalp.mineX.input.KeyboardHandler;

public class ChatEntryField {

	private Game game;
	private Vector2i position, size;

	private GUIObject textField, cursor;

	private GUIText text;

	private boolean someKeyDown = false;
	private int pressedTimeDelay = 500;
	private int pressedSpeed = 20;
	private int pressedTimer = 0;
	private int typedSpeed = 00;
	private boolean pressing = false;

	private long cooldown = 0;

	private String string = "";

	public ChatEntryField(Game game, Vector2i position, Vector2i size) {
		this.game = game;
		this.position = position;
		this.size = size;
		init();
	}

	private void init() {
		textField = new GUIObject(position, size, game.getLoader());
		textField.setVisible(Chat.visible);
		cursor = new GUIObject(new Vector2i(5, -5).add(position), new Vector2i(2, 20), game.getLoader());
		cursor.setVisible(Chat.visible);
		cursor.setColor(new Vector4f(1, 1, 1, .7f));
		game.getGUIRenderer().addObject(cursor);
		game.getGUIRenderer().addObject(textField);

		text = new GUIText("", Chat.FONT_SIZE,
				new Vector2i(5, (int) (-TextMeshCreator.getPixelLineHeight(Chat.FONT_SIZE)*0.5)-(int)(size.y*0.5)).add(position));
		text.setColour(1, 1, 1);
		text.setVisible(false);
		text.update("");
	}

	public void update() {
		someKeyDown = (KeyboardHandler.activeKeys.size() > 0);
		if (someKeyDown) {
			pressedTimer += Game.frameTimeMS;
			if (pressedTimer >= pressedTimeDelay) {
				pressing = true;
			}
			for(char c: CharacterHandler.characters) {
				string += c;
			}
			if (cooldown <= 0) {
				
				
				if (pressing) {
					if (KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_BACKSPACE)) {
						string = string.substring(0, Math.max(string.length() - 1, 0));
					}
					cooldown = pressedSpeed;
				} else {
					if (KeyboardHandler.keyTyped(GLFW.GLFW_KEY_BACKSPACE)) {
						string = string.substring(0, Math.max(string.length() - 1, 0));
					}
					cooldown = typedSpeed;
				}
				text.update(string);
					
			} else {
				cooldown -= Game.frameTimeMS;
			}
		} else {
			pressedTimer = 0;
			cooldown = 0;
			pressing = false;
		}
		
		
		cursor.setPosition(new Vector2f(textField.getPosition().x + text.getLength(), cursor.getPosition().y));

		if (KeyboardHandler.keyTyped(GLFW.GLFW_KEY_ENTER)) {
			enterMessage();
			return;
		}
	}

	private void enterMessage() {
		string = string.trim().replaceAll(" +", " ");
		if (string.length() > 0) {
			if (string.startsWith(".")) {
				int index = string.indexOf(" ");
				if (index < 0) {
					CommandManager.executeCommand(string.substring(1, string.length()), new String[0]);
				} else {
					String command = string.substring(1, index);
					String[] args = string.substring(index + 1, string.length()).split(" ");
					CommandManager.executeCommand(command, args);
				}
			} else {
				Console.print(string);
			}
		}
		string = "";
	}

	public void reset() {
		string = "";
		text.update(string);
	}

	public void setVisible(boolean visible) {
		text.setVisible(visible);
		cursor.setVisible(visible);
		textField.setVisible(visible);
	}

	public Vector2i getPosition() {
		return position;
	}

	public void setPosition(Vector2i position) {
		this.position = position;
	}

	public Vector2i getSize() {
		return size;
	}

	public void setSize(Vector2i size) {
		this.size = size;
	}

}
