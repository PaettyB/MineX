package de.keygalp.mineX.assets;

import java.io.File;

import de.keygalp.mineX.fontMeshCreator.FontType;
import de.keygalp.mineX.models.Loader;
import de.keygalp.mineX.models.OBJLoader;
import de.keygalp.mineX.models.TexturedModel;
import de.keygalp.mineX.textures.ModelTexture;
import de.keygalp.mineX.textures.TextureType;

public class Assets {
	
	public static TexturedModel DIRT;
	public static ModelTexture DIRT_TEXTURE;
	public static ModelTexture WHITE_TEXTURE;
	public static ModelTexture SPRITE_SHEET;
	public static FontType FONT_ARIAL; 
	public static FontType FONT_MINECRAFTER; 
	public static FontType FONT_BOOK_ANTIQUA; 
	public static FontType FONT_CONSOLAS; 
	public static FontType FONT_ARIAL_SHADOW; 
	
	
	public static void loadAssets(Loader loader) {
		TextureType.loadTextureTypes();
		DIRT_TEXTURE = new ModelTexture(loader.loadTexture("textures/dirt"));
		WHITE_TEXTURE = new ModelTexture(loader.loadTexture("textures/white"));
		SPRITE_SHEET = new ModelTexture(loader.loadTexture("textures/spriteSheet"));
		
		DIRT = new TexturedModel(loader.loadToVao(OBJLoader.loadOBJ("block")), DIRT_TEXTURE);
	
		FONT_ARIAL = new FontType(loader.loadTexture("fonts/arial"), new File("res/fonts/arial.fnt"));
		FONT_MINECRAFTER = new FontType(loader.loadTexture("fonts/minecrafter"), new File("res/fonts/minecrafter.fnt"));
		FONT_BOOK_ANTIQUA = new FontType(loader.loadTexture("fonts/bookAntiqua"), new File("res/fonts/bookAntiqua.fnt"));
		FONT_CONSOLAS = new FontType(loader.loadTexture("fonts/consolas"), new File("res/fonts/consolas.fnt"));
		FONT_ARIAL_SHADOW = new FontType(loader.loadTexture("fonts/arialShadow"), new File("res/fonts/arialShadow.fnt"));
	}
}
