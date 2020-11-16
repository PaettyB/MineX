package de.keygalp.mineX.renderEngine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import de.keygalp.mineX.entities.Camera;
import de.keygalp.mineX.entities.Entity;
import de.keygalp.mineX.entities.Light;
import de.keygalp.mineX.input.KeyboardHandler;
import de.keygalp.mineX.models.TexturedModel;
import de.keygalp.mineX.shaders.StaticShader;
import de.keygalp.mineX.worlds.ChunkSection;

public class MasterRenderer {

	private static final float FOV =90;
	private static final float NEAR_PLANE = 0.01f;
	private static final float FAR_PLANE = 1000;

	private static final float RED = 0.42f;
	private static final float GREEN = 0.78f;
	private static final float BLUE = 1.0f;

	private boolean fill = true;
	private boolean cull = true;
	public static boolean CHUNK_BORDERS = false;
	private static boolean vSync = true;

	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private ChunkRenderer chunkRenderer;
	private EntityRenderer entityRenderer;

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<ChunkSection> chunks = new ArrayList<ChunkSection>();

	public MasterRenderer() {
		enableCulling();
		createProjectionMatrix();
		chunkRenderer = new ChunkRenderer(shader, projectionMatrix);
		entityRenderer = new EntityRenderer(shader, projectionMatrix);
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public static void enableVsync() {
		GLFW.glfwSwapInterval(1);
		vSync = true;
	}

	public static void disableVsync() {
		GLFW.glfwSwapInterval(0);
		vSync = false;
	}

	public void toggleVsync() {
		if (vSync)
			disableVsync();
		else
			enableVsync();

	}
	
	public void tick() {
		if (KeyboardHandler.keyTyped(GLFW.GLFW_KEY_F)) {
			fill = !fill;
		}
		if (KeyboardHandler.keyTyped(GLFW.GLFW_KEY_C)) {
			if (cull) {
				disableCulling();
				cull = false;
			} else {
				enableCulling();
				cull = true;
			}
		}
		if (KeyboardHandler.keyTyped(GLFW.GLFW_KEY_V)) {
			toggleVsync();
		}

		if (KeyboardHandler.keyTyped(GLFW.GLFW_KEY_B)) {
			CHUNK_BORDERS = !CHUNK_BORDERS;
		}
	}

	public void prepare() {
		// GL.createCapabilities();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(RED, GREEN, BLUE, 0.0f);
		if(fill)
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		else 
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
	}

	public void render(Light sun, Camera camera) {
		prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		chunkRenderer.render(chunks);
		entityRenderer.render(entities);
		shader.stop();
	}

	public void processEntity(Entity entity) {
		if (!entities.containsKey(entity.getModel())) {
			entities.put(entity.getModel(), new ArrayList<Entity>());
		}
		entities.get(entity.getModel()).add(entity);
	}

	public void removeEntity(Entity entity) {
		if (!entities.containsKey(entity.getModel())) return;
		entities.get(entity.getModel()).remove(entity);
	}

	public void processChunk(ChunkSection chunk) {
		chunks.add(chunk);
	}

	public void removeChunk(ChunkSection chunk) {
		chunks.remove(chunk);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) DisplayManager.WIDTH / (float) DisplayManager.HEIGHT;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix._m00(x_scale);
		projectionMatrix._m11(y_scale);
		projectionMatrix._m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
		projectionMatrix._m23(-1);
		projectionMatrix._m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
		projectionMatrix._m33(0);
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	public int getChunkCount() {
		return chunks.size();
	}
}
