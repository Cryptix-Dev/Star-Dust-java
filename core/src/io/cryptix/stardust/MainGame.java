package io.cryptix.stardust;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import io.cryptix.stardust.entities.BoxEntity;
import io.cryptix.stardust.entities.Entity;
import io.cryptix.stardust.entities.PlayerEntity;

public class MainGame extends ApplicationAdapter {
	private GameRenderer batch;
	
	public PlayerCamera camera;
	private World world;
	
	private Box2DDebugRenderer debugRenderer;
	public float TIMESTEP = 1/300f;
	
	public PlayerEntity player;
	private BoxEntity box;
	
	// TODO: Replace with smarter body management system. One for terrain, one for entities.
	private Array<Body> bodies;
	
	@Override
	public void create () {
		new Atlas();
		
		world = new World(new Vector2(0, 0f), true);
		bodies = new Array<Body>();
		debugRenderer = new Box2DDebugRenderer();
		
		batch = new GameRenderer();	
		camera = new PlayerCamera();
		Gdx.input.setInputProcessor(new PlayerInput(this));
		
		player = new PlayerEntity(world, new Vector2(0, 0));
		box = new BoxEntity(world, new Vector2(-4f, 0), 30f);
	}
	
	public void update() {
		((PlayerInput)Gdx.input.getInputProcessor()).update();
		
		world.getBodies(bodies);
		for (Body b : bodies) {
			Entity e = (Entity)b.getUserData();
			
			if (e != null) {
				e.setPosition(b.getPosition());
				e.setRotation(MathUtils.radiansToDegrees * b.getAngle());
				e.update();
			}
		}
		
		doPhysicsStep(Gdx.graphics.getDeltaTime());
		camera.setTarget(player.getPosition());
		camera.update();
	}
	
	@Override
	public void render () {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.getProjViewMatrix());
		batch.begin();
		player.render(batch);
		box.render(batch);
		batch.end();
		
		debugRenderer.render(world, camera.getProjViewMatrix());
		update();
	}
	
	@Override
	public void resize(int width, int height) {
		camera.resize(width, height);
		
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
}
