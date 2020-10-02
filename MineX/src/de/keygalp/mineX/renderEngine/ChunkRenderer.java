package de.keygalp.mineX.renderEngine;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import de.keygalp.mineX.assets.Assets;
import de.keygalp.mineX.shaders.StaticShader;
import de.keygalp.mineX.utils.Maths;
import de.keygalp.mineX.worlds.ChunkMesh;
import de.keygalp.mineX.worlds.ChunkSection;
import de.keygalp.mineX.worlds.ChunkState;

public class ChunkRenderer {

	private StaticShader shader;

	public ChunkRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(List<ChunkSection> chunks) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Assets.SPRITE_SHEET.getId());
		
		for (ChunkSection chunk : chunks) {
			if (chunk.getState() == ChunkState.LOADED || chunk.getState() == ChunkState.UPDATED || chunk.getState() == ChunkState.REGENERATED) {
				if(chunk.getMesh() == null) {
					System.err.println("MESH IS NULL");
					return;
				}
				prepareModel(chunk.getMesh());
				prepareChunk(chunk);
				GL11.glDrawElements(GL11.GL_TRIANGLES, chunk.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				unbindModel();
			}
			//System.out.println(chunks.size());
			if (MasterRenderer.CHUNK_BORDERS) {
				prepareChunk(chunk);
				GL11.glBegin(GL11.GL_LINES);
				GL11.glVertex3d(0, 0, 0);
				GL11.glVertex3d(0, 16, 0);

				GL11.glVertex3d(16, 0, 16);
				GL11.glVertex3d(16, 16, 16);

				GL11.glVertex3d(0, 0, 16);
				GL11.glVertex3d(0, 16, 16);

				GL11.glVertex3d(16, 0, 0);
				GL11.glVertex3d(16, 16, 0);

				GL11.glVertex3d(0, 16, 0);
				GL11.glVertex3d(0, 16, 16);

				GL11.glVertex3d(0, 16, 0);
				GL11.glVertex3d(16, 16, 0);

				GL11.glVertex3d(16, 16, 0);
				GL11.glVertex3d(16, 16, 16);

				GL11.glVertex3d(0, 16, 16);
				GL11.glVertex3d(16, 16, 16);


				
				GL11.glVertex3d(0, 0, 0);
				GL11.glVertex3d(0, 0, 16);

				GL11.glVertex3d(0, 0, 0);
				GL11.glVertex3d(16, 0, 0);

				GL11.glVertex3d(16, 0, 16);
				GL11.glVertex3d(16, 0, 0);

				GL11.glVertex3d(16, 0, 16);
				GL11.glVertex3d(0, 0, 16);

				
				GL11.glEnd();
			}
		}

	}

	private void prepareModel(ChunkMesh mesh) {
		GL30.glBindVertexArray(mesh.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

	}

	private void unbindModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareChunk(ChunkSection chunk) {
		Matrix4f transformationMatrix = Maths
				.createTransformationMatrix(
						new Vector3f(chunk.getPosition().x * ChunkSection.SIZE,
								chunk.getPosition().y * ChunkSection.SIZE, chunk.getPosition().z * ChunkSection.SIZE),
						0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
