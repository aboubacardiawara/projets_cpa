package algorithms.detection;

import java.awt.Point;
import java.util.ArrayList;

/***************************************************************
 * TME 1: calcul de diamètre et de cercle couvrant minimum.    *
 *   - Trouver deux points les plus éloignés d'un ensemble de  *
 *     points donné en entrée.                                 *
 *   - Couvrir l'ensemble de poitns donné en entrée par un     *
 *     cercle de rayon minimum.                                *
 *                                                             *
 * class Circle:                                               *
 *   - Circle(Point c, int r) constructs a new circle          *
 *     centered at c with radius r.                            *
 *   - Point getCenter() returns the center point.             *
 *   - int getRadius() returns the circle radius.              *
 *                                                             *
 * class Line:                                                 *
 *   - Line(Point p, Point q) constructs a new line            *
 *     starting at p ending at q.                              *
 *   - Point getP() returns one of the two end points.         *
 *   - Point getQ() returns the other end point.               *
 ***************************************************************/
import supportGUI.Circle;
import supportGUI.Line;

public class Camille {

	// calculCercleMin: ArrayList<Point> --> Circle
	// renvoie un cercle couvrant tout point de la liste, de rayon minimum.
	public Circle calculCercleMin(ArrayList<Point> points) {
//		return cercleMinNaif(points);
		return welzl(points);
	}

	// implémentation du calcul du cercle minimum avec un algorithme naïf
	public static Circle cercleMinNaif(ArrayList<Point> points) {
		if (points.isEmpty()) {
			return null;
		}

		Point pMax = points.get(0), qMax = points.get(0);
		double radius = 0.0, dist = 0.0, distMax = 0.0;

		// vars temps
		Point pTemp, qTemp;

		// recherche des deux points les plus éloignés
		for (int i = 0; i < points.size(); i++) {
			for (int j = 0; j < points.size(); j++) {
				pTemp = points.get(i);
				qTemp = points.get(j);
				dist = pTemp.distance(qTemp);
				if (dist > distMax) {
					distMax = dist;
					pMax = pTemp;
					qMax = qTemp;
				}
			}
		}

		// calcul du centre
		double cX, cY;
		cX = (pMax.x + qMax.x) / 2;
		cY = (pMax.y + qMax.y) / 2;

		// calcul du radius
		radius = distMax / 2;

		Circle res = new Circle(new Point((int) cX, (int) cY), (int) radius);
		if (checkEnglobant(res, points)) {
			return res;
		}

		Circle resCirc, resCercMin = new Circle(points.get(0), Integer.MAX_VALUE);
		for (int i = 0; i < points.size() - 2; i++) {
			for (int j = i + 1; j < points.size() - 1; j++) {
				for (int k = j + 1; k < points.size(); k++) {
					resCirc = cercleCirconscrit(points.get(i), points.get(j), points.get(k));
					if (checkEnglobant(resCirc, points) && resCercMin.getRadius() > resCirc.getRadius()) {
						resCercMin = resCirc;
					}
				}
			}
		}
		return resCercMin;
	}

	// implémentation de l'algorithme de Welzl
	public static Circle welzl(ArrayList<Point> points) {
		ArrayList<Point> vus = new ArrayList<>();
		ArrayList<Point> bordure = new ArrayList<>();
		Circle res = new Circle(points.get(0), 10);
		bordure.add(points.get(0));
		for (Point p : points) {
			if (!isInCircle(p, res)) {
				bordure.add(p);
				res = md(vus, bordure);
				// update de la bordure
//				bordure = updateBordure(bordure, res);
			}
			vus.add(p);
		}
		return res;
	}

	/////////////////////////////////////////////////////////////////////
	// fonctions intermédiaires
	/////////////////////////////////////////////////////////////////////

	// verifie que tous les points sont dans le cercle
	private static boolean checkEnglobant(Circle c, ArrayList<Point> points) {

		Point centre = c.getCenter();
		int radius = c.getRadius();
		for (Point p : points) {
			if (p.distance(centre) > radius + 1) {
				return false;
			}
		}
		return true;
	}

	// verifie si un point donné appartient à cercle
	private static boolean isInCircle(Point p, Circle c) {
		return c.getCenter().distance(p) <= c.getRadius();
	}

	// renvoie le cercle circonscrit
	private static Circle cercleCirconscrit(Point p, Point q, Point r) {

		// equation des droites des médiatrices [pq] et [pr]
		double a1, b1, a2, b2;
		double m1X, m1Y, m2X, m2Y;
		double x, y;
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

		return new Circle(centre, ((int) p.distance(centre)) + 1);
	}

	public static Circle md(ArrayList<Point> points, ArrayList<Point> bordure) {
		if (bordure.size() == 1) {
			return new Circle(bordure.get(0), 10);
		}
		if (bordure.size() == 2) {
			Point a = bordure.get(0);
			Point b = bordure.get(1);
			// calcul du centre
			double cX, cY, radius;
			cX = (a.x + b.x) / 2;
			cY = (a.y + b.y) / 2;

			// calcul du radius
			radius = a.distance(b) / 2;

			return new Circle(new Point((int) cX, (int) cY), (int) radius);
		}
		// dernier point ajouté à la bordure fait forcément partie de la bordure du
		// nouveau cercle
		Point fixed = bordure.get(bordure.size() - 1);
		Circle resMin = new Circle(fixed, 10), tmp;
		double radiusMin = Integer.MAX_VALUE;
		for (int i = 0; i < points.size() - 1; i++) {
			for (int j = i + 1; j < points.size(); j++) {
				tmp = cercleCirconscrit(fixed, points.get(i), points.get(j));
				if (checkEnglobant(tmp, points) && radiusMin > tmp.getRadius()) {
					radiusMin = tmp.getRadius();
					resMin = tmp;
				}
			}
		}
		return resMin;
	}
}
