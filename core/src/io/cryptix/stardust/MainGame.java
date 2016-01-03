package io.cryptix.stardust;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import io.cryptix.stardust.worlds.WorldManager;

public class MainGame extends ApplicationAdapter {
	private GameRenderer batch;
		
	private Box2DDebugRenderer debugRenderer;
	public float TIMESTEP = 1/300f;
		
	private GameCamera camera;
	private WorldManager worldManager;
	
	@Override
	public void create () {
		new Atlas();
		
		batch = new GameRenderer();
		camera = new GameCamera();
		
		worldManager = new WorldManager(camera.getViewportSize());
		
		Gdx.input.setInputProcessor(new PlayerInput(worldManager, camera));
		
		debugRenderer = new Box2DDebugRenderer();

	}
	
	public void update() {
		((PlayerInput)Gdx.input.getInputProcessor()).update();

		worldManager.getActiveWorld().update();
		
		camera.setTarget(worldManager.getActiveWorld().getPlayer().getPosition());
		camera.update();
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.getProjViewMatrix());
		batch.begin();
		worldManager.getActiveWorld().render(batch, camera.getProjViewMatrix());
		batch.end();
		
		worldManager.getActiveWorld().debugRender(batch, camera.getProjViewMatrix());
		debugRenderer.render(worldManager.getPhysicsWorld(), camera.getProjViewMatrix());
		
		doPhysicsStep(Gdx.graphics.getDeltaTime());		
	}
	
	@Override
	public void resize(int width, int height) {
		camera.resize(width, height);
		worldManager.resize(camera.getViewportSize().x, camera.getViewportSize().y);
	}
	
	private float accumulator = 0;
	private void doPhysicsStep(float deltaTime) {
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= TIMESTEP) {
			worldManager.getPhysicsWorld().step(TIMESTEP, 6, 2);
			update();
			accumulator -= TIMESTEP;
		}
	}
}
