package de.keygalp.mineX.renderEngine;

import de.keygalp.mineX.assets.Assets;
import de.keygalp.mineX.shaders.StaticShader;
import de.keygalp.mineX.utils.Maths;
import de.keygalp.mineX.worlds.ChunkMesh;
import de.keygalp.mineX.worlds.ChunkSection;
import de.keygalp.mineX.worlds.ChunkState;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.util.List;

public class ChunkRenderer {
    
    private final StaticShader shader;
    
    public ChunkRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    
    public void render(List<ChunkSection> chunks) {
        
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Assets.SPRITE_SHEET.getId());
        
        for (int i = 0; i < chunks.size(); i++) {
            ChunkSection chunk = chunks.get(i);
            if (chunk.getState() == ChunkState.LOADED || chunk.getState() == ChunkState.UPDATED || chunk.getState() == ChunkState.REGENERATED) {
                if (chunk.getMesh() == null) {
                    System.err.println("MESH IS NULL");
                    return;
                }
                prepareModel(chunk.getMesh());
                prepareChunk(chunk);
                GL11.glDrawElements(GL11.GL_TRIANGLES, chunk.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT,0);
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
        glBindBuffer(GL_ARRAY_BUFFER, mesh.getVbo());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());
    
        glEnableClientState(GL_VERTEX_ARRAY);             // activate vertex position array
        glEnableClientState(GL_NORMAL_ARRAY);             // activate vertex normal array
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);      // activate texture coord array
        
        glVertexPointer(3, GL_BYTE,0,0);
        glNormalPointer(GL_BYTE,0, mesh.getNOffset());
        glTexCoordPointer(2, GL_FLOAT, 0, mesh.getTOffset());
        
        
    }
    
    private void unbindModel() {
        glDisableClientState(GL_VERTEX_ARRAY);            // deactivate vertex position array
        glDisableClientState(GL_NORMAL_ARRAY);            // deactivate vertex normal array
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);     // deactivate vertex tex coord array
        
        GL15.glBindBuffer(GL15.GL_VERTEX_ARRAY, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
    }
    
    private void prepareChunk(ChunkSection chunk) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(
                        chunk.getPosition().x * ChunkSection.SIZE,
                        chunk.getPosition().y * ChunkSection.SIZE,
                        chunk.getPosition().z * ChunkSection.SIZE),
                0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
