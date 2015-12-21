package io.cryptix.stardust;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

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
		
		if (keycode == Keys.W) {
			game.player.movePlayer(0, 1);
			return true;
		} else if (keycode == Keys.S) {
			game.player.movePlayer(0, -1);
			return true;
		} else if (keycode == Keys.D) {
			game.player.movePlayer(1, 0);
			return true;
		} else if (keycode == Keys.A) {
			game.player.movePlayer(-1, 0);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		heldKeys.remove(heldKeys.lastIndexOf(keycode));
		
		if (keycode == Keys.W) {
			game.player.movePlayer(0, -1);
			return true;
		} else if (keycode == Keys.S) {
			game.player.movePlayer(0, 1);
			return true;
		} else if (keycode == Keys.D) {
			game.player.movePlayer(-1, 0);
			return true;
		} else if (keycode == Keys.A) {
			game.player.movePlayer(1, 0);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		game.player.calculateGunRotation(game.camera.unproject(new Vector2(screenX, screenY)));
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		game.player.calculateGunRotation(game.camera.unproject(new Vector2(screenX, screenY)));
		return true;
	}

	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) { return false; }

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean scrolled(int amount) { return false; }

}
