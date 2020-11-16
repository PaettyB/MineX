package de.keygalp.mineX.guis.rendering;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.keygalp.mineX.models.Loader;
import de.keygalp.mineX.models.RawModel;
import de.keygalp.mineX.utils.Maths;

public class GUITextureRenderer {

	private final RawModel quad;
	private GUITextureShader shader;
	private List<GUITexture> guis = new ArrayList<GUITexture>();

	public GUITextureRenderer(Loader loader) {
		float[] positions = { 0, 1, 0, 0, 1, 1, 1, 0  };
		quad = loader.loadToVAO(positions);
		shader = new GUITextureShader();
	}
	
	public void addTexture(GUITexture texture) {
		guis.add(texture);
	}
	
	public void render(){
		if(guis.size() < 1)
			return;
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		for(GUITexture gui : guis){
			if(!gui.isVisibe())
				continue;
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			//System.out.println(""+ gui.getPositon().y);
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPositon(), gui.getScale());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
		shader.stop();
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
}
