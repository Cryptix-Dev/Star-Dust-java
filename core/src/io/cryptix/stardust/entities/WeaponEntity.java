package io.cryptix.stardust.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import io.cryptix.stardust.Atlas;
import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.worlds.GameWorld;

public class WeaponEntity extends Entity{
	
	private boolean flip;
	private boolean held = false;

	public WeaponEntity(GameWorld world, Vector2 position, float angle) {
		super(world, position, angle);
	}
	
	public void setHeld(boolean heldBool){
		held = heldBool;
	}
	
	public void setFlip(boolean newFlip){
		flip = newFlip;
	}

	@Override
	public BodyType bodyType() {
		if(held == true)return null; else return BodyType.DynamicBody;
	}
	

	@Override
	public float width() {
		if(held == true)return 0; else return 1.7f;
	}

	@Override
	public float height() {
		if(held == true)return 0; else return 0.3f;
	}

	@Override
	public float mass() {
		if(held == true)return 0; else return 10;
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
		if(held == true){
			batch.draw(Atlas.gunImg, getPosition().x, getPosition().y, .1f, .3f, getRotation(), false, flip);
		}else{
			batch.draw(Atlas.gunImg, getPosition().x, getPosition().y);
		}
	}
}
