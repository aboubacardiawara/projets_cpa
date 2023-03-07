package algorithms.detection;

import java.awt.Point;
import java.util.ArrayList;

import supportGUI.Circle;
import supportGUI.Line;

public class NaiveAlgorithmForMinCircle extends Algorithm {

	public Circle solve(ArrayList<Point> points) {
		return implementationPartielle(points);
	}

	public Circle implementationPartielle(ArrayList<Point> points) {

		if (points.isEmpty()) {
			return null;
		}
		Line diametreInitial = calculDiametre(points);
		Point center = center(diametreInitial.getP(), diametreInitial.getQ());
		int radius = (int) diametreInitial.getP().distance(diametreInitial.getQ()) / 2;

		/*******************
		 * PARTIE A ECRIRE *
		 *******************/

		for (Point p : points) {
			for (Point q : points) {
				center = center(p, q);
				radius = (int) p.distance(q);
				Circle circle = new Circle(center, radius);
				if (circleContainsAllPoints(circle, points)) {
					return circle;
				}
			}
		}

		return new Circle(center, radius);
	}

	


}
