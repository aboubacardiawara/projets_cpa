package algorithms.detection;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import supportGUI.Circle;

public class WelzlAlgorithmForMinCircle extends Algorithm {

	public Circle solve(ArrayList<Point> points) {
		return minidisk(points);
	}

	private Circle minidisk(ArrayList<Point> inputPoints) {
		if (inputPoints.isEmpty()) {
			return null;
		}
		ArrayList<Point> points = new ArrayList<>(inputPoints);
		Circle c = welzlAlgoRec(points, new ArrayList<Point>());
		assertionCheck(c != null, "cercle cannot be null");
		return c;
	}

	/**
	 * Version recursive de l'algo de welzl. ! Stack overflow sur une grande entrée
	 * 
	 * @param inputPoints
	 * @param inputR
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Circle welzlAlgoRec(ArrayList<Point> inputPoints, ArrayList<Point> inputR) {
		ArrayList<Point> R = (ArrayList<Point>) inputR.clone();
		ArrayList<Point> points = (ArrayList<Point>) inputPoints.clone();

		assertionCheck(R.size() <= 3, "Precondition (WelzlAlgorithm): |R| ≤ 3");

		if (points.isEmpty() || R.size() == 3) {
			return trivial(R);
		}

		Random randomGenerator = new Random();
		//Point p = points.remove(randomGenerator.nextInt(0, points.size()));
		Point p = points.remove(0);
		Circle D = welzlAlgoRec(points, R);
		if (circleContainsPoint(D, p)) {
			return D; 
		}

		R.add(p);
		return welzlAlgoRec(points, R);
	}

	/**
	 * Calcul un cercle min qui couvre l'ensemble des points en entree.
	 * 
	 * @param r
	 * @return
	 */
	private Circle trivial(ArrayList<Point> r) {
		if (r.size() == 3) { // |R|=3
			return cercleCirconscrit(r);
		} else if (r.size() == 2) { // |R|=3
			int rayon = (int) r.get(0).distance(r.get(1)) / 2;
			Point centre = center(r.get(0), r.get(1));
			return new Circle(centre, rayon);
		} else if (r.size() == 1) { // |R|=3
			return new Circle(r.get(0), 0);
		} else { // points.isEmpty()
			return new Circle(new Point(), 0);
		}
	}

	private void assertionCheck(boolean b, String msg) {
		if (!b)
			throw new AssertionError(msg, null);
	}

	/**
	 * @author Camille
	 * @param p
	 * @param q
	 * @param r
	 * @return
	 */
	public Circle cercleCirconscrit(ArrayList<Point> points) {
		Point p, q, r;

		p = points.get(0);
		q = points.get(1);
		r = points.get(2);
		// equation des droites des médiatrices [pq] et [pr]
		// y = a1x + b1
		// y = a2x + b2
		// 0 = (a1-a2) xm + b1 - b2 => xm = - (b1 -b2) / (a1-a2) 
		double a1, b1, a2, b2;
		double m1X, m1Y, m2X, m2Y;
		double x, y;
		
		// milieux
		m1X = (q.getX() + p.getX()) / 2.0;
		m1Y = (q.getY() + p.getY()) / 2.0;
		
		a1 = -((q.getX() - p.getX()) / (q.getY() - p.getY()));
		b1 = m1Y - a1 * m1X;

		m2X = (r.getX() + p.getX()) / 2.0;
		m2Y = (r.getY() + p.getY()) / 2.0;
		a2 = -((r.getX() - p.getX()) / (r.getY() - p.getY()));
		b2 = m2Y - a2 * m2X;

		// calcul point d'intersection qui est le centre du cercle circonscrit
		x = (b1 - b2) / (a2 - a1);
		y = a2 * x + b2;

		Point centre = new Point((int) x, (int) y);

		return new Circle(centre, ((int) p.distance(centre))+1);
	}

}
