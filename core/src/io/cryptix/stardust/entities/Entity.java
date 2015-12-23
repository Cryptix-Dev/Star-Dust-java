package io.cryptix.stardust.entities;

import javax.crypto.IllegalBlockSizeException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import io.cryptix.stardust.GameRenderer;

public abstract class Entity {
	
	private final World world;
	
	private Vector2 position;
	private float angle;
	
	private Body body;
	private Fixture[] fixtures;
	
	public Entity(World world, Vector2 position, float angle) {
		this.world = world;
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
	
	public World getWorld() {
		return this.world;
	}
	
	public void createBody() {
		this.body = this.world.createBody(this.bodyDef());
		this.body.setUserData(this);
		this.createFixtures();
	}
	
	public void createFixtures() {
		FixtureDef[] fixtureDefs = this.fixtureDefs();
		Shape[] shapes = this.fixtureShapes();
		if (shapes == null) {
			shapes = new Shape[1];
			shapes[0] = new PolygonShape();
			((PolygonShape)shapes[0]).setAsBox(width()/2, height()/2);
		}
		if (fixtureDefs.length != shapes.length && shapes != null)
			new IllegalBlockSizeException("FixtureShapes must be same size as FixtureDefs");
		
		fixtures = new Fixture[fixtureDefs.length];
		for (int i = 0; i < fixtureDefs.length; i++) {
			fixtureDefs[i].shape = shapes[i];
			fixtures[i] = this.body.createFixture(fixtureDefs[i]);
			shapes[i].dispose();
		}
	}
	
	public Body getBody() {
		return this.body;
	}
	
	public Fixture[] getFixtures() {
		return fixtures;
	}
	
	public void destoryFixture(int index) {
		this.body.destroyFixture(fixtures[index]);
	}
	
	public void destroyAllFixtures() {
		for (Fixture f : fixtures)
			this.body.destroyFixture(f);
	}
	
	public void destroyBody() {
		this.world.destroyBody(this.body);
		this.body = null;
	}
	
	public Shape[] fixtureShapes() {
		return null;
	}
	
	public abstract BodyDef bodyDef();
	
	public abstract FixtureDef[] fixtureDefs();
	
	public abstract float width();
	
	public abstract float height();
	
	public abstract float drawPoint();
	
	public abstract void initialize();
	
	public abstract void destroy();
	
	public abstract void update();
	
	public abstract void render(GameRenderer batch);
}
