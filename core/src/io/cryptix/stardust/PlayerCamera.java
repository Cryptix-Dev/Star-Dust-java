package io.cryptix.stardust;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayerCamera {
	
	private Viewport viewport;
	private Camera camera;
	
	private float gameScale    = .05f;
	private float moveDistance = 5f; 
	
	private Vector3 offset = new Vector3();
	private Vector3 position = new Vector3();
	
	public PlayerCamera() {
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		((ScreenViewport)viewport).setUnitsPerPixel(gameScale);
	}
	
	public void update() {
		camera.position.set(position.x + offset.x, position.y + offset.y, position.z);
		camera.update();
	}
	
	public void calculateOffset(int mouseX, int mouseY) {
		offset.x = (((float) mouseX / Gdx.graphics.getWidth()) - .5f) * 2 * moveDistance;
		offset.y = (.5f - ((float) mouseY / Gdx.graphics.getHeight())) * 2 * moveDistance;
	}
	
	public void setPosition(Vector2 newPos) {
		position = new Vector3(newPos, 0f);
	}
	
	public Matrix4 getProjViewMatrix() {
		return this.camera.combined;
	}
	
	public Vector2 getPosition() {
		return new Vector2(camera.position.x, camera.position.y);
	}
	
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
