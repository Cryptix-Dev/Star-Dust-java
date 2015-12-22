package io.cryptix.stardust;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Atlas {
	
	public static Texture playerImg;
	public static Texture maskImg;
	public static Texture gunImg;
	
	private static final int FRAME_COLS = 16;
	private static final int FRAME_ROWS = 1;
	
	public static Texture walkSheet;
	public static TextureRegion[] walkFrames;
	
	public Atlas() {
		loadTextures();
	}
	
	private void loadTextures() {
		playerImg = new Texture("main_character.png");
		gunImg = new Texture("gun.png");
		maskImg = new Texture("face_mask.png");
		
		walkSheet = new Texture(Gdx.files.internal("main_character_spritesheet.png"));
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/FRAME_COLS, walkSheet.getHeight()/FRAME_ROWS);
		walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;		
		for(int i = 0; i < FRAME_ROWS; i++){
			for(int j = 0; j < FRAME_COLS; j++){
				walkFrames[index++] = tmp[i][j];
			}
		}
	}
}
