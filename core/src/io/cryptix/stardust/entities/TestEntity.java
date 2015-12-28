package io.cryptix.stardust.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.worlds.GameWorld;

public class TestEntity extends Entity {
	
	public TestEntity(GameWorld world, Vector2 position, float angle) {
		super(world, position, angle);
	}
	
	@Override
	public BodyType bodyType() {
		return BodyType.DynamicBody;
	}

	@Override
	public float width() {
		return 2;
	}

	@Override
	public float height() {
		return 2;
	}
	
	@Override
	public float mass() {
		return 100;
	}
	@Override
	public void destroy() { }

	@Override
	public void update() { }

	@Override
	public void render(GameRenderer batch) { }

}
