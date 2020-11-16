package de.keygalp.mineX.input;

import org.lwjgl.glfw.GLFWCharCallback;

import java.util.ArrayList;
import java.util.List;

public class CharacterHandler extends GLFWCharCallback{
	
	public static List<Character> characters = new ArrayList<Character>();

	@Override
	public void invoke(long window, int codepoint) {
		characters.add((char) codepoint);
	}
	
	
	public static void clear() {
		characters.clear();
	}

}
