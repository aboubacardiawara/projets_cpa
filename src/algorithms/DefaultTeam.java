package algorithms;

import java.awt.Point;
import java.security.AlgorithmConstraints;
import java.security.AlgorithmParameters;
import java.security.CryptoPrimitive;
import java.security.Key;
import java.util.ArrayList;
import java.util.Set;

import algorithms.detection.Camille;
import algorithms.detection.Lyna;
import algorithms.detection.NaiveAlgorithmForMinCircle;
import algorithms.detection.Utile;
import algorithms.detection.WelzlAlgorithmForMinCircle;
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

public class DefaultTeam {

	// calculDiametre: ArrayList<Point> --> Line
	// renvoie une paire de points de la liste, de distance maximum.
	public Line calculDiametre(ArrayList<Point> points) {
		if (points.size() < 3) {
			return null;
		}

		Point p = points.get(0);
		Point q = points.get(1);

		/*******************
		 * PARTIE A ECRIRE *
		 *******************/

		return new Line(p, q);
	}

	// calculCercleMin: ArrayList<Point> --> Circle
	// renvoie un cercle couvrant tout point de la liste, de rayon minimum.
	public Circle calculCercleMin(ArrayList<Point> points) {
		//return new NaiveAlgorithmForMinCircle().solve(points);
		return new WelzlAlgorithmForMinCircle().solve(points);
		//return new WelzlAlgorithmForMinCircle().cercleCirconscrit(points);
		//return new Lyna().calculCercleMin(points);
		//return new Utile().solve(points);
		//return new Camille().calculCercleMin(points);
	}

	private boolean isEquilateral(ArrayList<Point> points) {
		Point p = points.get(0);
		Point q = points.get(1);
		Point r = points.get(2);

		return p.distance(q) == p.distance(r) && p.distance(r) == q.distance(r);
	}
}
