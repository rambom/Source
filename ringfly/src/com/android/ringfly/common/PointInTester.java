package com.android.ringfly.common;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import static com.android.ringfly.common.MathTools.*;

public class PointInTester {
	public static boolean pointInRectangle(Rectangle r, Vector2 p) {
		return r.x <= p.x && r.x + r.width >= p.x && r.y <= p.y
				&& r.y + r.height >= p.y;
	}

	public static boolean pointInRectangle(Rectangle r, float x, float y) {
		return r.x <= x && r.x + r.width >= x && r.y <= y
				&& r.y + r.height >= y;
	}

	public static boolean pointInCircle(Circle c, Vector2 p) {
		Vector2 p1 = new Vector2(c.x, c.y);
		Vector2 p2 = new Vector2(p.x, p.y);
		double distance = distanceCalc(p1, p2);
		return distance <= c.radius ? true : false;

	}

	public static boolean pointInCircle(Circle c, float x, float y) {
		Vector2 p1 = new Vector2(c.x, c.y);
		Vector2 p2 = new Vector2(x, y);
		double distance = distanceCalc(p1, p2);
		return distance <= c.radius ? true : false;
	}

	// point come first
	public static boolean pointInPolygon(Vector2 point, Vector2... p) {
		if (p.length <= 1)
			return false;
		double angle = 0.0;
		for (int i = 0; i < p.length - 1; i++) {
			Vector2 l1 = new Vector2(p[i].x - point.x, p[i].y - point.y);
			Vector2 l2 = new Vector2(p[i + 1].x - point.x, p[i + 1].y - point.y);
			if ((l1.x == 0 && l1.y == 0) || (l2.x == 0 && l2.y == 0))// 点在顶点上
			{
				return true;
			}
			double temp = GetAngle(l1.x, l1.y, l2.x, l2.y);
			if (Math.abs(temp) == Math.PI)// 点在边上
			{
				return true;
			} else {
				angle += temp;
			}
		}
		if (Math.abs(angle) >= Math.PI)// 点在多边形内部
		{
			return true;
		} else {
			return false;
		}
	}

	public static double GetAngle(double x1, double y1, double x2, double y2) {
		double theta = 0.0, theta1 = 0.0, theta2 = 0.0;
		theta1 = Math.atan2(y1, x1); // tan(theta1)=y1/x1,求反正切值
		theta2 = Math.atan2(y2, x2);
		theta = theta1 - theta2; // 获取夹角
		// 返回的夹角值的绝对值要在(0,180)之间
		while (theta > Math.PI)
			theta -= 2 * Math.PI;
		while (theta < -Math.PI)
			theta += 2 * Math.PI;
		return theta;
	}

	public static void main(String[] args) {
		System.out.println(pointInPolygon(new Vector2(2.1f, 2f), new Vector2(0,
				0), new Vector2(2, 0), new Vector2(2, 2), new Vector2(0, 2)));
	}

	/**
	 * 面积放判断点是否在三角形内
	 * 
	 * @param pos
	 * @param posA
	 * @param posB
	 * @param posC
	 * @return
	 */
	private static boolean pointInTriangle(Vector2 pos, Vector2 posA,
			Vector2 posB, Vector2 posC) {
		double triangleArea = calcTriangleArea(posA, posB, posC);
		double area = calcTriangleArea(pos, posA, posB);
		area += calcTriangleArea(pos, posA, posC);
		area += calcTriangleArea(pos, posB, posC);
		double epsilon = 0.0001; // 由于浮点数的计算存在着误差，故指定一个足够小的数，用于判定两个面积是否(近似)相等。
		if (Math.abs(triangleArea - area) < epsilon) {
			return true;
		}
		return false;
	}

	private static double calcTriangleArea(Vector2 pos1, Vector2 pos2,
			Vector2 pos3) {
		double result = Math
				.abs((pos1.x * pos2.y + pos2.x * pos3.y + pos3.x * pos1.y
						- pos2.x * pos1.y - pos3.x * pos2.y - pos1.x * pos3.y) / 2.0D);
		return result;
	}

}
