package de.keygalp.mineX.guis;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import org.joml.Vector2i;
import org.joml.Vector4f;

import de.keygalp.mineX.Game;
import de.keygalp.mineX.guis.rendering.GUIObject;
import de.keygalp.mineX.guis.rendering.GUIRenderer;
import de.keygalp.mineX.models.Loader;
import de.keygalp.mineX.renderEngine.DisplayManager;

public class FrameTimeGraph {


	private GUIObject[] bars = new GUIObject[120];
	private Deque<Float> queue = new ArrayDeque<Float>();

	public FrameTimeGraph(Vector2i position, GUIRenderer renderer, Loader loader) {
		for (int i = 0; i < bars.length; i++) {
			bars[i] = new GUIObject(new Vector2i(position.x + i * 1, position.y), new Vector2i(1, 1), loader);
			renderer.addObject(bars[i]);
		}

	}

	public void update() {
		queue.add(Game.frameTimeMS);
		if (queue.size() > bars.length) {
			queue.remove();
		}
		int i = 0;
		for (Iterator<Float> itr = queue.iterator(); itr.hasNext();) {
			float time = itr.next();
			bars[i].setScale(new Vector2i(1, Math.round(time * 0.5f)));
			if (time < DisplayManager.TimePerTick / 1000000 - 10) {
				bars[i].setColor(new Vector4f(1,1,1,0.5f));
			}
			if(time > DisplayManager.TimePerTick / 1000000 - 10) {
				bars[i].setColor(new Vector4f(0,1,0,0.5f));
			}
			if(time > DisplayManager.TimePerTick / 1000000) {
				bars[i].setColor(new Vector4f(1,0,0,0.5f));
			}
			i++;
		}
		
	}

}
