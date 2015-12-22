package io.cryptix.stardust.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import io.cryptix.stardust.Atlas;
import io.cryptix.stardust.GameRenderer;

public class BoxEntity extends Entity {

	private Body body;
	
	public BoxEntity(World world, Vector2 position, float angle) {
		super(world, position, angle);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(position.x, position.y);
		bodyDef.angle = angle * MathUtils.degreesToRadians;
		body = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1f, 1f);
		body.createFixture(shape, 20f);
		
		shape.dispose();
		
		body.setUserData(this);
	}
	
	@Override
	public float drawPoint() {
		return 0;
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void render(GameRenderer batch) {
		batch.draw(Atlas.maskImg, this.getPosition().x, this.getPosition().y, this.getRotation());
	}

}
