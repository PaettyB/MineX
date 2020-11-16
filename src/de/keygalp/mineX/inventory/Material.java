package de.keygalp.mineX.inventory;

public enum Material {

	AIR(0), DIRT(1), GRASS(2), STONE(3), OAK_LOG( 4);

	
	private static Material[] byId = new Material[5];
	
	private final int id;
	private final int maxStackSize;
	
	Material(final int id) {
		this(id, 64);
	}
	
	Material(final int id, int maxStackSize){
		this.id = id;
		this.maxStackSize = maxStackSize;
	}
	
	
	static {
		for(Material material : values()) {
			byId[material.id] = material;
		}
	}

	public int getId() {
		return id;
	}
	
	public static Material getMaterial(int id) {
		return byId[id];
	}
	
	public int getMaxStackSize() {
		return this.maxStackSize;
	}
}