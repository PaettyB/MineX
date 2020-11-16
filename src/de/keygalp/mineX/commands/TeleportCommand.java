package de.keygalp.mineX.commands;

import org.joml.Vector3f;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.guis.Console;

public class TeleportCommand implements Command {

	@Override
	public boolean execute(String[] args) {
		if (args.length == 3) {
			try {
				float x, y, z;
				x = Float.parseFloat(args[0]);
				y = Float.parseFloat(args[1]);
				z = Float.parseFloat(args[2]);
				Vector3f pos = new Vector3f(x, y, z);
				Game.getPlayer().teleport(pos);
				Console.print("Teleportet Player to: " + (int) x + " " + (int) y + " " + (int) z);
				return true;
			} catch (Exception e) {
				Console.print("Usage: .tp [x] [y] [z]");
				return false;
			}
		} else {
			Console.print("Usage: .tp [x] [y] [z]");
			return false;
		}
	}
}
