package io.cryptix.stardust;

import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.List;

public class PlayerInput implements InputProcessor {
	
	private final List<Integer> heldKeys;
	
	private final MainGame game;
	
	public PlayerInput(MainGame game) {
		heldKeys = new ArrayList<Integer>();
		this.game = game;
	}
	
	public void update() {
		for (int key : heldKeys) {
			keyPressed(key);
		}
	}
	
	public boolean keyPressed(int keycode) {
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		heldKeys.add(keycode);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		heldKeys.remove(heldKeys.lastIndexOf(keycode));
		return true;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		game.camera.calculateOffset(screenX, screenY);
		return true;
	}

	@Override
	public boolean keyTyped(char character) { return false; }

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

	@Override
	public boolean scrolled(int amount) { return false; }

}
