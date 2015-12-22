package io.cryptix.stardust;

import com.badlogic.gdx.graphics.Texture;

public class Atlas {
	
	public static Texture playerImg;
	public static Texture maskImg;
	public static Texture gunImg;
	
	public Atlas() {
		loadTextures();
	}
	
	private void loadTextures() {
		playerImg = new Texture("main_character.png");
		gunImg = new Texture("gun.png");
		maskImg = new Texture("face_mask.png");
	}
}
