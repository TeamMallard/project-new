package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Game;

/**
 * Launcher for the game.
 */
public class DesktopLauncher {

	/**
	 * Main method.
	 * @param arg command-line arguments
     */
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;

		/**
		 * CHANGE M1: Altered resolution from 960x720 to 1280x720.
		 */
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new Game(), config);
	}
}
