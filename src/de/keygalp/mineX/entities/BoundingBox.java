package de.keygalp.mineX.entities;

import de.keygalp.mineX.input.KeyboardHandler;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class BoundingBox {
    
    private Vector3f offset;
    private Vector3f size;
    private Vector3f pos;
    
    public BoundingBox(Vector3f pos, Vector3f offset, Vector3f size) {
        super();
        this.offset = offset;
        this.size = size;
        this.pos = pos;
    }
    
    public boolean intersects(BoundingBox other) {
        return (this.getMinX() < other.getMaxX() && this.getMaxX() > other.getMinX())
                && (this.getMinY() < other.getMaxY() && this.getMaxY() > other.getMinY())
                && (this.getMinZ() < other.getMaxZ() && this.getMaxZ() > other.getMinZ());
    }
    
    /**
     * Checks for collision and returns a correction vector,
     * by wich the position of the Object of the BoundingBox will be reversed if there was a collition
     *
     * @param ownVelocity
     * @param other
     * @return
     */
    public Vector3f collide(Vector3f ownVelocity, BoundingBox other) {
        Vector3f delta = new Vector3f();
        
        if (intersects(other)) {
            float midDeltaX = Math.abs(getMidX() - other.getMidX());
            float midDeltaY = Math.abs(getMidY() - other.getMidY());
            float midDeltaZ = Math.abs(getMidZ() - other.getMidZ());
            if(midDeltaX > midDeltaY && midDeltaX > midDeltaZ){
                //X is largest
                float x1 = this.getMinX() - other.getMaxX();
                float x2 = this.getMaxX() - other.getMinX();
                float deltaX = Math.abs(x1) < Math.abs(x2) ? x1 : x2;
                delta.x = deltaX;
                ownVelocity.x = 0;
            } else if(midDeltaY > midDeltaZ){
                //Y is largest
                float y1 = this.getMinY() - other.getMaxY();
                float y2 = this.getMaxY() - other.getMinY();
                float deltaY = Math.abs(y1) < Math.abs(y2) ? y1 : y2;
                delta.y = deltaY;
                ownVelocity.y = 0;
            } else {
                //Z is largest
                float z1 = this.getMinZ() - other.getMaxZ();
                float z2 = this.getMaxZ() - other.getMinZ();
                float deltaZ = Math.abs(z1) < Math.abs(z2) ? z1 : z2;
                delta.z = deltaZ;
                ownVelocity.z = 0;
            }
        }
        //System.out.println("Delta: " + deltaX + ", " + deltaY + ", " + deltaZ);
        return delta;
    }
    
    public boolean collideX(BoundingBox other) {
        return (this.getMinX() <= other.getMaxX() && this.getMaxX() >= other.getMinX());
    }
    
    public boolean collideY(BoundingBox other) {
        return (this.getMinY() <= other.getMaxY() && this.getMaxY() >= other.getMinY());
    }
    
    public boolean collideZ(BoundingBox other) {
        return (this.getMinZ() <= other.getMaxZ() && this.getMaxZ() >= other.getMinZ());
    }
    
    
    public Vector3f getOffset() {
        return offset;
    }
    
    public void setOffset(Vector3f offset) {
        this.offset = offset;
    }
    
    public Vector3f getSize() {
        return size;
    }
    
    public void setSize(Vector3f size) {
        this.size = size;
    }
    
    public Vector3f getPos() {
        return pos;
    }
    
    public void setPos(Vector3f pos) {
        this.pos = pos;
    }
    
    public float getMinX() {
        return Math.min(pos.x + offset.x(), pos.x() + offset.x() + size.x());
    }
    
    public float getMaxX() {
        return Math.max(pos.x + offset.x(), pos.x() + offset.x() + size.x());
    }
    
    public float getMinY() {
        return Math.min(pos.y + offset.y(), pos.y() + offset.y() + size.y());
    }
    
    public float getMaxY() {
        return Math.max(pos.y + offset.y(), pos.y() + offset.y() + size.y());
    }
    
    public float getMinZ() {
        return Math.min(pos.z + offset.z(), pos.z() + offset.z() + size.z());
    }
    
    public float getMaxZ() {
        return Math.max(pos.z + offset.z(), pos.z() + offset.z() + size.z());
    }
    
    public float getMidX(){
        return pos.x + offset.x + 0.5f * (size.x);
    }
    
    public float getMidY(){
        return pos.y + offset.y + 0.5f * (size.y);
    }
    
    public float getMidZ(){
        return pos.y + offset.y + 0.5f * (size.y);
    }
}