package io.cryptix.stardust.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.worlds.GameWorld;

public class TestEntity extends Entity {
	
	public TestEntity(GameWorld world, Vector2 position, float angle) {
		super(world, position, angle);
	}
	
	@Override
	public BodyDef bodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(this.getPosition());
		return bodyDef;
	}
	
	@Override
	public void createBody() {
		super.createBody();
		//this.getBody().setLinearVelocity(new Vector2(2f, 0));
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
	public void destroy() { }

	@Override
	public void update() { }

	@Override
	public void render(GameRenderer batch) { }

}
