package de.keygalp.mineX.inventory;

public interface Inventory {

	public int getSize();

	public String getName();

	public ItemStack getItem(int slot);

	public void setItem(int slot, ItemStack item);

	public void clear(int slot);

	public InventoryHolder getHolder();
}
