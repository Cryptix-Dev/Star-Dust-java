package io.cryptix.stardust.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

import io.cryptix.stardust.Atlas;
import io.cryptix.stardust.GameRenderer;

public class BoxEntity extends Entity {

	private Body body;
	
	public BoxEntity(World world, Vector2 position, float angle) {
		super(world, position, angle);
	}
	
	@Override
	public BodyDef bodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(this.getPosition());
		bodyDef.angle = this.getRotation() * MathUtils.degreesToRadians;
		return bodyDef;
	}

	@Override
	public FixtureDef[] fixtureDefs() {
		FixtureDef[] output = new FixtureDef[1];
		PolygonShape shape = new PolygonShape();
		output[0] = new FixtureDef();
		return output;
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
	public void update() { }

	@Override
	public void render(GameRenderer batch) {
		batch.draw(Atlas.maskImg, this.getPosition().x, this.getPosition().y, this.getRotation());
	}

	@Override
	public float width() {
		return 8f;
	}

	@Override
	public float height() {
		return 8f;
	}

	@Override
	public void initialize() { }

	@Override
	public void destroy() { }

}
