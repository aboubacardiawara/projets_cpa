/**
 
 - Duration Test:
 	pointsCount duration1 duration2
 
 - Correction
 	remarques: quantifier ??
 	
 */

package experimentation;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.rmi.server.ExportException;
import java.sql.Time;
import java.util.ArrayList;

import algorithms.detection.Utile;
import algorithms.detection.WelzlAlgorithmForMinCircle;
import supportGUI.Circle;

public class Experimentation {

	public static int NOMBRE_MAGIQUE = 1664;
	public static String SAMPLES_PATH = "./experimentation/samples/test-";
	public static String RESULTS_PATH= "./experimentation/resultats/duration.data";
	int counter = 0;

	private void run() {
		clearResultFiles();
		
		ArrayList<File> testCases = loadTestFiles();
		for (File file : testCases) {
			testCase(file); // new result
		}
	
	}
	
	private void clearResultFiles() {
		File resultFile = new File(RESULTS_PATH);
		resultFile.delete();
		
	}

	private Boolean testCase(File file) {
		FileReader fileReader;
		try {
			ArrayList<Point> points = toPoint(file);

			long t10 = System.currentTimeMillis();
			Circle circleFromNaiveImpl = new Utile().solve(points);
			long t11 = System.currentTimeMillis();

			long t20 = System.currentTimeMillis();
			Circle circleFromWelzlImpl = new WelzlAlgorithmForMinCircle().solve(points);
			long t21 = System.currentTimeMillis();

			displayCircle(circleFromNaiveImpl);
			displayCircle(circleFromWelzlImpl);

			// comparaison
			Point c1 = circleFromNaiveImpl.getCenter();
			Point c2 = circleFromWelzlImpl.getCenter();
			int r1 = circleFromNaiveImpl.getRadius();
			int r2 = circleFromWelzlImpl.getRadius();

			// export data
			// write in file
			FileWriter fileWriter = new FileWriter(RESULTS_PATH, true);			
			BufferedWriter writer = new BufferedWriter (fileWriter);
			writer.write(">>>>>>");
			writer.newLine();
			
			writer.close();
			
			System.out.println("duration: " + (t11 - t10));

			return c1.equals(c2) && r1 == r2;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private void displayCircle(Circle c) {
		//System.out.println("(" + c.getCenter() + ", " + c.getRadius() + ")");
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
			String path = SAMPLES_PATH + i.toString() + ".points";
			File file = new File(path);
			testFiles.add(file);
		}

		return testFiles;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Experimentation exp = new Experimentation();
		exp.run();

	}
}
