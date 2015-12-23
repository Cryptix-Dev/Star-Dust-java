package io.cryptix.stardust.utils;

import com.badlogic.gdx.math.Vector2;

public class Util {
	
	public static final float PIXELS_TO_METERS = .1f;
	public static final float METERS_TO_PIXELS = 10f;
	
	
	public static float convertToMeters(float pixels) {
		return pixels * PIXELS_TO_METERS;
	}
	
	public static float convertToPixels(float meters) {
		return meters * METERS_TO_PIXELS;
	}
	
	public static Vector2 convertToMeters(Vector2 vPixels) {
		return new Vector2(vPixels.x*PIXELS_TO_METERS, vPixels.y*PIXELS_TO_METERS);
	}
	
	public static Vector2 convertToPixels(Vector2 vMeters) {
		return new Vector2(vMeters.x*METERS_TO_PIXELS, vMeters.y*METERS_TO_PIXELS);
	}
}
