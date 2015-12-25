package io.cryptix.stardust.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class Util {
	
	public static final float PIXELS_TO_METERS = .1f;
	public static final float METERS_TO_PIXELS = 10f;
	
	private static ShapeRenderer debugRenderer = new ShapeRenderer();
	
	
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
	
	public static void DrawSquare(Vector2 bottomLeft, Vector2 topRight, Matrix4 projMatrix)
	{
		Gdx.gl20.glLineWidth(2);
		debugRenderer.setProjectionMatrix(projMatrix);
		debugRenderer.begin(ShapeRenderer.ShapeType.Line);
		debugRenderer.setColor(Color.WHITE);
		debugRenderer.line(bottomLeft, new Vector2(bottomLeft.x, topRight.y));
		debugRenderer.line(bottomLeft, new Vector2(topRight.x, bottomLeft.y));
		debugRenderer.line(topRight, new Vector2(topRight.x, bottomLeft.y));
		debugRenderer.line(topRight, new Vector2(bottomLeft.x, topRight.y));
		debugRenderer.end();
		Gdx.gl20.glLineWidth(1);
	}
}
