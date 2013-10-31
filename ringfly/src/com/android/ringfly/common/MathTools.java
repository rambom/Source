package com.android.ringfly.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.badlogic.gdx.math.Vector2;

public class MathTools {
	public static List<String> getShuffle(int start,int end) {
		List<String> list = new ArrayList<String>();
		for (int i = start; i <=end; i++) {
			list.add(i + "");
		}
		Collections.shuffle(list);
		return list;
	}

	public static List<Vector2> CalcQieDian(Vector2 ptCenter, double dbRadious,
			Vector2 ptOutside) {
		List<Vector2> resList = new ArrayList<Vector2>();
		double r = dbRadious;
		double ex = ptOutside.x - ptCenter.x;
		double ey = ptOutside.y - ptCenter.y;
		double t = r / Math.sqrt(ex * ex + ey * ey);
		double fx = ex * t;
		double fy = ey * t;
		double a = Math.acos(t);
		double gx = fx * Math.cos(a) - fy * Math.sin(a);
		double gy = fx * Math.sin(a) + fy * Math.cos(a);
		double hx = fx * Math.cos(-a) - fy * Math.sin(-a);
		double hy = fx * Math.sin(-a) + fy * Math.cos(-a);
		resList.add(new Vector2((float) (gx + ptCenter.x),
				(float) (gy + ptCenter.y)));
		resList.add(new Vector2((float) (hx + ptCenter.x),
				(float) (hy + ptCenter.y)));
		return resList;
	}

	public static List<Vector2> CalcQieDian(Vector2 ptCenter, double dbRadious,
			Vector2 ptOutside, float len) {
		List<Vector2> resList = new ArrayList<Vector2>();
		double r = dbRadious;
		double ex = ptOutside.x - ptCenter.x;
		double ey = ptOutside.y - ptCenter.y;
		double t = r / Math.sqrt(ex * ex + ey * ey);
		double fx = ex * t;
		double fy = ey * t;
		double a = Math.acos(t);
		double gx = fx * Math.cos(a) - fy * Math.sin(a);
		double gy = fx * Math.sin(a) + fy * Math.cos(a);
		double hx = fx * Math.cos(-a) - fy * Math.sin(-a);
		double hy = fx * Math.sin(-a) + fy * Math.cos(-a);
		double q1X = gx + ptCenter.x;
		double q1Y = gy + ptCenter.y;

		double q2X = hx + ptCenter.x;
		double q2Y = hy + ptCenter.y;

		double d1 = distanceCalc(new Vector2((float) q1X, (float) q1Y),
				ptOutside);
		double d2 = distanceCalc(new Vector2((float) q2X, (float) q2Y),
				ptOutside);
		double k1 = len / d1;
		double k2 = len / d2;

		double x1 = k1 * (q1X - ptOutside.x) + q1X;
		double y1 = k1 * (q1Y - ptOutside.y) + q1Y;

		double x2 = k2 * (q2X - ptOutside.x) + q2X;
		double y2 = k2 * (q2Y - ptOutside.y) + q2Y;

		resList.add(new Vector2((float) x1, (float) y1));
		resList.add(new Vector2((float) x2, (float) y2));
		return resList;
	}

	public static double distanceCalc(Vector2 p1, Vector2 p2) {
		double t1 = Math.pow((p1.x - p2.x), 2);
		double t2 = Math.pow((p1.y - p2.y), 2);
		return Math.sqrt(t1 + t2);
	}

	public static double kCalc(Vector2 p1, Vector2 p2) {
		double t1 = Math.pow((p1.x - p2.x), 2);
		double t2 = Math.pow((p1.y - p2.y), 2);
		return t1 / t2;
	}

	public static double angleCalc(Vector2 p1, Vector2 p2) {
		float distanceX = p1.x - p2.x;
		float distanceY = p1.y - p2.y;
		double angle = Math.atan2(distanceY, distanceX);
		return angle;
	}

	public static float abs(float n) {
		return (n >= 0.0f) ? n : -n;
	}

	public static float sgn(float n) {
		if (n > 0.0f)
			return 1.0f;
		else if (n < 0.0f)
			return -1.0f;
		else
			return 0.0f;
	}

	public static float min(float a, float b) {
		return (a < b) ? a : b;
	}

	public static float max(float a, float b) {
		return (a > b) ? a : b;
	}

	public static int min(int a, int b) {
		return (a < b) ? a : b;
	}

	public static int max(int a, int b) {
		return (a > b) ? a : b;
	}

//	public static void main(String... strings) {
//		// List<Vector2> qiedian = CalcQieDian(new Vector2(1, 1), 1d, new
//		// Vector2(
//		// 3f, 2f));
//		System.out.print(getShuffle(0,9).toString());
//		System.out.print(getShuffle(0,9).toString());
//	}
}
