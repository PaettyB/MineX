package de.keygalp.mineX.textures;

public class ModelTexture {

	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	private int textureID;
	
	public ModelTexture(int id) {
		this.textureID = id;
	}
	
	public int getId() {
		return textureID;
	}
	
}
