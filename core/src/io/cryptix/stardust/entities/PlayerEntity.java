package io.cryptix.stardust.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.cryptix.stardust.Atlas;
import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.Util;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PlayerEntity extends Entity {
	
	private Body body;

	private Vector2 gunPos = new Vector2();
	private float gunRotation = 0;
	private boolean flip = false;
	
	private Vector2 lastMoveVel = Vector2.Zero;
	private Vector2 direction = new Vector2();
	private float velocity = 10f;
	
	public PlayerEntity(World world, Vector2 position) {
		super(world, position, 0);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type  = BodyType.DynamicBody;
		bodyDef.position.set(position.x, position.y);
		bodyDef.angle = 0;
		bodyDef.fixedRotation = true;
		body = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Util.convertToMeters(Atlas.playerImg.getWidth())/2,
					   Util.convertToMeters(Atlas.playerImg.getHeight())/2);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 40f;
		body.createFixture(fixture);
		shape.dispose();
		
		body.setUserData(this);
	}
	
	public void calculateGunRotation(Vector2 mouse) {
		gunRotation = (float) MathUtils.atan2(mouse.y - (gunPos.y + .2f), mouse.x - (gunPos.x - .9f)) * MathUtils.radiansToDegrees;
		if (gunRotation < 0) gunRotation += 360;
	}
	
	public void movePlayer(float x, float y) {
		direction.add(x, y);
	}

	@Override
	public void update() {
		gunPos = new Vector2(getPosition().x + .9f, getPosition().y - .3f);
		if (gunRotation >= 90 && gunRotation <= 270) flip = true; else flip = false;
		
		// TODO: Check if can move.
		Vector2 moveVel = new Vector2(direction.x * velocity, direction.y * velocity); 
		body.setLinearVelocity(moveVel);
		lastMoveVel = moveVel;
	}
	
	@Override
	public float drawPoint() {
		return this.getPosition().y - Util.convertToMeters(Atlas.playerImg.getHeight())/2;
	}

	@Override
	public void render(GameRenderer batch) {
		if (gunRotation < 25 || gunRotation > 155) {
			batch.draw(Atlas.playerImg, this.getPosition().x, this.getPosition().y, this.getRotation(), flip, false);
			batch.setColor(Color.valueOf("F4D841"));
			batch.draw(Atlas.maskImg, this.getPosition().x - (flip ? .25f : -.25f), this.getPosition().y + .55f, this.getRotation(), flip, false);
			batch.setColor(1f, 1f, 1f, 1f);
			batch.draw(Atlas.gunImg, gunPos.x, gunPos.y, .1f, .3f, gunRotation, false, flip);
		} else {
			batch.draw(Atlas.gunImg, gunPos.x, gunPos.y, .1f, .3f, gunRotation, false, flip);
			batch.draw(Atlas.playerImg, this.getPosition().x, this.getPosition().y, this.getRotation(), flip, false);
			batch.setColor(Color.valueOf("F4D841"));
			batch.draw(Atlas.maskImg, this.getPosition().x - (flip ? .25f : -.25f), this.getPosition().y + .55f, this.getRotation(), flip, false);
			batch.setColor(1f, 1f, 1f, 1f);
		}
	}
	
	@Override
	public Body getBody() {
		return body;
	}

}
