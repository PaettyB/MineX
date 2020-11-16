package de.keygalp.mineX.guis.rendering;

import de.keygalp.mineX.models.Loader;
import de.keygalp.mineX.models.RawModel;
import de.keygalp.mineX.utils.Maths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class GUIObjectRenderer {

	private RawModel mesh;
	private GUIObjectShader shader;

	private List<GUIObject> guis = new ArrayList<GUIObject>();

	public GUIObjectRenderer(Loader loader) {
		shader = new GUIObjectShader();
		float[] positions = { 0, 1, 0, 0, 1, 1, 1, 0  };
		mesh = loader.loadToVAO(positions);
	}

	public void updateGUIs() {
		for (GUIObject gui : guis) {
			gui.updateMesh();
		}
	}

	public void addObject(GUIObject object) {
		guis.add(object);
	}

	public void render() {
		if (guis.size() < 1)
			return;

		updateGUIs();

		shader.start();

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		for (GUIObject gui : guis) {
			if (!gui.getVisible())
				continue;
			GL30.glBindVertexArray(gui.getMesh().getVaoID());
			GL20.glEnableVertexAttribArray(0);
			// GL13.glActiveTexture(GL13.GL_TEXTURE0);
			// GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			// GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformation(matrix);
			shader.loadColor(gui.getColor());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, mesh.getVertexCount());
		}
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

}
