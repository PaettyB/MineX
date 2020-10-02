package de.keygalp.mineX.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.PNGDecoder;

import de.keygalp.mineX.worlds.ChunkMesh;

public class Loader {

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	// UPDATING

	public void updateVao(int vaoID, int vboID, float[] positions) {
		GL30.glBindVertexArray(vaoID);
		updateAttributeList(0, 2, vboID, positions);
		unbindVAO();
	}

	public int updateVao(int vaoID, int posVBO, int texVBO, float[] positions, float[] textureCoords) {
		GL30.glBindVertexArray(vaoID);
		updateAttributeList(0, 2, posVBO, positions);
		updateAttributeList(1, 2, texVBO, textureCoords);
		unbindVAO();
		return vaoID;
	}

	private int updateAttributeList(int attributeNumber, int coordinateSize, int vboID, float[] data) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	private int updateAttributeList(int attributeNumber, int coordinateSize, int vboID, FloatBuffer buffer) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	private int updateAttributeList(int attributeNumber, int coordinateSize, int vboID, ByteBuffer buffer) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_BYTE, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}


	// LOADING

	public VertexArrayPointers loadToVao(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		int posVBO = storeDataInAttributeList(0, 2, positions);
		int texVBO = storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		VertexArrayPointers pointers = new VertexArrayPointers(vaoID, posVBO, texVBO);
		return pointers;
	}

	public RawModel loadToVAO(float[] postions) {
		int vaoID = createVAO();
		int vboID = storeDataInAttributeList(0, 2, postions);
		unbindVAO();
		return new RawModel(vaoID, vboID, postions.length / 2);
	}

	public RawModel loadToVao(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}

	public ChunkMesh loadChunkToVao(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		int indVBO = bindIndicesBuffer(indices);
		int posVBO = storeDataInAttributeList(0, 3, positions);
		int texVBO = storeDataInAttributeList(1, 2, textureCoords);
		int norVBO = storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new ChunkMesh(vaoID, indices.length, indVBO, posVBO, texVBO, norVBO);
	}

	public ChunkMesh loadChunkToVao(byte[] positions, float[] textureCoords, byte[] normals, int[] indices) {
		int vaoID = createVAO();
		int indVBO = bindIndicesBuffer(indices);
		int posVBO = storeDataInAttributeList(0, 3, positions);
		int texVBO = storeDataInAttributeList(1, 2, textureCoords);
		int norVBO = storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new ChunkMesh(vaoID, indices.length, indVBO, posVBO, texVBO, norVBO);
	}

	public ChunkMesh loadChunkToVao(ByteBuffer positions, FloatBuffer textureCoords, ByteBuffer normals,
			IntBuffer indices) {
		int vaoID = createVAO();
		int indVBO = bindIndicesBuffer(indices);
		int posVBO = storeDataInAttributeList(0, 3, positions);
		int texVBO = storeDataInAttributeList(1, 2, textureCoords);
		int norVBO = storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new ChunkMesh(vaoID, indices.capacity(), indVBO, posVBO, texVBO, norVBO);
	}
	
	public int updateChunkInVao(int vaoID, int posVBO, int indVBO, int texVBO, int norVBO, ByteBuffer positions, FloatBuffer textureCoords, ByteBuffer normals,
			IntBuffer indices) {
		GL30.glBindVertexArray(vaoID);
		updateIndicesBuffer(indVBO, indices);
		updateAttributeList(0, 3, posVBO, positions);
		updateAttributeList(1, 2, texVBO, textureCoords);
		updateAttributeList(2, 3, norVBO, normals);
		unbindVAO();
		return vaoID;
	}
	
	
	

	public RawModel loadToVao(ModelData data) {
		int vaoID = createVAO();
		bindIndicesBuffer(data.getIndices());
		storeDataInAttributeList(0, 3, data.getVertices());
		storeDataInAttributeList(1, 2, data.getTextureCoords());
		storeDataInAttributeList(2, 3, data.getNormals());
		unbindVAO();
		return new RawModel(vaoID, data.getIndices().length);
	}

	@SuppressWarnings("null")
	public int loadTexture(String filepath) {
		try {
			File file = new File("res/" + filepath + ".png");
			InputStream in = new FileInputStream(file);
			PNGDecoder decoder = new PNGDecoder(in);
			ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.RGBA);
			buffer.flip();

			int textureID = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			// WRAP OR NOT TO WRAP, THAT IS THE QUESTION
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

			// ME DOEST SMOOTH OR SHARP? SHARP!
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

			// MIP MAP!
			GL30.glGenerateMipmap(textureID);

			// MAKE!
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0,
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			return textureID;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Integer) null;

	}

	public void cleanUp() {
		 System.out.println(vaos.size()+ ": " + vbos.size());

		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}

		for (int vao : vbos) {
			GL15.glDeleteBuffers(vao);
		}
		for (int tex : textures) {
			GL11.glDeleteTextures(tex);
		}
	}

	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private int storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	private int storeDataInAttributeList(int attributeNumber, int coordinateSize, FloatBuffer buffer) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	private int storeDataInAttributeList(int attributeNumber, int coordinateSize, byte[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		ByteBuffer buffer = storeDataInByteBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_BYTE, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	private int storeDataInAttributeList(int attributeNumber, int coordinateSize, ByteBuffer buffer) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_BYTE, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private int bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		return vboID;
	}

	private int bindIndicesBuffer(IntBuffer buffer) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		return vboID;
	}
	
	private void updateIndicesBuffer(int vboID, IntBuffer buffer) {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	public IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public ByteBuffer storeDataInByteBuffer(byte[] data) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public IntBuffer storeDataInIntBuffer(int[] data, int length) {
		IntBuffer buffer = BufferUtils.createIntBuffer(length);
		buffer.put(data, 0, length);
		buffer.flip();
		return buffer;
	}

	public FloatBuffer storeDataInFloatBuffer(float[] data, int length) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(length);
		buffer.put(data, 0, length);
		buffer.flip();
		return buffer;
	}

	public ByteBuffer storeDataInByteBuffer(byte[] data, int length) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(length);
		buffer.put(data, 0, length);
		buffer.flip();
		return buffer;
	}
}
