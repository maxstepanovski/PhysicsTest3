package com.mamabayamba.physicstest3.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mamabayamba.physicstest3.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		System.setProperty("user.name", "\\xD0\\xBC\\xD0\\xB0\\xD0\\xBA\\xD1\\x81");
		new LwjglApplication(new MyGdxGame(), config);
	}
}
