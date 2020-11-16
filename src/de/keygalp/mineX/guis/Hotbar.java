package de.keygalp.mineX.guis;

import org.joml.Vector2i;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.assets.Assets;
import de.keygalp.mineX.guis.rendering.GUIRenderer;
import de.keygalp.mineX.guis.rendering.GUITexture;
import de.keygalp.mineX.inventory.ItemStack;
import de.keygalp.mineX.inventory.Material;
import de.keygalp.mineX.models.Loader;

public class Hotbar {
	
	public static int WIDTH = 182 * 2, HEIGHT = 22 * 2;

	private GUITexture background;
	private Vector2i pos;
	private boolean visible = true;
	
	private GUITexture[] textures = new GUITexture[9];
	
	public Hotbar( Vector2i pos, GUIRenderer renderer, Loader loader) {
		this.pos = pos;
		
		
		for(int i= 0; i < 9; i ++) {
			textures[i] = new GUITexture(Assets.SPRITE_SHEET.getId(), new Vector2i(pos.x + 3 + i * 20, pos.y - 3), new Vector2i(16,16));
			textures[i].setVisible(false);
			renderer.addTexture(textures[i]);
		}
		background = new GUITexture(loader.loadTexture("/textures/gui/hotbar"), pos, new Vector2i(WIDTH,HEIGHT));
		renderer.addTexture(background);
	}
	
	public void update() {
		for(int i= 0; i < 9; i++) {
			ItemStack item = Game.getPlayer().getInventory().getItem(i);	
			if(item == null) {
				textures[i].setVisible(false);
				continue;
			}
			if(item.getType() == Material.GRASS) {
				textures[i].setTexture(Assets.DIRT_TEXTURE.getId());
				textures[i].setVisible(true);
			}
		}
	}
	
	public void setVisible(boolean b) {
		background.setVisible(b);
	}
	
	public boolean isVisible() {
		return visible;
	}

	public Vector2i getPos() {
		return pos;
	}

	public void setPos(Vector2i pos) {
		this.pos = pos;
	}
	

}
