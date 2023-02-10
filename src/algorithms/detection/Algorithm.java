package algorithms.detection;

import java.awt.Point;
import java.util.ArrayList;

import supportGUI.Circle;

public abstract class Algorithm {

	public abstract Circle solve(ArrayList<Point> points);
	
	protected boolean circleContainsAllPoints(Circle circle, ArrayList<Point> points) {
		for (Point point : points) {
			if (!circleContainsPoint(circle, point)) {
				return false;
			}
		}
		return true;
	}

	protected boolean circleContainsPoint(Circle circle, Point point) {
		return point.distance(circle.getCenter()) <= circle.getRadius();
	}

	protected Point center(Point p, Point q) {
		return new Point((p.x + q.x) / 2, (p.y + q.y) / 2);
	}
}
