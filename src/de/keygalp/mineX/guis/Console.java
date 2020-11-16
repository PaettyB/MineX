package de.keygalp.mineX.guis;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.guis.rendering.GUIObject;
import org.joml.Vector2i;

public class Console {
	
	private GUIObject frame;
	private Vector2i position;
	private Vector2i size;
	
	private static TextContainer textContainer;
	
	private Game game;
	
	
	public Console(Game game, Vector2i position, Vector2i size){
		this.game = game;
		this.position = position;
		this.size = size;
		frame = new GUIObject(position, size, game.getLoader());
		frame.setVisible(Chat.visible);
		game.getGUIRenderer().addObject(frame);
		
		textContainer = new TextContainer(game, position,size);
	}
	
	
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
		textContainer.setVisible(visible);
	}
	
	public static void print(String message) {
		textContainer.print(message);
	}

}
