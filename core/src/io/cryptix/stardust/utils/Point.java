package io.cryptix.stardust.utils;

public class Point {
	public int x;
	public int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {	
		if (obj instanceof Point && obj != null)
			if ((((Point)obj).x == this.x) && (((Point)obj).y == this.y))
				return true;		
		return false;
	}
}
