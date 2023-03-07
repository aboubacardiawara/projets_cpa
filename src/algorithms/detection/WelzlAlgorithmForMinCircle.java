package algorithms.detection;

import java.awt.Point;
import java.lang.invoke.StringConcatFactory;
import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;

import supportGUI.Circle;
import supportGUI.Line;

public class WelzlAlgorithmForMinCircle extends Algorithm {

	public Circle solve(ArrayList<Point> points) {
		return minidisk(points);
	}

	private Circle minidisk(ArrayList<Point> inputPoints) {
		/**
		 * if P = ~ then D : = ~ else choose p 6 P; D := m i n i d i s k ( P - {p}); if
		 * p ~ D then D := b_minidisk(P - {p},p)
		 */
		@SuppressWarnings("unchecked")
		ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();
		Circle d;
		if (points.isEmpty()) {
			d = new Circle(new Point(30, 30), 0);
		} else {
			Point p = points.remove(0);
			d = minidisk(points);
			assertionCheck(d != null, "circle cannot be null");
			if (!circleContainsPoint(d, p)) {
				ArrayList<Point> r = new ArrayList<Point>();
				r.add(p);
				d = b_minidisk(points, r);
			}
		}

		return d;
	}
	
	private void assertionCheck(boolean b, String msg) {
		if (!b) throw new AssertionError(msg, null);
	}

	private Circle b_minidisk(ArrayList<Point> inputPoints, ArrayList<Point> r) {
		ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();
		Circle d;
		if (r.size() == 3 || points.isEmpty()) {
			d = handleBaseCase(r);
		}

		Point p = points.get(0);

		d = b_minidisk(points, r);
		if (d != null && !circleContainsPoint(d, p)) {
			r.add(p);
			d = b_minidisk(points, r);
		}
		return d;
	}

	public Circle handleBaseCase(ArrayList<Point> points) {
		assert points.size() >= 3;
		Point A = points.get(0);
		Point B = points.get(1);
		Point C = points.get(2);

		double x1 = A.getX();
		double y1 = A.getY();
		double x2 = B.getX();
		double y2 = B.getY();
		double x3 = C.getX();
		double y3 = C.getY();

		Point M = center(A, B);
		Point N = center(B, C);

		// mediatrice A B
		double a = -(x2 - x1) / (y2 - y1);
		double b = M.getY() - a * M.getX();
		// mediatrice B C
		double c = -(x3 - x2) / (y3 - y2);
		double d = N.getY() - c * N.getX();

		// intersection
		int x = (int) ((d - b) / (a - c));
		int y = (int) (a * x + b);
		Point intersection = new Point(x, y);

		return new Circle(intersection, (int) intersection.distance(A));
	}
	
	public Circle handleBaseCaseBis(ArrayList<Point> points) {
		Line diametre = calculDiametre(points);
		Point p = diametre.getP();
		Point q = diametre.getQ();
		Point c = center(p, q);
		
		return new Circle(c, (int) c.distance(p));
	}
}
