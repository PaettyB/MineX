package de.keygalp.mineX.fontRendering;

import de.keygalp.mineX.fontMeshCreator.FontType;
import de.keygalp.mineX.fontMeshCreator.GUIText;
import de.keygalp.mineX.fontMeshCreator.TextMeshData;
import de.keygalp.mineX.models.Loader;
import de.keygalp.mineX.models.VertexArrayPointers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextMaster {

	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;

	public static void init(Loader theLoader) {
		renderer = new FontRenderer();
		loader = theLoader;
	}

	public static void render() {
		//renderer.render(texts);
	}

	public static void updateText(GUIText text, int vaoID, int posVBO, int texVBO) {
		/*FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		//if (data.getVertexPositions().length > 0) {
			loader.updateVao(vaoID, posVBO, texVBO, data.getVertexPositions(), data.getTextureCoords());
			text.setMeshInfo(vaoID, posVBO, texVBO, data.getVertexCount());
	//	}*/
		
	}

	public static void loadText(GUIText text) {
		/*FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		VertexArrayPointers pointers = loader.loadToVao(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(pointers.vao, pointers.posVBO, pointers.texVBO, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);*/
	}

	public static void removeText(GUIText text) {
		List<GUIText> batch = texts.get(text.getFont());
		batch.remove(text);
		if (batch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}

	public static void clearTexts() {
		texts.clear();
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}

}
