package io.cryptix.stardust.worlds;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class WorldManager {
	
	private GameWorld activeWorld;
	private int screenWidth, screenHeight;
	
	public WorldManager(Vector2 viewportSize) {
		this.screenWidth = (int) viewportSize.x;
		this.screenHeight = (int) viewportSize.y;
		// TODO: Read Game file to see current gameData.
		setActiveWorld(0); 
	}
	
	public void setActiveWorld(int index) {
		if (activeWorld != null) {
			// TODO: dispose of current world.
		}
		
		if (loadWorld(index) == null) {
			this.activeWorld = createWorld(index);
		}
	}
	
	private GameWorld loadWorld(int index) {
		// TODO: NBT loading.
		return null;
	}
	
	public GameWorld createWorld(int index) {
		// TODO: Create base NBT and settings.
		return new TestGameWorld(screenWidth, screenWidth);
	}
	
	public GameWorld getActiveWorld() {
		return activeWorld;
	}
	
	public World getPhysicsWorld() {
		return activeWorld.getPhysicsWorld();
	}
	
	public void resize(float viewWidth, float viewHeight) {
		this.screenWidth = (int) viewWidth;
		this.screenHeight = (int) viewHeight;
		activeWorld.resize(screenWidth, screenHeight);
	}
}
