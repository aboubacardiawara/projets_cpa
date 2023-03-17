package experimentation;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import algorithms.detection.Utile;
import algorithms.detection.WelzlAlgorithmForMinCircle;
import supportGUI.Circle;

public class Experimentation {

	public static int NOMBRE_MAGIQUE = 1664;

	/**
	 * correction
	 * 
	 * @param file
	 */
	private Boolean testCase(File file) {
		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			ArrayList<Point> points = toPoint(file);

			Circle circleFromNaiveImpl = new Utile().solve(points);
			Circle circleFromWelzlImpl = new WelzlAlgorithmForMinCircle().solve(points);

			displayCircle(circleFromNaiveImpl);
			displayCircle(circleFromWelzlImpl);

			// comparaison
			Point c1 = circleFromNaiveImpl.getCenter();
			Point c2 = circleFromWelzlImpl.getCenter();
			int r1 = circleFromNaiveImpl.getRadius();
			int r2 = circleFromWelzlImpl.getRadius();

			return c1.equals(c2) && r1 == r2;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private void displayCircle(Circle c) {
		System.out.println("(" + c.getCenter() + ", " + c.getRadius() + ")");
	}

	private ArrayList<Point> toPoint(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fileReader);
		String line = reader.readLine();
		System.out.println(line);
		ArrayList<Point> res = new ArrayList<>();
		while (line != null) {
			// lecture de la prochaine ligne
			String[] tableau = line.split(" ");
			int x = Integer.parseInt(tableau[0]);
			int y = Integer.parseInt(tableau[1]);
			res.add(new Point(x, y));

			line = reader.readLine();
		}
		return res;
	}

	public ArrayList<File> loadTestFiles() {
		ArrayList<File> testFiles = new ArrayList<>();
		for (Integer i = 2; i <= NOMBRE_MAGIQUE; i++) {
			String path = "./samples/test-" + i.toString() + ".points";
			File file = new File(path);
			testFiles.add(file);
		}

		return testFiles;
	}

	public static void main(String[] args) {

		// correction (par rapport au naif) (c1 c2 identiques ? (centres, rayons))
		// le temps d'execution des deux (nombre_points temp1 temp2)
		Experimentation exp = new Experimentation();
		ArrayList<File> testCases = exp.loadTestFiles();

		//Boolean b = testCases.stream().allMatch(o -> exp.testCase(o));
		//System.out.println(b);

		for (int i = 0; i < testCases.size(); i++) {
			boolean res = exp.testCase(testCases.get(i));
			if (!res) {
				System.out.println((i+2) + ") " + res);	

			}

		}

	}
}
