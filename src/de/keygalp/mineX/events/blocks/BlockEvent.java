package de.keygalp.mineX.events.blocks;

import de.keygalp.mineX.Block;
import de.keygalp.mineX.events.IEvent;

public abstract class BlockEvent implements IEvent{
	
	protected Block block;

	public BlockEvent(Block block) {
		this.block = block;
	}
	
	public Block getBlock() {
		return block;
	}
}
