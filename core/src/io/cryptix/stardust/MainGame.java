package io.cryptix.stardust;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainGame extends ApplicationAdapter {
	SpriteBatch batch;
	Sprite img;
	
	private Viewport viewport;
	private Camera camera;
	private World world;
	
	private Box2DDebugRenderer debugRenderer;
	public float TIMESTEP = 1/45f;
	
	private Body body;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Sprite(new Texture("badlogic.jpg"));
		
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		
		world = new World(new Vector2(0, 0), true);
		
		debugRenderer = new Box2DDebugRenderer();
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, 0);
		
		body = world.createBody(bodyDef);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(6f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;
		
		body.createFixture(fixtureDef);
		
		circle.dispose();
	}
	
	private float accumulator = 0;
	private void doPhysicsStep(float deltaTime) {
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= TIMESTEP) {
			world.step(TIMESTEP, 6, 2);
			accumulator -= TIMESTEP;
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Vector2 moveVelocity = new Vector2();
		if (Gdx.input.isKeyPressed(Keys.A)) {
			moveVelocity.x += -50f;
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			moveVelocity.y += 50f;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			moveVelocity.x += 50f;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			moveVelocity.y += -50f;
		}
		body.setLinearVelocity(moveVelocity);
		
		camera.update();
		
		debugRenderer.render(world, camera.combined);
		doPhysicsStep(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
