package de.keygalp.mineX.guis;

import java.util.ArrayList;

import org.joml.Vector2i;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.assets.Assets;
import de.keygalp.mineX.fontMeshCreator.GUIText;
import de.keygalp.mineX.fontMeshCreator.TextMeshCreator;

public class TextContainer {

	private Game game;
	private Vector2i position, size;

	private int numberOfLines = 0;
	private int margin = 5;
	private int maxNumberofLines = 17;

	private ArrayList<GUIText> texts = new ArrayList<GUIText>();

	public TextContainer(Game game, Vector2i position, Vector2i size) {
		this.game = game;
		this.position = position;
		this.size = size;
	}

	public void print(String string) {
		GUIText text = new GUIText(string, Chat.FONT_SIZE, Assets.FONT_ARIAL,
				new Vector2i(margin,
						(int) (-size.y + margin + numberOfLines * TextMeshCreator.getPixelLineHeight(Chat.FONT_SIZE)))
								.add(position),
				size.x - margin * 2, false);
		text.setColour(1, 1, 1);
		text.setVisible(Chat.visible);
		texts.add(text);
		numberOfLines += text.getNumberOfLines();
		while (numberOfLines - maxNumberofLines > 0) {
			GUIText removed = texts.remove(0);
			numberOfLines -= removed.getNumberOfLines();
			removed.remove();
			int nextLine = 0;
			for (int i = 0; i < texts.size();i++) {
				
				texts.get(i)
						.setPosition(new Vector2i(margin,
								(int) (-size.y + margin + nextLine* TextMeshCreator.getPixelLineHeight(Chat.FONT_SIZE)))
										.add(position));
				nextLine += texts.get(i).getNumberOfLines();
			}
		}
	}

	public void setVisible(boolean visible) {
		for (GUIText text : texts) {
			text.setVisible(visible);
		}
	}
}
