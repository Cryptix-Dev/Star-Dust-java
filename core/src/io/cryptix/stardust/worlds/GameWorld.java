package io.cryptix.stardust.worlds;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import io.cryptix.stardust.GameRenderer;
import io.cryptix.stardust.entities.Entity;
import io.cryptix.stardust.entities.EntityComparator;
import io.cryptix.stardust.utils.Point;
import io.cryptix.stardust.utils.Util;

public abstract class GameWorld {
	
	private final World physicsWorld;
	private int screenWidth, screenHeight;
	
	private Vector2 bottomLeft;
	
	private ArrayMap<Point, Chunk> activeChunks;
	private Array<Point> newActiveChunks;
	private Point playerGrid;
	private Point direction;
	private Grid[][] grids;
	
	private Array<Entity> entities;
	private EntityComparator entityComparator;
		
	public GameWorld(int screenWidth, int screenHeight) {
		this.physicsWorld = new World(new Vector2(0, 0), true);
		this.resize(screenWidth, screenHeight);
		
		this.activeChunks = new ArrayMap<Point, Chunk>(false, 9);
		this.newActiveChunks = new Array<Point>(false, 9);
		this.playerGrid = null;
		this.direction = null;
		
		this.entityComparator = new EntityComparator();
		this.entities = new Array<Entity>();
	}
	
	public World getPhysicsWorld() {
		return this.physicsWorld;
	}
	
	public void resize(int viewWidth, int viewHeight) {
		this.screenWidth = viewWidth + 16;
		this.screenHeight = viewHeight + 16;
		
		if (this.grids != null) {
			for (int x = 0; x < this.grids.length; x++) {
				for (int y = 0; y < this.grids[x].length; y ++) {
					if (this.grids[x][y] != null)
						this.grids[x][y].destroy();
				}
			}
		}
		
		this.grids = new Grid[screenWidth/8 + 6][screenHeight/8 + 6];
		this.playerGrid = null;
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
	
	private void createGrids(Vector2 playerPosition) {
		this.activeChunks.clear();
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
				
				if (x != 0 && x != this.grids.length - 1 && y != 0 && y != this.grids[x].length - 1 &&
					x != 1 && x != this.grids.length - 2 && y != 1 && y != this.grids[x].length - 2) {
					this.grids[x][y].createPhysics();
				}
			}
		}
	}
	
	private void updateGrids(Vector2 playerPosition) {
		if (this.direction == null) {
			createGrids(playerPosition);
			return;
		}
		
		
	}
	
	public void update(Vector2 playerPosition) {
		Point currentGrid  = getRealGrid(playerPosition.x, playerPosition.y);
		this.bottomLeft = new Vector2(playerPosition.x - (float)screenWidth/2 - 16, playerPosition.y - (float)screenHeight/2 - 16);
		
		if (this.playerGrid == null || !this.playerGrid.equals(currentGrid)) {
			if (this.playerGrid == null) {
				this.playerGrid = currentGrid;
				this.direction = null;
			} else {
				this.direction = new Point(currentGrid.x - this.playerGrid.x, currentGrid.y - this.playerGrid.y);
				this.playerGrid = currentGrid;
			}
			updateGrids(playerPosition);
		}
		
		entities.clear();
		for (int x = 2; x < this.grids.length - 2; x++) {
			for (int y = 2; y < this.grids[x].length - 2; y ++) {
				if (this.grids[x][y] != null && this.grids[x][y].getEntities().length != 0) {
					for (Entity e : this.grids[x][y].getEntities()) {
						Point oldGrid = getRealGrid(e.getPosition().x, e.getPosition().y);
						e.updatePhysics();
						e.update();
						Point newGrid = getRealGrid(e.getPosition().x, e.getPosition().y);
						if (!oldGrid.equals(newGrid)) {
							Point direction = new Point(newGrid.x - oldGrid.x, newGrid.y - oldGrid.y);
							this.grids[x][y].removeEntity(e);
							if (x+direction.x > this.grids.length - 1 || y+direction.y > this.grids[x].length - 1 || 
								this.grids[x+direction.x][y+direction.y] == null) {
								e.destroyBody();
								e.destroy();
							} else {
								this.grids[x+direction.x][y+direction.y].addEntity(e);
								if (direction.x < 0 || direction.y < 0) entities.add(e);
							}
						}
					}
					entities.addAll(this.grids[x][y].getEntities());
				}
			}
		}
		entities.sort(entityComparator);
	}
	
	public void render(GameRenderer batch, Matrix4 matrix) {
		// TODO: Add ground render.
		for (int x = 0; x < this.grids.length; x++) {
			for (int y = 0; y < this.grids[x].length; y ++) {
				if (this.grids[x][y] != null) {
					Util.DrawSquare(new Vector2(this.grids[x][y].getPosition().x - 4, this.grids[x][y].getPosition().y - 4),
							        new Vector2(this.grids[x][y].getPosition().x + 4, this.grids[x][y].getPosition().y + 4),
							        matrix);
				}
			}
		}
		batch.setProjectionMatrix(matrix);
		batch.begin();
		for (Entity e : entities)
			e.render(batch);
		batch.end();
	}
	
	public void destroy() { }
	
	public abstract Chunk generateChunk(int x, int y);
	
	public abstract Grid generateGrid(Point worldPosition);
	
	public abstract boolean repeats();
	
	public abstract int size();
}