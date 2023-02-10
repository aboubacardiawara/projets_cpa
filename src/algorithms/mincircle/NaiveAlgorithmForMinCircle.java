package algorithms.mincircle;

import java.awt.Point;
import java.util.ArrayList;

import supportGUI.Circle;

public class NaiveAlgorithmForMinCircle {
	public Circle solve(ArrayList<Point> points) {
		return new Circle(points.get(0), 10);
	}
}
