package algorithms.detection;

import java.awt.Point;
import java.util.ArrayList;

import supportGUI.Circle;
import supportGUI.Line;

public class WelzlAlgorithmForMinCircle extends Algorithm {

	public Circle solve(ArrayList<Point> points) {
		return minidisk(points);
	}

	private Circle minidisk(ArrayList<Point> inputPoints) {
		if (inputPoints.isEmpty()) {
			return null;
		}
		ArrayList<Point> points = new ArrayList<>(inputPoints);
		Circle c = wikipediaTrial(points, new ArrayList<>());
		assertionCheck(c != null, "cercle cannot be null");
		System.out.println("cercle: (" + c.getCenter() + ",  " + c.getRadius() + ")");
		return c;
	}

	@SuppressWarnings("unchecked")
	private Circle wikipediaTrial(ArrayList<Point> inputPoints, ArrayList<Point> inputR) {
		ArrayList<Point> R = (ArrayList<Point>) inputR.clone();
		ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();

		assertionCheck(R.size() <= 3, "Precondition (WelzlAlgorithm): |R| ≤ 3");

		if (points.isEmpty() || R.size() == 3) {
			return trivial(R);
		}

		Point p = points.remove(0);
		Circle D = wikipediaTrial(points, R);
		if (circleContainsPoint(D, p)) {
			return D;
		}

		R.add(p);
		return wikipediaTrial(points, R);
	}

	private Circle trivial(ArrayList<Point> r) {
		if (r.size() == 3) { // |R|=3 case
			return cercleMinimalTroisPoints(r);
		} else { // points.isEmpty()
			return new Circle(new Point(), 0);
		}
	}

	private Circle minidiskRec(ArrayList<Point> points, ArrayList<Point> r) {
		if (r.size() == 3) {
			return handleBaseCase(r);
		}
		if (points.isEmpty()) {
			return new Circle(new Point(), 0);
		}
		Point p = points.remove(0);
		Circle d = minidiskRec(points, r);
		if (d != null && !circleContainsPoint(d, p)) {
			r.add(p);
			d = minidiskRec(points, r);
			r.remove(p);
		} else if (d == null) {
			r.add(p);
			d = minidiskRec(points, r);
			r.remove(p);
		}
		return d;
	}

	private Circle handleBaseCase(ArrayList<Point> r) {
		if (r.size() == 1) {
			return new Circle(r.get(0), 0);
		} else if (r.size() == 2) {
			return new Circle(center(r.get(0), r.get(1)), (int) (r.get(0).distance(r.get(1)) / 2));
		} else { // r.size() == 3
			return circleFromThreePoints(r);
		}
	}

	private Circle circleFromThreePoints(ArrayList<Point> points) {
		Line l = calculDiametre(points);
		Point p = l.getP();
		Point q = l.getQ();
		Point m = midPoint(p, q);
		return new Circle(m, (int) p.distance(q));
	}

	private Point midPoint(Point p, Point q) {
		int x = (int) ((p.getX() + q.getX()) / 2);
		int y = (int) ((p.getY() + q.getY()) / 2);
		return new Point(x, y);
	}

	private void assertionCheck(boolean b, String msg) {
		if (!b)
			throw new AssertionError(msg, null);
	}

	private Circle cercleMinimalTroisPoints(ArrayList<Point> points) {
		Point a = new Point((int) points.get(0).getX(), (int) points.get(0).getY());
		Point b = new Point((int) points.get(1).getX(), (int) points.get(1).getY());
		Point c = new Point((int) points.get(2).getX(), (int) points.get(2).getY());
		double dA = a.distanceSq(b);
		double dB = b.distanceSq(c);
		double dC = c.distanceSq(a);
		Point center = null;
		double radius = 0;
		if (dA > dB && dA > dC) {
			center = midPoint(a, b);
			radius = a.distance(center);
		} else if (dB > dC) {
			center = midPoint(b, c);
			radius = b.distance(center);
		} else {
			center = midPoint(c, a);
			radius = c.distance(center);
		}
		return new Circle(center, (int) radius);
	}

}
