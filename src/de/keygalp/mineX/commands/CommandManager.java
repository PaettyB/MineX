package de.keygalp.mineX.commands;

import java.util.HashMap;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.guis.Console;

public class CommandManager {
	
	private static HashMap<String, Command> commands = new HashMap<String, Command>();
	
	public static void registerCommand(String name, Command command) {
		commands.put(name, command);
	}
	
	public static void executeCommand(String name, String[] args) {
		if(!commands.containsKey(name)) {
			Console.print("This command doesn't exist!");
		}
		try {
		commands.get(name).execute(args);
		} catch(Exception e) {
			Console.print("Error with command: " + name);
			e.printStackTrace();
		}
	}
	
	public static void registerAll() {
		registerCommand("tp", new TeleportCommand());
		registerCommand("getChunkSec", new GetChunkSectionCommand());
		registerCommand("getChunk", new GetChunkCommand());
		registerCommand("reload", new ReloadCommand());
	}

}
