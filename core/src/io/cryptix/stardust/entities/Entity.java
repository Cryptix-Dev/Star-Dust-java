package io.cryptix.stardust.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.cryptix.stardust.GameRenderer;

public abstract class Entity {
	
	private Vector2 position;
	private float angle;
	public Entity(World world, Vector2 position, float angle) {
		this.position = position;
		this.angle = angle;
	}
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	public void setRotation(float newRot) {
		angle = newRot;
	}
	
	public float getRotation() {
		return angle;
	}
	
	public abstract float drawPoint();
	
	public abstract Body getBody();
	
	public abstract void update();
	
	public abstract void render(GameRenderer batch);
}
