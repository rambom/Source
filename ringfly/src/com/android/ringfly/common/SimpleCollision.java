package com.android.ringfly.common;

import com.badlogic.gdx.math.Rectangle;

public class SimpleCollision {

	public static boolean overlapRectangles(Rectangle r1, Rectangle r2) {
		return (r1.x < r2.x + r2.width && r1.x + r1.width > r2.x
				&& r1.y < r2.y + r2.height && r1.y + r1.height > r2.y);
	}
	public static boolean outOfStage(){
		return false;
	}
}
