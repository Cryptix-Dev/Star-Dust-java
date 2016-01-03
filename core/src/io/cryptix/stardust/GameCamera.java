package io.cryptix.stardust;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameCamera {
	
	private Viewport viewport;
	private Camera camera;
	
	private float gameScale    = .03f;
	private float lerp 		   = 0.02f;
	
	private Vector3 targetPosition = new Vector3();
	
	public GameCamera() {
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		((ScreenViewport)viewport).setUnitsPerPixel(gameScale);
	}
	
	public void update() {
		Vector3 cPos = camera.position;
		camera.position.x += (targetPosition.x - cPos.x) * lerp;
		camera.position.y += (targetPosition.y - cPos.y) * lerp;
		camera.update();
	}
	
	public Vector2 project(Vector2 coords) {
		Vector3 res = camera.project(new Vector3(coords.x, coords.y, 0));
		return new Vector2(res.x, res.y);
	}
	
	public Vector2 unproject(Vector2 coords) {
		Vector3 res = camera.unproject(new Vector3(coords.x, coords.y, 0));
		return new Vector2(res.x, res.y);
	}
	
	public void setTarget(Vector2 newPos) {
		targetPosition = new Vector3(newPos, 0f);
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
	
	public Vector2 getViewportSize() {
		return new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
	}
}
