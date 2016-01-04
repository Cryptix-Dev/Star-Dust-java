package io.cryptix.stardust.entities;

import javax.crypto.IllegalBlockSizeException;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.worlds.GameWorld;

public abstract class Entity implements Drawable {
	
	private final GameWorld world;
	
	private Vector2 position;
	private float angle;
	
	private Body body;
	private Fixture[] fixtures;
	
	private Vector2 linearVelocity;
	private float angularVelocity;
	
	public Entity(GameWorld world, Vector2 position, float angle) {
		this.world = world;
		this.position = position;
		this.angle = angle;
		
		this.linearVelocity = null;
		this.angularVelocity = 0f;
	}
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	public void setPosition(Vector2 position) {
		this.position = position;
		if (this.body != null)
			this.body.setTransform(this.position, this.body.getAngle());
	}
	
	public void setRotation(float newRot) {
		angle = newRot;
		if (this.body != null)
			this.body.setTransform(this.body.getPosition(), this.angle * MathUtils.degreesToRadians);
	}
	
	public void SetPositionAndRotation(Vector2 position, float rotation) {
		this.position = position;
		this.angle = rotation;
		if (this.body != null)
			this.body.setTransform(this.position, this.angle * MathUtils.degreesToRadians);
	}
	
	public float getRotation() {
		return angle;
	}
	
	public GameWorld getWorld() {
		return this.world;
	}
	
	public void storePhysics() {
		this.linearVelocity = this.body.getLinearVelocity();
		this.angularVelocity = this.body.getAngularVelocity();
	}
	
	public void createBody() {
		this.body = this.world.getPhysicsWorld().createBody(this.bodyDef());
		this.body.setUserData(this);
		this.createFixtures();
	}
	
	public void createFixtures() {
		Shape[] shapes = this.fixtureShapes();
		if (shapes == null) {
			shapes = new Shape[1];
			shapes[0] = new PolygonShape();
			((PolygonShape)shapes[0]).setAsBox(this.width()/2, this.height()/2);
		}
		FixtureDef[] fixtureDefs = this.fixtureDefs();
		if (fixtureDefs == null) {
			fixtureDefs = new FixtureDef[1];
			fixtureDefs[0] = new FixtureDef();
			fixtureDefs[0].density = this.mass();
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
		this.storePhysics();
		this.world.getPhysicsWorld().destroyBody(this.body);
		this.body = null;
	}
	
	public FixtureDef[] fixtureDefs() {
		return null;
	}
	
	public BodyDef bodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = this.bodyType();
		bodyDef.position.set(getPosition());
		bodyDef.angle = getRotation() * MathUtils.degreesToRadians;
		bodyDef.linearDamping = 10f;
		bodyDef.angularDamping = 10f;
		bodyDef.fixedRotation = true;
		
		if (this.linearVelocity != null) {
			bodyDef.linearVelocity.set(this.linearVelocity);
			bodyDef.angularVelocity = this.angularVelocity;

			this.linearVelocity = null;
		}
		
		
		this.bodyDefSettings(bodyDef);
		return bodyDef;
	}
	
	public void bodyDefSettings(BodyDef bodyDef) {
		
	}
	
	public Shape[] fixtureShapes() {
		return null;
	}
	
	@Override
	public float drawPoint() {
		return this.getPosition().y;
	}
	
	public void updatePhysics() {
		if (this.body != null) {
			this.position = this.body.getPosition();
			this.angle = this.body.getAngle() * MathUtils.radiansToDegrees;
		}
	}
	
	public abstract BodyType bodyType();
	
	public abstract float width();
	
	public abstract float height();
	
	public abstract float mass();
		
	public abstract void destroy();
	
	public abstract void update();
	
	@Override
	public abstract void render(GameRenderer batch);
}
