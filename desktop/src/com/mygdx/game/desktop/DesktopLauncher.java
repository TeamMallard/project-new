package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.game.Game;

public class DesktopLauncher {
//	public static void main (String[] arg) {
//		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		TexturePacker.Settings settings = new TexturePacker.Settings();
//		settings.maxWidth = 512;
//		settings.maxHeight = 512;
//		TexturePacker.process(settings, "topack", "packedimages", "pack");
//		new LwjglApplication(new Game(), config);
//	}
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new Game(), config);
	}
}
