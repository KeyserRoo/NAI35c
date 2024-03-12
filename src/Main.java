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
		else if (k % 2 == 0)
			k++;
		String[][] test = getData("data\\iris_test.txt");
		String[][] training = getData("data\\iris_training.txt");
		// for (int i = 0; i < training.length; i++) {
		// for (int j = 0; j < training[i].length; j++) {
		// System.out.print(training[i][j]);
		// }
		// System.out.println();
		// }
		findKNearest(test[0], training, k);
	}

	public static String[] findKNearest(String[] test, String[][] training, int k) {
		double[] distances = new double[training.length];
		for (int i = 0; i < training.length; i++) {
			distances[i] = euclideanDistance(test, training[i]);
		}

		double[] copy = Arrays.copyOf(distances, distances.length);
		quickSort(copy, 0, copy.length - 1);

		double[] values = new double[k];
		for (int i = 0; i < values.length; i++) {
			values[i] = copy[i];
		}

		int[] closestIndecies = new int[k];
		int index = 0;
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < distances.length; j++) {
				if (values[i] == distances[j])
					closestIndecies[index++] = j;
			}
		}

		for (int i = 0; i < closestIndecies.length; i++) {
			System.out.println(closestIndecies[i]);
		}

		return null;
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

	public static void quickSort(double[] array, int low, int high) {
		if (low < high) {
			int pivotIndex = partition(array, low, high);
			quickSort(array, low, pivotIndex - 1);
			quickSort(array, pivotIndex + 1, high);
		}
	}

	public static int partition(double[] array, int low, int high) {
		double pivot = array[high];
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (array[j] <= pivot) {
				i++;
				swap(array, i, j);
			}
		}
		swap(array, i + 1, high);
		return i + 1;
	}

	public static void swap(double[] array, int i, int j) {
		double temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}