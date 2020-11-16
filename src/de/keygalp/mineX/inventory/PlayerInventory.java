package de.keygalp.mineX.inventory;

public class PlayerInventory implements Inventory {

	private int size;
	private ItemStack[] items;
	private InventoryHolder holder;
	private String name;

	private int heldItemSlot = 0;

	public PlayerInventory(int size, String name, InventoryHolder holder) {
		this.size = size;
		this.holder = holder;
		this.name = name;

		items = new ItemStack[size];
	}

	public boolean addItem(ItemStack stack) {
		int remaining = stack.getAmount();
		
		for (int i = 0; i < size; i++) {
			if (items[i] == null) {
				items[i] = new ItemStack(stack.getType(), remaining);
				remaining -= stack.getMaxStackSize();
				
			} else if (items[i].getType() == stack.getType()) {
				if (remaining > 0) {
					int available = stack.getMaxStackSize() - items[i].getAmount();
					if (available >= remaining) {
						items[i].setAmount(items[i].getAmount() + remaining);
						remaining = 0;
					} else {
						items[i].setAmount(stack.getMaxStackSize());
						remaining -= available;
					}
				}
			}
			if (remaining < 1)
				break;
		}
		return remaining < 1;
	}

	public int getSize() {
		return size;
	}

	public String getName() {
		return name;
	}

	public ItemStack getItem(int slot) {
		return items[slot] == null ? new ItemStack(0, 0) : items[slot];
	}

	public void setItem(int slot, ItemStack item) {
		items[slot] = item;
	}

	public void clear(int slot) {
		items[slot] = null;
	}

	public InventoryHolder getHolder() {
		return holder;
	}

	public int getHeldItemSlot() {
		return heldItemSlot;
	}

	public void setHeldItemSlot(int slot) {
		heldItemSlot = slot;
	}
}
