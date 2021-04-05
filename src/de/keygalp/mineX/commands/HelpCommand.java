package de.keygalp.mineX.commands;

import de.keygalp.mineX.guis.Console;

public class HelpCommand implements Command{
    @Override
    public boolean execute(String[] args) {
        Console.print("List of Commands: ");
        Console.print("-----------------");
        for (String command : CommandManager.commands.keySet() ){
            System.out.println(command);
            Console.print("." + command);
        }
        Console.print("-----------------");
        return true;
    }
}
