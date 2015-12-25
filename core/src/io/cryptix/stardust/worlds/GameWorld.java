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
	
	//private Vector2[] screenCorners;
	private Vector2 bottomLeft;
	
	private ArrayMap<Point, Chunk> activeChunks;
	private Array<Point> newActiveChunks;
	private Point playerGrid;
	private Point direction;
	private Grid[][] grids;
	
	private Array<Entity> entities;
	private EntityComparator entityComparator;
	
	//private boolean once = false;
	
	public GameWorld(int screenWidth, int screenHeight) {
		this.physicsWorld = new World(new Vector2(0, 0), true);
		this.resize(screenWidth, screenHeight);

		//this.screenCorners = new Vector2[4];
		
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
	
	/*int[] xOffsets = {-1,  0,  1,  1,  1,  0, -1, -1};
	int[] yOffsets = {-1, -1, -1,  0,  1,  1,  1,  0};
	private void updateChunks(Vector2 playerPosition) {
		newActiveChunks.clear();
		Point playerChunk = realToRelativeChunk(playerPosition.x / 128, playerPosition.y / 128);
		if (playerChunk != null)
			newActiveChunks.add(playerChunk);
		
		for (int i = 0; i < 4; i++) {
			Point relativeGrid = realToRelativeGrid(screenCorners[i].x / 8, screenCorners[i].y / 8);
			if (relativeGrid == null)
				continue;
			
			Point cornerChunk = realToRelativeChunk(screenCorners[i].x / 128, screenCorners[i].y / 128);
			if (cornerChunk == null)
				continue;
			if (!newActiveChunks.contains(cornerChunk, false))
				newActiveChunks.add(cornerChunk);
			
			for (int j = 0; j < 8; j++) {
				Point dist = new Point(((xOffsets[j] * 16 + 8) - relativeGrid.x)*((xOffsets[j] * 16 + 8) - relativeGrid.x),
						   			  ((yOffsets[j] * 16 + 8) - relativeGrid.y)*((yOffsets[j] * 16 + 8) - relativeGrid.y));
				if ((xOffsets[j] != 0 && yOffsets[j] == 0 && dist.x <= 100) ||
					(yOffsets[j] != 0 && xOffsets[j] == 0 &&dist.y <= 100) ||
					(xOffsets[j] != 0 && yOffsets[j] != 0 && (dist.x + dist.y) <= 100)) {
					Point offsetChunk = realToRelativeChunk((screenCorners[i].x / 128) + xOffsets[j], (screenCorners[i].y / 128) + yOffsets[j]);
					if (offsetChunk == null)
						continue;
					if (!newActiveChunks.contains(offsetChunk, false))
						newActiveChunks.add(offsetChunk);
				}
			}
		}
		
		Iterator<Entry<Point,Chunk>> itr = this.activeChunks.iterator();
		while (itr.hasNext()) {
			Entry<Point,Chunk> item = itr.next();
			int index = newActiveChunks.indexOf(item.key, false);
			if (index == -1) {
				item.value.destroy();
				itr.remove();
			} else {
				newActiveChunks.removeIndex(index);
			}
		}

		for (Point p : newActiveChunks)
			activeChunks.put(p, this.generateChunk(p.x, p.y));
	}
	*/
	
	private void createGrids(Vector2 playerPosition) {
		this.activeChunks.clear();
		for (int x = 0; x < this.grids.length; x++) {
			for (int y = 0; y < this.grids[x].length; y ++) {
				Point gridCenter = getRealGrid(this.bottomLeft.x + x*8, this.bottomLeft.y + y*8);
				gridCenter.x = gridCenter.x * 8 + 4;
				gridCenter.y = gridCenter.y * 8 + 4;
				Point relChunk = realToRelativeChunk((float)gridCenter.x / 128, (float)gridCenter.y / 128);
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
		
		if (this.playerGrid == null || !this.playerGrid.equals(currentGrid)) {
			if (this.playerGrid == null) {
				this.playerGrid = currentGrid;
				this.direction = null;
			} else {
				this.direction = new Point(currentGrid.x - this.playerGrid.x, currentGrid.y - this.playerGrid.y);
				this.playerGrid = currentGrid;
			}
			this.bottomLeft = new Vector2(playerPosition.x - (float)screenWidth/2 - 16, playerPosition.y - (float)screenHeight/2 - 16);
			updateGrids(playerPosition);
			//this.screenCorners[0] = new Vector2(playerPosition.x - (float)screenWidth/2, playerPosition.y + (float)screenHeight/2);
			//this.screenCorners[1] = new Vector2(playerPosition.x + (float)screenWidth/2, playerPosition.y + (float)screenHeight/2);
			//this.screenCorners[2] = new Vector2(playerPosition.x + (float)screenWidth/2, playerPosition.y - (float)screenHeight/2);
			//this.screenCorners[3] = new Vector2(playerPosition.x - (float)screenWidth/2, playerPosition.y - (float)screenHeight/2);
			//updateChunks(playerPosition);
		}
		
		entities.clear();
		for (int x = 2; x < this.grids.length - 2; x++) {
			for (int y = 2; y < this.grids[x].length - 2; y ++) {
				if (this.grids[x][y].getEntities().length != 0) {
					if (this.grids[x][y].isPhysicsEnabled()) {
						for (Entity e : this.grids[x][y].getEntities()) {
							Point oldGrid = getRealGrid(e.getPosition().x, e.getPosition().y);
							e.setPosition(e.getBody().getPosition());
							e.setRotation(e.getBody().getAngle() * MathUtils.radiansToDegrees);
							e.update();
							Point newGrid = getRealGrid(e.getPosition().x, e.getPosition().y);
							if (!oldGrid.equals(newGrid)) {
								Point direction = new Point(newGrid.x - oldGrid.x, newGrid.y - oldGrid.y);
								this.grids[x][y].removeEntity(e);
								if (x+direction.x > this.grids.length - 1 || y+direction.y > this.grids[x].length - 1) {
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
		for (Entity e : entities)
			e.render(batch);
	}
	
	public void destroy() { }
	
	public abstract Chunk generateChunk(int x, int y);
	
	public abstract Grid generateGrid(Point worldPosition);
	
	public abstract boolean repeats();
	
	public abstract int size();
}