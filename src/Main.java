import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int k = scanner.nextInt();
		if (k <= 0)
			k = 3;
		String[][] test = getData("data\\iris_test.txt");
		String[][] training = getData("data\\iris_training.txt");
		// for (int i = 0; i < training.length; i++) {
		// for (int j = 0; j < training[i].length; j++) {
		// System.out.print(training[i][j]);
		// }
		// System.out.println();
		// }
		kNearestIndecies(test[0], training, k);
	}

	public static int[] kNearestIndecies(String[] test, String[][] training, int k) {

		double[][] distancesWithIndices = new double[training.length][2];
		for (int i = 0; i < training.length; i++) {
			distancesWithIndices[i][0] = euclideanDistance(test, training[i]);
			distancesWithIndices[i][1] = i;
		}

		Arrays.sort(distancesWithIndices, (a, b) -> Double.compare(a[0], b[0]));

		int[] closestIndecies = new int[k];
		for (int i = 0; i < k; i++) {
			closestIndecies[i] = (int) distancesWithIndices[i][1];
		}

		for (int i : closestIndecies) {
			System.out.println(i);
		}

		return closestIndecies;
	}

	public static double euclideanDistance(String[] test, String[] trainig) {
		double[] temp = new double[test.length - 1];
		for (int i = 0; i < test.length - 1; i++) {
			temp[i] = Math.pow(Double.parseDouble(test[i]) - Double.parseDouble(trainig[i]), 2);
		}
		double distance = 0;
		for (int i = 0; i < temp.length; i++) {
			distance += temp[i];
		}
		return Math.sqrt(distance);
	}

	public static String[][] getData(String filePath) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			int noCols = lines.getFirst().split("\t").length;
			int noRows = lines.size();

			String[][] toReturn = new String[noRows][noCols];
			for (int i = 0; i < noRows; i++) {
				toReturn[i] = lines.get(i).replace(",", ".").split("\t");
			}

			return toReturn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}