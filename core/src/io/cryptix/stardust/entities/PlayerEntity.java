package io.cryptix.stardust.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.Util;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PlayerEntity extends Entity {
	
	private Body body;
	private Texture playerImg;
	
	private Texture gunImg;
	private Vector2 gunPos = new Vector2();
	private float gunRotation = 0;
	private boolean flip = false;
	
	private Vector2 lastMoveVel = Vector2.Zero;
	private Vector2 direction = new Vector2();
	private float velocity = 10f;
	
	public PlayerEntity(World world, Vector2 position) {
		super(world, position, 0);
		
		playerImg = new Texture("main_character_blue_1.png");
		gunImg = new Texture("gun.png");
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type  = BodyType.DynamicBody;
		bodyDef.position.set(position.x, position.y);
		bodyDef.angle = 0;
		bodyDef.fixedRotation = true;
		body = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Util.convertToMeters(playerImg.getWidth())/2,
					   Util.convertToMeters(playerImg.getHeight())/2);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = 40f;
		body.createFixture(fixture);
		shape.dispose();
		
		body.setUserData(this);
		
	}
	
	public void calculateGunRotation(Vector2 mouse) {
		gunRotation = (float) MathUtils.atan2(mouse.y - (gunPos.y), mouse.x - (gunPos.x)) * MathUtils.radiansToDegrees;
		if (gunRotation < 0) gunRotation += 360;
	}
	
	public void movePlayer(float x, float y) {
		direction.add(x, y);
	}

	@Override
	public void update() {
		gunPos = new Vector2(getPosition().x + .9f, getPosition().y);
		if (gunRotation >= 90 && gunRotation <= 270) flip = true; else flip = false;
		
		Vector2 moveVel = new Vector2(direction.x * velocity, direction.y * velocity); 
		body.setLinearVelocity(body.getLinearVelocity().add(moveVel).mulAdd(lastMoveVel, -1));
		lastMoveVel = moveVel;
	}

	@Override
	public void render(GameRenderer batch) {
		if (gunRotation < 25 || gunRotation > 125) {
			batch.draw(playerImg, this.getPosition().x, this.getPosition().y, this.getRotation(), flip, false);
			batch.draw(gunImg, gunPos.x, gunPos.y, .1f, .1f, gunRotation, false, flip);
		} else {
			batch.draw(gunImg, gunPos.x, gunPos.y, .1f, .1f, gunRotation, false, flip);
			batch.draw(playerImg, this.getPosition().x, this.getPosition().y, this.getRotation(), flip, false);
		}
	}
	
	@Override
	public Body getBody() {
		return body;
	}

}
