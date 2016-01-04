package io.cryptix.stardust.entities;

import io.cryptix.stardust.GameRenderer;

public interface Drawable {
	public float drawPoint();
	
	public void render(GameRenderer batch);
}
