package de.keygalp.mineX.commands;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.entities.Player;
import de.keygalp.mineX.guis.Console;
import de.keygalp.mineX.worlds.Chunk;

public class ReloadCommand implements Command{

	public ReloadCommand() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(String[] args) {
		if (args.length != 0) {
			Console.print("No Arguments needed!");
			return false;
		}
		Player player = Game.getPlayer();
		Chunk c = Game.getWorld().getChunkAtBlockPos((int) player.getPosition().x, (int) player.getPosition().y,
				(int) player.getPosition().z);
		if(c == null) {
			Console.print("Null");
			return true;
		}
		
		Game.getWorld().unloadChunk(c.getPosition());
		Game.getWorld().createChunk(c.getPosition());
		return true;
	}

}
