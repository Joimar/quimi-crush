package com.quimic.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.quimic.game.QuimiCrush;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "QuimiCrush";
		config.resizable = false;	
		//config.width = 320;
		//config.height = 568;//
		new LwjglApplication(new QuimiCrush(), config);
	}
}
