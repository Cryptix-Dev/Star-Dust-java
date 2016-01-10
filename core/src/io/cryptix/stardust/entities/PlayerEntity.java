package io.cryptix.stardust.entities;

import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

import io.cryptix.stardust.Atlas;
import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.utils.Util;
import io.cryptix.stardust.weapons.Weapon;
import io.cryptix.stardust.worlds.GameWorld;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PlayerEntity extends Entity {

	private boolean flip = false;
	
	private float velocity = 10f;
	private Vector2 direction = new Vector2();
	
	private Weapon heldWeapon;
	
	//private Animation walkAnimation;
	//private float stateTime;
	//private TextureRegion currentFrame;
	
	//private Color maskColor = Color.valueOf("F4D841");
	private Color maskColor = Color.valueOf("DB4242");
	
	public PlayerEntity(GameWorld world, Vector2 position) {
		super(world, position, 0);
		//walkAnimation = new Animation(0.05f, Atlas.walkFrames);
		//stateTime = 0f;
		heldWeapon = new Weapon(world, this.getPosition(), 0);
		this.createBody();
	}
	
	@Override
	public BodyType bodyType() {
		return BodyType.DynamicBody;
	}
	
	@Override
	public Shape[] fixtureShapes() {
		PolygonShape[] shapes = new PolygonShape[2];
		shapes[0] = new PolygonShape();
		shapes[0].setAsBox(this.width()/2, this.height()/2);
		
		shapes[1] = new PolygonShape();
		shapes[1].setAsBox(this.width()/2.5f, .5f, new Vector2(0, -1), 0);
		return shapes;
	}
	
	@Override
	public FixtureDef[] fixtureDefs() {
		FixtureDef[] fixtures = new FixtureDef[2];
		fixtures[0] = new FixtureDef();
		fixtures[0].isSensor = true;
		fixtures[0].density = this.mass();
		
		fixtures[1] = new FixtureDef();
		return fixtures;
	}
	
	public void calculateGunRotation(Vector2 mouse) {
		if(heldWeapon != null){
			heldWeapon.setRotation((float) MathUtils.atan2(mouse.y - (heldWeapon.getPosition().y + .2f), mouse.x - (heldWeapon.getPosition().x - .9f)) * MathUtils.radiansToDegrees);
			if ((heldWeapon.getRotation()) < 0) heldWeapon.setRotation(heldWeapon.getRotation() + 360);
		}
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
	public void destroy() { }

	@Override
	public void update() {
		if(heldWeapon != null){
			heldWeapon.setPosition(new Vector2(getPosition().x + .9f, getPosition().y - .3f));
			if (heldWeapon.getRotation() >= 90 && heldWeapon.getRotation() <= 270){
				heldWeapon.setFlip(true);
			}else heldWeapon.setFlip(false);
		}
		
		if (Math.abs(direction.x) != Math.abs(direction.y) || (direction.x == 0 && direction.y == 0)) {
			this.getBody().setLinearVelocity(direction.x * velocity, direction.y * velocity);
		} else {
			Vector2 temp = new Vector2(direction.x, direction.y).clamp(-1, 1);
			this.getBody().setLinearVelocity(temp.x * velocity, temp.y * velocity);
		}
	}

	@Override
	public void render(GameRenderer batch) {
		if (heldWeapon.getRotation() < 25 || heldWeapon.getRotation() > 155) {
			batch.draw(Atlas.playerImg, this.getPosition().x, this.getPosition().y, this.getRotation(), flip, false);
			batch.setColor(maskColor);
			batch.draw(Atlas.maskImg, this.getPosition().x - (flip ? .25f : -.25f), this.getPosition().y + .55f, this.getRotation(), flip, false);
			batch.setColor(1f, 1f, 1f, 1f);
			heldWeapon.render(batch);
		} else {
			heldWeapon.render(batch);
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
	
	@Override
	public float mass() {
		return 1f;
	}
}
