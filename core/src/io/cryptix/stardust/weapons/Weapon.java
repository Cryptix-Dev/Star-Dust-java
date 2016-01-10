package io.cryptix.stardust.weapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import io.cryptix.stardust.Atlas;
import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.entities.Entity;
import io.cryptix.stardust.worlds.GameWorld;

public class Weapon extends Entity{
	
	private boolean flip;
	
	public Weapon(GameWorld world, Vector2 position, float angle) {
		super(world, position, angle);
	}
	
	public void setFlip(boolean newFlip){
		flip = newFlip;
	}

	@Override
	public BodyType bodyType() {
		return BodyType.DynamicBody;
	}
	

	@Override
	public float width() {
		return 1.7f;
	}

	@Override
	public float height() {
		return 0.3f;
	}

	@Override
	public float mass() {
		return 10;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameRenderer batch) {
		batch.draw(Atlas.gunImg, getPosition().x, getPosition().y, .1f, .3f, getRotation(), false, flip);
	}
}
