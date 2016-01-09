package io.cryptix.stardust.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import io.cryptix.stardust.Atlas;
import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.worlds.GameWorld;

public class WeaponEntity extends Entity{
	
	private boolean flip;

	public WeaponEntity(GameWorld world, Vector2 position, float angle) {
		super(world, position, angle);
		// TODO Auto-generated constructor stub
	}
	
	public void setFlip(boolean newFlip){
		flip = newFlip;
	}

	@Override
	public BodyType bodyType() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public float width() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float height() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float mass() {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		batch.draw(Atlas.gunImg, getPosition().x, getPosition().y, .1f, .3f, getRotation(), false, flip);
	}

}
