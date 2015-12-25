package io.cryptix.stardust.worlds;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.cryptix.stardust.entities.Entity;
import io.cryptix.stardust.utils.Point;

public class Grid {

	private final Point center;
	
	private Array<Entity> entities;
	
	private boolean physicsEnabled;
	private boolean isSleeping;
	
	public Grid(Point center) {
		this.center = center;
		this.entities = new Array<Entity>();
		this.physicsEnabled = false;
		this.isSleeping = false;
	}
	
	public void createPhysics() {
		for (Entity e : entities) {
			if (e.getBody() == null)
				e.createBody();
		}
		this.physicsEnabled = true;
	}
	
	public void destroyPhysics() {
		for (Entity e : entities) {
			if (e.getBody() != null)
				e.destroyBody();
		}
		this.physicsEnabled = false;
	}
	
	public void sleep() {
		if (this.physicsEnabled) {
			for (Entity e : entities) {
				e.getBody().setAwake(this.isSleeping);
				e.getBody().setActive(false);
			}
			this.isSleeping = !this.isSleeping;
		}
	}
	
	public boolean isPhysicsEnabled() {
		return this.physicsEnabled;
	}
	
	public Point getPosition() {
		return this.center;
	}
	
	public void placeEntity(Entity e, Vector2 relativePosition) {
		e.setPosition(new Vector2(center.x + relativePosition.x, center.y + relativePosition.y));
		this.addEntity(e);
	}
	
	public void addEntity(Entity e) {
		if (this.physicsEnabled && e.getBody() == null)
			e.createBody();
		else if (!this.physicsEnabled && e.getBody() != null)
			e.destroyBody();
		entities.add(e);
	}
	
	public void removeEntity(Entity e) {
		entities.removeValue(e, true);
	}
	
	public Entity[] getEntities() {
		if (entities.size != 0)
			return this.entities.toArray(Entity.class);
		return new Entity[0];
	}
	
	public void destroy() {
		if (this.physicsEnabled) {
			for (Entity e : entities) {
				e.destroyBody();
				e.destroy();
				e = null;
			}
		} else {
			for (Entity e : entities) {
				e.destroy();
				e = null;
			}
		}
		entities.clear();
	}
}
