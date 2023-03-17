package algorithms.detection;

import java.awt.Point;
import java.util.ArrayList;
import supportGUI.Circle;

public class Lyna {

	public Circle calculCercleMin(ArrayList<Point> points) {
		ArrayList<Point> R = new ArrayList<Point>();
		ArrayList<Point> cp = (ArrayList<Point>) points.clone();
		Circle D = new Circle(new Point(0, 0), 0);
		// Recursion can also terminate when R has size 3
		if (cp.isEmpty() || R.size() == 3) {
			D = B_md(R);
		} else {
			int range = (points.size() - 1) + 1;
			int index = (int) (Math.random() * range);
			Point p = cp.remove(index); // supprime le point p d'index "index" puis remlace
			D = B_minidisk(cp, R);
			if (D != null && !inCircle(D, p)) {
				R.add(p);
				D = B_minidisk(cp, R);
			}
		}
		return D;
	}

	private double crossProduct(Point p, Point q, Point s, Point t) {
		return ((q.x - p.x) * (t.y - s.y) - (q.y - p.y) * (t.x - s.x));
	}

	public Circle B_md(ArrayList<Point> pointsR) {
		Circle circle = new Circle(new Point(0, 0), 0);
//for 2 points the minimal circle has its center at the midpoint between the two points
		if (pointsR.size() == 2) {
			Point p1 = pointsR.get(0);
			Point p2 = pointsR.get(1);
			double center_x = (p1.getX() + p2.getX()) / 2;
			double center_y = (p1.getY() + p2.getY()) / 2;
			Point center = new Point((int) center_x, (int) center_y);
			circle = new Circle(center, (int) center.distance(p1));
		} else// for 3 points the circle is the circumcircle of the triangle described by the
				// points
		if (pointsR.size() == 3) {
			Point p1 = pointsR.get(0);
			Point p2 = pointsR.get(1);
			Point p3 = pointsR.get(2);
			if (crossProduct(p1, p2, p1, p3) != 0) {
				double a = p1.getX() - p2.getX();
				double b = p1.getY() - p2.getY();
				double c = p1.getX() - p3.getX();
				double d = p1.getY() - p3.getY();
				double e = ((p1.getX() * p1.getX() - p2.getX() * p2.getX())
						- (p2.getY() * p2.getY() - p1.getY() * p1.getY())) / 2;
				double f = ((p1.getX() * p1.getX() - p3.getX() * p3.getX())
						- (p3.getY() * p3.getY() - p1.getY() * p1.getY())) / 2;

				double x = (e * d - b * f) / (a * d - b * c);
				double y = (a * f - e * c) / (a * d - b * c);
				Point center = new Point((int) x, (int) y);
				circle = new Circle(center, (int) center.distance(p1));
			}
		}
		return circle;
	}

	public boolean inCircle(Circle circle, Point p) {
		return circle.getCenter().distance(p) <= circle.getRadius();
	}

	public Circle B_minidisk(ArrayList<Point> points, ArrayList<Point> pointsR) {
		ArrayList<Point> P = new ArrayList<Point>(points);
		ArrayList<Point> R = new ArrayList<Point>(pointsR);
		Circle D = new Circle(new Point(0, 0), 0);
		if (P.isEmpty() || R.size() == 3) {
			D = B_md(R);
		} else {
			int range = (points.size() - 1) + 1;
			int index = (int) (Math.random() * range);
			Point p = P.remove(index);
			D = B_minidisk(P, R);
			if (!inCircle(D, p)) {
				R.add(p);
				D = B_minidisk(P, R);
			}
		}
		return D;
	}

}
