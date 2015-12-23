package io.cryptix.stardust;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.cryptix.stardust.utils.Util;

public class GameRenderer extends SpriteBatch {
	
	private boolean drawCenter;
	
	public GameRenderer() {
		super();
		drawCenter = true;
	}
	
	public boolean isDrawingFromCenter() {
		return drawCenter;
	}
	
	public void setDrawingFromCenter(boolean value) {
		this.drawCenter = value;
	}
	
	@Override
	public void draw(Texture texture, float x, float y) {
		float mWidth = Util.convertToMeters(texture.getWidth());
		float mHeight = Util.convertToMeters(texture.getHeight());
		if (drawCenter)
			super.draw(texture, x - mWidth/2, y - mHeight/2, mWidth, mHeight);
		else
			super.draw(texture, x, y, mWidth, mHeight);
	}
	
	public void draw(Texture texture, float x, float y, float rotation) {
		float mWidth = Util.convertToMeters(texture.getWidth());
		float mHeight = Util.convertToMeters(texture.getHeight());
		if (drawCenter)
			super.draw(texture, x - mWidth/2, y - mHeight/2,
					   mWidth/2, mHeight/2, mWidth, mHeight, 1, 1,
					   rotation, 0, 0, texture.getWidth(), texture.getHeight(),
					   false, false);
		else
			super.draw(texture, x, y, mWidth, mHeight, mWidth, mHeight, 1, 1,
					   rotation, 0, 0, texture.getWidth(), texture.getHeight(),
					   false, false);
	}
	
	public void draw(Texture texture, float x, float y, float rotX, float rotY, float rotation, boolean flipX, boolean flipY) {
		float mWidth = Util.convertToMeters(texture.getWidth());
		float mHeight = Util.convertToMeters(texture.getHeight());
		if (drawCenter)
			super.draw(texture, x - mWidth/2, y - mHeight/2,
					   rotX, rotY, mWidth, mHeight, 1, 1,
					   rotation, 0, 0, texture.getWidth(), texture.getHeight(),
					   flipX, flipY);
		else
			super.draw(texture, x, y, rotX, rotY, mWidth, mHeight, 1, 1,
					   rotation, 0, 0, texture.getWidth(), texture.getHeight(),
					   flipX, flipY);
	}
	
	public void draw(Texture texture, float x, float y, float rotX, float rotY, float rotation) {
		draw(texture, x, y, rotX, rotY, rotation, false, false);
	}
	
	public void draw(Texture texture, float x, float y, float rotation, boolean flipX, boolean flipY) {
		float mWidth = Util.convertToMeters(texture.getWidth());
		float mHeight = Util.convertToMeters(texture.getHeight());
		if (drawCenter)
			super.draw(texture, x - mWidth/2, y - mHeight/2,
					   mWidth/2, mHeight/2, mWidth, mHeight, 1, 1,
					   rotation, 0, 0, texture.getWidth(), texture.getHeight(),
					   flipX, flipY);
		else
			super.draw(texture, x, y, mWidth, mHeight, mWidth, mHeight, 1, 1,
					   rotation, 0, 0, texture.getWidth(), texture.getHeight(),
					   flipX, flipY);
	}
	
	public void draw(Texture texture, float x, float y, boolean flipX, boolean flipY) {
		draw(texture, x, y, 0, flipX, flipY);
	}
	
	
	@Override
	public void draw(TextureRegion region, float x, float y) {
		float mWidth = Util.convertToMeters(region.getRegionWidth());
		float mHeight = Util.convertToMeters(region.getRegionHeight());
		if (drawCenter)
			super.draw(region, x - mWidth/2, y - mHeight/2, mWidth, mHeight);
		else
			super.draw(region, x, y, mWidth, mHeight);
	}
	
	public void draw(TextureRegion region, float x, float y, float rotation) {
		float mWidth = Util.convertToMeters(region.getRegionWidth());
		float mHeight = Util.convertToMeters(region.getRegionHeight());
		if (drawCenter)
			super.draw(region, x - mWidth/2, y - mHeight/2,
					   mWidth/2, mHeight/2, mWidth, mHeight, 1, 1, rotation);
		else
			super.draw(region, x, y, mWidth, mHeight, mWidth, mHeight, 1, 1,
					   rotation);
	}
}
