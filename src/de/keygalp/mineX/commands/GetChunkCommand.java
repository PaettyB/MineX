package de.keygalp.mineX.commands;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.entities.Player;
import de.keygalp.mineX.guis.Console;
import de.keygalp.mineX.worlds.Chunk;
import de.keygalp.mineX.worlds.ChunkSection;
import org.joml.Vector2i;
import org.joml.Vector3i;

public class GetChunkCommand implements Command {

	@Override
	public boolean execute(String[] args) {
		if (args.length != 0) {
			Console.print("No Arguments needed!");
			return false;
		}
		Player player = Game.getPlayer();
		Vector3i cp = Game.getWorld().getChunkLoc((int) player.getPosition().x, (int) player.getPosition().y,
				(int) player.getPosition().z);
		Chunk c = Game.getWorld().getChunk(new Vector2i(cp.x, cp.z));
		if (c == null) {
			Console.print("Chunk = Null");
			return true;
		}

		Console.print(c.getPosition().x + ", " + c.getPosition().y + ": ");
		for (int i = 0; i < Chunk.HEIGHT; i++) {
			ChunkSection cs = c.getSection(i);
			if (cs == null) {
				Console.print(i + ": Null");
			} else {
				Console.print(i + ": " + cs.getState().toString());
			}
		}
		return true;
	}

}
