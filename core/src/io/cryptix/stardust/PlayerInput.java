package io.cryptix.stardust;

import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PlayerInput implements InputProcessor {
	
	private final MainGame game;
	private final Array<Integer> heldKeys;
	
	private Vector2 mousePos;
	
	public PlayerInput(MainGame game) {
		this.game = game;
		heldKeys = new Array<Integer>();
		mousePos = new Vector2();
	}
	
	public void update() {
		for (int key : heldKeys) {
			keyPressed(key);
		}
		
		game.player.calculateGunRotation(game.camera.unproject(mousePos));
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
		heldKeys.removeValue(keycode, true);
		
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
	
	public Vector2 getMousePosition() {
		return mousePos;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mousePos = new Vector2(screenX, screenY);
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mousePos = new Vector2(screenX, screenY);
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
