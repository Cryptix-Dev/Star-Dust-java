package io.cryptix.stardust.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import io.cryptix.stardust.Atlas;
import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.utils.Util;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PlayerEntity extends Entity {

	private Vector2 gunPos = new Vector2();
	private float gunRotation = 0;
	private boolean flip = false;
	
	private float velocity = 10f;
	private Vector2 direction = new Vector2();
	
	private Animation walkAnimation;
	private float stateTime;
	private TextureRegion currentFrame;
	
	//private Color maskColor = Color.valueOf("F4D841");
	private Color maskColor = Color.valueOf("DB4242");
	
	public PlayerEntity(World world, Vector2 position) {
		super(world, position, 0);
		walkAnimation = new Animation(0.05f, Atlas.walkFrames);
		stateTime = 0f;
	}
	
	@Override
	public BodyDef bodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type  = BodyType.DynamicBody;
		bodyDef.position.set(this.getPosition());
		bodyDef.angle = 0;
		bodyDef.fixedRotation = true;
		return bodyDef;
	}

	@Override
	public FixtureDef[] fixtureDefs() {
		FixtureDef[] output = new FixtureDef[1];
		output[0] = new FixtureDef();
		output[0].density = 40f;
		return output;
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
	public void initialize() { }

	@Override
	public void destroy() { }

	@Override
	public void update() {
		gunPos = new Vector2(getPosition().x + .9f, getPosition().y - .3f);
		if (gunRotation >= 90 && gunRotation <= 270) flip = true; else flip = false;
		
		if (Math.abs(direction.x) != Math.abs(direction.y) || (direction.x == 0 && direction.y == 0)) {
			this.getBody().setLinearVelocity(direction.x * velocity, direction.y * velocity);
		} else {
			Vector2 temp = new Vector2(direction.x, direction.y).clamp(-1, 1);
			this.getBody().setLinearVelocity(temp.x * velocity, temp.y * velocity);
		}
	}

	@Override
	public void render(GameRenderer batch) {
		if (gunRotation < 25 || gunRotation > 155) {
			batch.draw(Atlas.playerImg, this.getPosition().x, this.getPosition().y, this.getRotation(), flip, false);
			batch.setColor(maskColor);
			batch.draw(Atlas.maskImg, this.getPosition().x - (flip ? .25f : -.25f), this.getPosition().y + .55f, this.getRotation(), flip, false);
			batch.setColor(1f, 1f, 1f, 1f);
			batch.draw(Atlas.gunImg, gunPos.x, gunPos.y, .1f, .3f, gunRotation, false, flip);
		} else {
			batch.draw(Atlas.gunImg, gunPos.x, gunPos.y, .1f, .3f, gunRotation, false, flip);
			batch.draw(Atlas.playerImg, this.getPosition().x, this.getPosition().y, this.getRotation(), flip, false);
			batch.setColor(maskColor);
			batch.draw(Atlas.maskImg, this.getPosition().x - (flip ? .25f : -.25f), this.getPosition().y + .55f, this.getRotation(), flip, false);
			batch.setColor(1f, 1f, 1f, 1f);
		}
		/*stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		batch.draw(currentFrame, getPosition().x, getPosition().y, getRotation());
		*/
	}
	
	@Override
	public float drawPoint() {
		return this.getPosition().y - Util.convertToMeters(Atlas.playerImg.getHeight())/2;
	}
	
	@Override
	public float width() {
		return Util.convertToMeters(Atlas.playerImg.getWidth() - 2);
	}

	@Override
	public float height() {
		return Util.convertToMeters(Atlas.playerImg.getHeight() - 1);
	}
}
