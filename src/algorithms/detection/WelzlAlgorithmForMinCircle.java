package algorithms.detection;

import java.awt.Point;
import java.util.ArrayList;

import supportGUI.Circle;
import supportGUI.Line;

public class WelzlAlgorithmForMinCircle extends Algorithm {

	public Circle solve(ArrayList<Point> points) {
		return null;
	}

	private Circle minidisk(ArrayList<Point> inputPoints) {
		/**
		 * if P = ~ then D : = ~ else choose p 6 P; D := m i n i d i s k ( P - {p}); if
		 * p ~ D then D := b_minidisk(P - {p},p)
		 */
		ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();
		if (points.isEmpty()) {
			return null;
		}

		Point p = points.get(0);
		points.remove(p);
		Circle d = minidisk(points);
		if (!circleContainsPoint(d, p)) {
			ArrayList<Point> r = new ArrayList<>();
			r.add(p);
			d = b_minidisk(points, r);
		}

		return d;
	}

	private Circle b_minidisk(ArrayList<Point> inputPoints, ArrayList<Point> r) {
		ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();
		Circle d;
		if (r.size() == 3 || points.isEmpty()) {
			d = dhandleBaseCase(r);
		}

		Point p = points.get(0);

		d = b_minidisk(points, r);
		if (d != null && !circleContainsPoint(d, p)) {
			r.add(p);
			d = b_minidisk(points, r);
		}
		return null;
	}

	private Circle dhandleBaseCase(ArrayList<Point> r) {
		// TODO Auto-generated method stub
		return null;
	}
}
