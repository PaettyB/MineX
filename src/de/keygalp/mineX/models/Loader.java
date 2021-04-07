package de.keygalp.mineX.models;

import de.keygalp.mineX.worlds.ChunkMesh;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.PNGDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Loader {
    
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();
    
    // UPDATING
    
    public void updateVao(int vaoID, int vboID, float[] positions) {
        glBindVertexArray(vaoID);
        updateAttributeList(0, 2, vboID, positions);
        unbindVAO();
    }
    
    public int updateVao(int vaoID, int posVBO, int texVBO, float[] positions, float[] textureCoords) {
        glBindVertexArray(vaoID);
        updateAttributeList(0, 2, posVBO, positions);
        updateAttributeList(1, 2, texVBO, textureCoords);
        unbindVAO();
        return vaoID;
    }
    
    private int updateAttributeList(int attributeNumber, int coordinateSize, int vboID, float[] data) {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vboID;
    }
    
    private int updateAttributeList(int attributeNumber, int coordinateSize, int vboID, FloatBuffer buffer) {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vboID;
    }
    
    private int updateAttributeList(int attributeNumber, int coordinateSize, int vboID, ByteBuffer buffer) {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL_BYTE, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
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
    
    public ChunkMesh loadChunkToVao(FloatBuffer positions, FloatBuffer textureCoords, FloatBuffer normals,
                                    IntBuffer indices) {
        
        //TODO: REWRITE LOADING to not use ARRAY BUFFER
        int ibo = glGenBuffers();
        int vbo = glGenBuffers();
        
        System.out.println(ibo + "=ibo, " + vbo + "=vbo");
        
        if (vbos.contains(vbo) || vbos.contains(ibo))
            System.out.println("ERROR While loading");
        
        vbos.add(ibo);
        vbos.add(vbo);
        
        System.out.print("tex: ");
        
        for (int i = 0; i < textureCoords.capacity(); i++) {
            System.out.print(textureCoords.get(i) + " ");
            
        }
        System.out.println("");
        
        int size_pos = Float.BYTES * positions.capacity();
        int size_nor = Float.BYTES * positions.capacity();
        int size_tex = Float.BYTES * positions.capacity();
        int size_ind = Integer.BYTES * indices.capacity();
        
        int nOffset = size_pos;
        int tOffset = size_pos + size_nor;
        
        
        System.out.println("Indices: " + indices.capacity());
        System.out.println("Vertices: " + positions.capacity());
        System.out.println("Texture Coords: " + textureCoords.capacity());
        System.out.println("Normals : " + normals.capacity());
        
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, size_pos + size_nor + size_tex, GL_STATIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, positions);
        glBufferSubData(GL_ARRAY_BUFFER, nOffset, normals);
        glBufferSubData(GL_ARRAY_BUFFER, tOffset, textureCoords);
        
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        
        return new ChunkMesh(vbo, ibo, indices.capacity(), nOffset, tOffset);
    }
    
    /*
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
     */
    
    public int updateChunkInVao(int vaoID, int posVBO, int indVBO, int texVBO, int norVBO, byte[] positions, float[] textureCoords, byte[] normals, int[] indices) {
        glBindVertexArray(vaoID);
        updateIndicesBuffer(indVBO, indices);
        updateAttributeList(0, 3, posVBO, storeDataInByteBuffer(positions));
        updateAttributeList(1, 2, texVBO, textureCoords);
        updateAttributeList(2, 3, norVBO, storeDataInByteBuffer(normals));
        unbindVAO();
        return vaoID;
    }
    
    public int updateChunkInVao(int vaoID, int posVBO, int indVBO, int texVBO, int norVBO, ByteBuffer positions, FloatBuffer textureCoords, ByteBuffer normals,
                                IntBuffer indices) {
        glBindVertexArray(vaoID);
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
            
            int textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureID);
            // WRAP OR NOT TO WRAP, THAT IS THE QUESTION
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            
            // ME DOEST SMOOTH OR SHARP? SHARP!
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            
            // MIP MAP!
            glGenerateMipmap(textureID);
            
            // MAKE!
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0,
                    GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            
            glBindTexture(GL_TEXTURE_2D, 0);
            return textureID;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Integer) null;
        
    }
    
    public void cleanUp() {
        System.out.println(vaos.size() + ": " + vbos.size());
        
        for (int vao : vaos) {
            glDeleteVertexArrays(vao);
        }
        
        for (int vao : vbos) {
            glDeleteBuffers(vao);
        }
        for (int tex : textures) {
            glDeleteTextures(tex);
        }
    }
    
    private int createVAO() {
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }
    
    private int storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vboID;
    }
    
    private int storeDataInAttributeList(int attributeNumber, int coordinateSize, FloatBuffer buffer) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vboID;
    }
    
    private int storeDataInAttributeList(int attributeNumber, int coordinateSize, byte[] data) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        ByteBuffer buffer = storeDataInByteBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL_BYTE, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vboID;
    }
    
    private int storeDataInAttributeList(int attributeNumber, int coordinateSize, ByteBuffer buffer) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL_BYTE, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vboID;
    }
    
    private void unbindVAO() {
        glBindVertexArray(0);
    }
    
    private int bindIndicesBuffer(int[] indices) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        return vboID;
    }
    
    private int bindIndicesBuffer(IntBuffer buffer) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        return vboID;
    }
    
    private void updateIndicesBuffer(int vboID, int[] data) {
        IntBuffer buffer = storeDataInIntBuffer(data);
        updateIndicesBuffer(vboID, buffer);
    }
    
    private void updateIndicesBuffer(int vboID, IntBuffer buffer) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
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
