package io.cryptix.stardust.worlds;

import com.badlogic.gdx.math.Vector2;

import io.cryptix.stardust.entities.TestEntity;
import io.cryptix.stardust.utils.Point;

public class TestGameWorld extends GameWorld {

	public TestGameWorld(int width, int height) {
		super(width, height);
	}
	
	@Override
	public Chunk generateChunk(int x, int y) {
		return new Chunk(this, x, y);
	}
	
	@Override
	public Grid generateGrid(Point worldPosition) {
		Grid g = new Grid(worldPosition);
		g.placeEntity(new TestEntity(this, Vector2.Zero, 0), Vector2.Zero);
		return g;
	}

	@Override
	public boolean repeats() {
		return true;
	}

	@Override
	public int size() {
		return 10;
	}
}
