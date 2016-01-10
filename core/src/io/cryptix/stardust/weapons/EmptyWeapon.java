package io.cryptix.stardust.weapons;

import com.badlogic.gdx.math.Vector2;

import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.worlds.GameWorld;

public class EmptyWeapon extends Weapon{

	public EmptyWeapon(GameWorld world, Vector2 position, float angle) {
		super(world, position, angle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void render(GameRenderer batch){
		
	}

}
