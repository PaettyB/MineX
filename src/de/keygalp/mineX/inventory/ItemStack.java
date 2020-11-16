package de.keygalp.mineX.inventory;

public class ItemStack {
	private int type ;
	private int amount;

	public ItemStack(int id) {
		this(id,1);
	}
	
	public ItemStack(int id, int amount) {
		this.type= id;
		this.amount = amount;
	}
	
	public ItemStack(Material material, int amount) {
		this.type = material.getId();
		this.amount = amount;
	}

	public Material getType() {
		return getType(this.type);
	}
	
	public static Material getType(int id) {
		Material material = Material.getMaterial(id);
		return material == null ? Material.AIR : material;
	}
	
	public int getTypeId() {
        return type;
    }
	
	public int getAmount() {
        return amount;
    }
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getMaxStackSize() {
        Material material = getType();
        if (material != null) {
            return material.getMaxStackSize();
        }
        return -1;
    }

}
