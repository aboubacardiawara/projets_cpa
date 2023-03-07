package algorithms.detection;

import java.awt.Point;
import java.util.ArrayList;

import supportGUI.Circle;
import supportGUI.Line;

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
	
	protected Line calculDiametre(ArrayList<Point> points) {
		return calculDiametreNaif(points);
	}
	
	private Line calculDiametreNaif(ArrayList<Point> points) {
		if (points.size() < 3) {
			return null;
		}
		Double distance_max = null;
		Point pf = points.get(0);
		Point qf = points.get(1);
		
		for (Point p : points) {
			for (Point q : points) {
				if (distance_max == null) {
					distance_max = p.distance(q);
				} else {
					Double d = p.distance( q);
					if (d > distance_max) {
						distance_max = d;
						pf = p;
						qf = q;
					}
				}
			}
		}
		return new Line(pf, qf);
	}
}
