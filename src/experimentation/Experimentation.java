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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algorithms.detection.Utile;
import algorithms.detection.WelzlAlgorithmForMinCircle;
import supportGUI.Circle;

public class Experimentation {
	private Executor executor = Executors.newFixedThreadPool(1664);
	private ComparaisonResult[] comparaisons = new ComparaisonResult[1663];
	private ComparaisonResult[] comparaisonsUnFichier = new ComparaisonResult[256];

	public static int NOMBRE_MAGIQUE = 1664;
	public static String SAMPLES_PATH = "./experimentation/samples/test-";
	public static String RESULTS_PATH = "./experimentation/resultats/duration.data";
	public static String RESULTS_CROISS_PATH = "./experimentation/resultats/durationCrois.data";
	int counter = 0;

	public void paralleleRunUnFichier() throws IOException {
		System.out.println("un seul fichier");
		clearResult2();
		File resultFile = new File(RESULTS_CROISS_PATH);
		File testFile = new File(SAMPLES_PATH + "2.points");
		// charger points
		ArrayList<Point> points = toPoint(testFile);
		// pour chaque i allant de 1 à 256
		FileWriter fileWriter = new FileWriter(resultFile, true);
		BufferedWriter writer = new BufferedWriter(fileWriter);
		for (int nbPoints = 1; nbPoints <= 256; nbPoints++) {
			// collecte
			ArrayList<Point> collectedPoints = new ArrayList<Point>(points.subList(0, nbPoints));
			ComparaisonResult r = runAlgos(collectedPoints);
			writer.write(r.getPointsCount() + " " + r.getNaiveDuration() + " " + r.getWelzlDuration());
			writer.newLine();
		}
		System.out.println("un seul fichier terminer");
		writer.close();
	}

	private ComparaisonResult runAlgos(ArrayList<Point> points) {
		long t10 = System.nanoTime();
		Circle circleFromNaiveImpl = new Utile().solve(points);
		long t11 = System.nanoTime();

		long t20 = System.nanoTime();
		Circle circleFromWelzlImpl = new WelzlAlgorithmForMinCircle().solve(points);
		long t21 = System.nanoTime();

		long duration1 = (t11 - t10);
		long duration2 = (t21 - t20);

		ComparaisonResult result = new ComparaisonResult(points.size(), duration1, duration2, circleFromNaiveImpl,
				circleFromWelzlImpl);
		return result;
	}

	private void clearResult2() {
		File resultFile = new File(RESULTS_CROISS_PATH);
		resultFile.delete();
	}

	public void paralelleRun() throws IOException, InterruptedException {
		System.out.println("debut experience");
		clearResultFiles();
		File[] testCases = paralelleLoadTestFiles();
		Thread.sleep(2_000);
		for (int i = 0; i < testCases.length; i++) {

		}
		System.out.println("fin lecture fichiers");
		for (int i = 0; i < testCases.length; i++) {
			final int j = i;
			executor.execute(() -> {
				comparaisons[j] = testCaseBis(testCases[j]);
			});
		}

		Thread.sleep(60_000); // attendre la fin du calcul
		System.out.println("fin calcul");

		// write in file
		FileWriter fileWriter = new FileWriter(RESULTS_PATH, true);
		BufferedWriter writer = new BufferedWriter(fileWriter);
		for (int i = 0; i < testCases.length; i++) {
			ComparaisonResult r = comparaisons[i];
			if (r == null)
				throw new AssertionError("comparaison result is not null: wait more time");

			writer.write(r.getNaiveDuration() + " " + r.getWelzlDuration());
			writer.newLine();

		}
		writer.close();
		System.out.println("fin experience");

	}

	private File[] paralelleLoadTestFiles() {
		File[] testFiles = new File[1663]; // 0, 1662

		for (Integer i = 2; i <= NOMBRE_MAGIQUE; i++) {
			final int j = i;

			executor.execute(() -> {
				String path = SAMPLES_PATH + j + ".points";
				File file = new File(path);
				testFiles[j - 2] = file;
			});

		}

		return testFiles;
	}

	private ComparaisonResult testCaseBis(File file) {
		if (file == null)
			throw new AssertionError("fichier null");
		FileReader fileReader;
		try {
			ArrayList<Point> points = toPoint(file);

			ComparaisonResult result = runAlgos(points);
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void run() {
		// clearResultFiles();
		ArrayList<File> testCases = loadTestFiles();
		int i = 0;
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

			long t10 = System.nanoTime();
			Circle circleFromNaiveImpl = new Utile().solve(points);
			long t11 = System.nanoTime();

			long t20 = System.nanoTime();
			Circle circleFromWelzlImpl = new WelzlAlgorithmForMinCircle().solve(points);
			long t21 = System.nanoTime();

			long duration1 = (t11 - t10);
			long duration2 = (t21 - t20);
			System.out.println(duration1 + " vs " + duration2);

			// comparaison
			Point c1 = circleFromNaiveImpl.getCenter();
			Point c2 = circleFromWelzlImpl.getCenter();
			int r1 = circleFromNaiveImpl.getRadius();
			int r2 = circleFromWelzlImpl.getRadius();

			// export data
			// write in file
			/*
			 * FileWriter fileWriter = new FileWriter(RESULTS_PATH, true); BufferedWriter
			 * writer = new BufferedWriter (fileWriter); writer.write(points.size() + " " +
			 * duration1 + " " + duration2); writer.newLine();
			 * 
			 * 
			 * writer.close();
			 */

			return c1.equals(c2) && r1 == r2;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	private void displayCircle(Circle c) {
		// System.out.println("(" + c.getCenter() + ", " + c.getRadius() + ")");
	}

	private ArrayList<Point> toPoint(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fileReader);
		String line = reader.readLine();
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
		try {
			exp.paralelleRun();
			exp.paralleleRunUnFichier();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}

class ComparaisonResult {
	private int pointsCount;
	private long naiveDuration;
	private long welzlDuration;
	private Circle naiveCircle;
	private Circle wlzlCircle;

	public ComparaisonResult(int pointsCount, long naiveDuration, long welzlDuration, Circle naiveCircle,
			Circle wlzlCircle) {
		super();
		this.pointsCount = pointsCount;
		this.naiveDuration = naiveDuration;
		this.welzlDuration = welzlDuration;
		this.naiveCircle = naiveCircle;
		this.wlzlCircle = wlzlCircle;
	}

	public Circle getNaiveCircle() {
		return naiveCircle;
	}

	public Circle getWlzlCircle() {
		return wlzlCircle;
	}

	public int getPointsCount() {
		return pointsCount;
	}

	public long getNaiveDuration() {
		return naiveDuration;
	}

	public long getWelzlDuration() {
		return welzlDuration;
	}

}
