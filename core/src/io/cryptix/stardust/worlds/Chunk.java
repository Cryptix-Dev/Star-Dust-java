package io.cryptix.stardust.worlds;

import com.badlogic.gdx.utils.ArrayMap;

import io.cryptix.stardust.utils.Point;

public class Chunk {
	
	private final GameWorld world;
	
	private Point position;
	private ArrayMap<Point, Grid> grids;
	public Chunk(GameWorld world, int x, int y) {
		this.world = world;
		this.position = new Point(x, y);
		this.grids = new ArrayMap<Point, Grid>(false, 256);
	}
	
	public Point getPosition() {
		return position;
	}
	
	public Grid getGrid(Point worldLocation) {
		Grid output = this.grids.get(worldLocation);
		if (output == null) {
			output = world.generateGrid(worldLocation);
			this.grids.put(worldLocation, output);
		}
		return output;
	}
	
	public void save() {}
	
	public void load() {}
	
	public void destroy() {
		for (Grid g : grids.values()) {
			g.destroy();
			g = null;
		}
		grids.clear();
	}
}
