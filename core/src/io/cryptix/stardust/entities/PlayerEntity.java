package io.cryptix.stardust.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
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
	
	private float velocity = 10f;
	private Vector2 direction = new Vector2();
	
	private Animation walkAnimation;
	private float stateTime;
	private TextureRegion currentFrame;
	
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
		
		walkAnimation = new Animation(0.05f, Atlas.walkFrames);
		stateTime = 0f;
	}
	
	public void calculateGunRotation(Vector2 mouse) {
		gunRotation = (float) MathUtils.atan2(mouse.y - (gunPos.y + .2f), mouse.x - (gunPos.x - .9f)) * MathUtils.radiansToDegrees;
		if (gunRotation < 0) gunRotation += 360;
	}
	
	public void movePlayer(float x, float y) {
		if (x == 0 && y == 0) {
			direction = new Vector2(0, 0);		
		} else {
			direction.add(x, y);
			if (direction.x != 0)
				direction.x /= Math.abs(direction.x);
			if (direction.y != 0)
				direction.y /= Math.abs(direction.y);
		}
	}

	@Override
	public void update() {
		gunPos = new Vector2(getPosition().x + .9f, getPosition().y - .3f);
		if (gunRotation >= 90 && gunRotation <= 270) flip = true; else flip = false;
		
		if (Math.abs(direction.x) != Math.abs(direction.y) || (direction.x == 0 && direction.y == 0)) {
			body.setLinearVelocity(direction.x * velocity, direction.y * velocity);
		} else {
			Vector2 temp = new Vector2(direction.x, direction.y).clamp(-1, 1);
			body.setLinearVelocity(temp.x * velocity, temp.y * velocity);
		}
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
		/*stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		batch.draw(currentFrame, getPosition().x, getPosition().y, getRotation());
		*/
	}
	
	@Override
	public Body getBody() {
		return body;
	}

}
