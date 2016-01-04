package io.cryptix.stardust.worlds;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.entities.Drawable;
import io.cryptix.stardust.entities.Entity;
import io.cryptix.stardust.entities.EntityComparator;
import io.cryptix.stardust.entities.PlayerEntity;
import io.cryptix.stardust.utils.Point;
import io.cryptix.stardust.utils.Util;

public abstract class GameWorld {
	
	private final World physicsWorld;
	private final PlayerEntity player;
	
	private int screenWidth, screenHeight;
	
	private Vector2 bottomLeft;
	
	private ArrayMap<Point, Chunk> activeChunks;
	private Array<Point> newActiveChunks;
	private Point playerGrid;
	private Point direction;
	private Grid[][] grids;
	
	private Array<Drawable> entities;
	private EntityComparator entityComparator;
		
	public GameWorld(int screenWidth, int screenHeight) {
		this.physicsWorld = new World(new Vector2(0, 0), true);
		this.player = new PlayerEntity(this, spawnLocation());
				
		this.activeChunks = new ArrayMap<Point, Chunk>(false, 9);
		this.newActiveChunks = new Array<Point>(false, 9);
		this.playerGrid = null;
		this.direction = null;
		
		this.entityComparator = new EntityComparator();
		this.entities = new Array<Drawable>();
				
		this.resize(screenWidth, screenHeight);
	}
	
	public PlayerEntity getPlayer() {
		return this.player;
	}
	
	public World getPhysicsWorld() {
		return this.physicsWorld;
	}
	
	public void resize(int viewWidth, int viewHeight) {
		this.screenWidth = viewWidth + 16;
		this.screenHeight = viewHeight + 16;
		if (this.bottomLeft != null)
			this.resizeGrids();
	}
	
	public Point getRealGrid(float x, float y) {
		Point point = new Point(((int)x/8), ((int)y/8));
		if (x < 0) point.x -= 1;
		if (y < 0) point.y -= 1;
		return point;
	}
	
	public Point realToRelative(float x, float y, int size) {
		Point point = new Point((int)(((double)x/size % 1)*size),
                				(int)(((double)y/size % 1)*size));
		if (x < 0) {
			if (!this.repeats()) return null;
			point.x = (size - 1) + point.x;
		}
		if (y < 0) {
			if (!this.repeats()) return null;
			point.y = (size - 1) + point.y;
		}
		return point;
	}
	public Point realToRelativeChunk(float x, float y) {
		return realToRelative(x, y, this.size());
	}	
	public Point realToRelativeGrid(float x, float y) {
		return realToRelative(x, y, 16);
	}
	
	private void resizeGrids() { }
	
	private void createGrids() {
		this.activeChunks.clear();
		this.grids = new Grid[screenWidth/8 + 6][screenHeight/8 + 6];
		for (int x = 0; x < this.grids.length; x++) {
			for (int y = 0; y < this.grids[x].length; y ++) {
				Point gridCenter = getRealGrid(this.bottomLeft.x + x*8, this.bottomLeft.y + y*8);
				gridCenter.x = gridCenter.x * 8 + 4;
				gridCenter.y = gridCenter.y * 8 + 4;
				Point relChunk = realToRelativeChunk((float)gridCenter.x / 128, (float)gridCenter.y / 128);
				if (relChunk == null)
					continue;
				if (this.activeChunks.containsKey(relChunk)) {
					this.grids[x][y] = this.activeChunks.get(relChunk).getGrid(gridCenter);
				} else {
					Chunk c = this.generateChunk(relChunk.x, relChunk.y);
					this.grids[x][y] = c.getGrid(gridCenter);
					this.activeChunks.put(relChunk, c);
				}
				
				if (x != 0 && x != this.grids.length - 1 && y != 0 && y != this.grids[x].length - 1) {
					this.grids[x][y].createPhysics();
				}
			}
		}
	}
	
	private void updateGrids() {
	}
	
	public void update() {
		
		if (this.playerGrid == null) {
			this.bottomLeft = new Vector2(this.player.getPosition().x - (float)screenWidth/2 - 16, this.player.getPosition().y - (float)screenHeight/2 - 16);
			this.playerGrid = getRealGrid(this.player.getPosition().x, this.player.getPosition().y);
			createGrids();
		}
		
		entities.clear();
		this.player.updatePhysics();
		this.player.update();
		entities.add(this.player);
		for (int x = 1; x < this.grids.length - 1; x++) {
			for (int y = 1; y < this.grids[x].length - 1; y ++) {
				if (this.grids[x][y] != null) {
					for (Entity e : this.grids[x][y].getEntities()) {
						if (e.bodyType() != BodyType.StaticBody) {
							Point oldGrid = getRealGrid(e.getPosition().x, e.getPosition().y);
							e.updatePhysics();
							e.update();
							Point newGrid = getRealGrid(e.getPosition().x, e.getPosition().y);
							if (!oldGrid.equals(newGrid)) {
								Point direction = new Point(newGrid.x - oldGrid.x, newGrid.y - oldGrid.y);
								this.grids[x][y].removeEntity(e);
								this.grids[x+direction.x][y+direction.y].addEntity(e);
								if (direction.x < 0 || direction.y < 0) entities.add(e);
							}
						} else {
							e.updatePhysics();
							e.update();
						}
					}
					entities.addAll(this.grids[x][y].getEntities());
				}
			}
		}
		entities.sort(entityComparator);
		
		this.bottomLeft = new Vector2(this.player.getPosition().x - (float)screenWidth/2 - 16, this.player.getPosition().y - (float)screenHeight/2 - 16);
		Point currentGrid  = getRealGrid(this.player.getPosition().x, this.player.getPosition().y);
		
		if (!this.playerGrid.equals(currentGrid)) {
			this.direction = new Point(currentGrid.x - this.playerGrid.x, currentGrid.y - this.playerGrid.y);
			this.playerGrid = currentGrid;
			updateGrids();
		}
		
	}
	
	public void render(GameRenderer batch, Matrix4 matrix) {
		// TODO: Add ground render.
		for (Drawable e : entities)
			e.render(batch);
	}
	
	public void debugRender(GameRenderer batch, Matrix4 matrix) {
		if (this.grids != null) {
			for (int x = 0; x < this.grids.length; x++) {
				for (int y = 0; y < this.grids[x].length; y ++) {
					if (this.grids[x][y] != null) {
						Util.DrawSquare(new Vector2(this.grids[x][y].getPosition().x - 4, this.grids[x][y].getPosition().y - 4),
								        new Vector2(this.grids[x][y].getPosition().x + 4, this.grids[x][y].getPosition().y + 4),
								        matrix);
					}
				}
			}
		}
	}
	
	public void destroy() { }
	
	public abstract Chunk generateChunk(int x, int y);
	
	public abstract Grid generateGrid(Point worldPosition);
	
	public abstract boolean repeats();
	
	public abstract int size();
	
	public abstract Vector2 spawnLocation();
}