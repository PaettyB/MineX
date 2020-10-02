package de.keygalp.mineX.events.blocks;

import de.keygalp.mineX.Block;
import de.keygalp.mineX.entities.Player;

public class BlockBreakEvent extends BlockEvent {
	
	protected Player player;

	public BlockBreakEvent(Block block, Player player) {
		super(block);
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

}
