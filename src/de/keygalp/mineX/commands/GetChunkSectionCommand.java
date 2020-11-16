package de.keygalp.mineX.commands;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.entities.Player;
import de.keygalp.mineX.guis.Console;
import de.keygalp.mineX.worlds.ChunkSection;

public class GetChunkSectionCommand implements Command {

	public GetChunkSectionCommand() {

	}

	@Override
	public boolean execute(String[] args) {
		if (args.length != 0) {
			Console.print("No Arguments needed!");
			return false;
		}
		Player player = Game.getPlayer();
		ChunkSection cs = Game.getWorld().getChunkSectionAtBlockPos((int) player.getPosition().x, (int) player.getPosition().y,
				(int) player.getPosition().z);
		if(cs == null) {
			Console.print("Null");
			return true;
		}
			
		Console.print(cs.getState().toString());
		return true;
	}

}
