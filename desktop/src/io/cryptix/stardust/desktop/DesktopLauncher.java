package io.cryptix.stardust.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.cryptix.stardust.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Star Dust";
		config.width = 1280;
		config.height = 720;
		config.vSyncEnabled = true;
		new LwjglApplication(new MainGame(), config);
	}
}
