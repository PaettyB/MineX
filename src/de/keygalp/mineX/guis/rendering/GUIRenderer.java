package de.keygalp.mineX.guis.rendering;

import de.keygalp.mineX.models.Loader;

public class GUIRenderer {

	private GUITextureRenderer textureRenderer;
	private GUIObjectRenderer objectRenderer;

	public GUIRenderer(Loader loader) {
		textureRenderer = new GUITextureRenderer(loader);
		objectRenderer = new GUIObjectRenderer(loader);

	}

	public void addTexture(GUITexture texture) {
		textureRenderer.addTexture(texture);
	}

	public void addObject(GUIObject object) {
		objectRenderer.addObject(object);
	}

	public void render() {
		textureRenderer.render();
		objectRenderer.render();
	}

	public void cleanUp() {
		textureRenderer.cleanUp();
		objectRenderer.cleanUp();
	}
}
