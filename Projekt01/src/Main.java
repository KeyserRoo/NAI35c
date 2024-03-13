import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
	static int _userBasedTries = 0;
	static int _userBasedSucceses = 0;

	public static void main(String[] args) {
		String[][] test = getData("data\\iris_test.txt");
		String[][] training = getData("data\\iris_training.txt");
		Scanner scanner = new Scanner(System.in);
		int k = scanner.nextInt();
		if (k <= 0) {
			k = 1;
			System.out.println("parametr nie moze byc mniejszy od 0, ustawiono na 1");
		}
		if (k > training.length) {
			k = 120;
			System.out.println("parametr nie moze byc wiekszy od 120, ustawiono na 120");
		}
		analyzeFromFile(test, training, k);
		analyzeFromUser(scanner, training, k);
		scanner.close();
	}

	/**
 * Interactively Analyzes KNN algorithm's performance with user-provided data.
 *
 * This method allows the user to input a new observation and its type, then uses the KNN algorithm to classify the observation 
 * by comparing given classification to its prediction
 * 
 * @param scanner A Scanner object for reading user input.
 * @param training A 2D array containing the training dataset, used for comparisons.
 * @param k Parameter of how many closest neighbours to consider
 */
	public static void analyzeFromUser(Scanner scanner, String[][] training, int k) {
		String newPointAtributes = "";
		String newPointType = "";

		newPointAtributes = scanner.nextLine();
		while (!newPointAtributes.equals("quit")) {
			System.out.println("\n\nPodaj " + (training[0].length - 1)
					+ " parametrów reprezentowanych liczbami zmiennoprzecinkowymi oddzielonych spacją");
			newPointAtributes = scanner.nextLine();

			String[] userInput = newPointAtributes.replace(",", ".").split("\\s+");
			if (userInput.length != (training[0].length - 1)) {
				System.out.println("Niepoprawne dane!\nSpróbuj jeszcze raz!");
				continue;
			}

			System.out.println("\nPodaj numer typu kwiatu:\n1: Iris-setosa\n2: Iris-versicolor\n3: Iris-virginica");
			switch (scanner.nextLine()) {
				case "1", "Iris-setosa" -> newPointType = "Iris-setosa";
				case "2", "Iris-versicolor" -> newPointType = "Iris-versicolor";
				case "3", "Iris-virginica" -> newPointType = "Iris-virginica";
				default -> {
					System.out.println("Niepoprawne dane!\nSpróbuj jeszcze raz!");
					continue;
				}
			}

			String knnAnswer = getResult(kNearestIndecies(userInput, training, k), training);

			System.out.println("Rodzaj kwiatu podany przez uzytkownika: " + newPointType + "\nOcena algorytmu: " + knnAnswer);
			if (newPointType.equals(knnAnswer))
				_userBasedSucceses++;
			_userBasedTries++;
			System.out.println("Poprawność algorytmu: " + _userBasedSucceses + "/" + _userBasedTries);
		}
	}

	/**
 * Performs KNN algorithm for given files and then analyzes how successful it was
 * 
 * @param test A 2D array representing tested data, the last column represents classification
 * @param training A 2D array containing the training dataset
 * @param k Parameter of how many closest neighbours to consider
 */
	public static void analyzeFromFile(String[][] test, String[][] training, int k) {
		int comparisons = 0, successes = 0;
		String[][] trimmedTest = trimArray(test);
		for (int i = 0; i < trimmedTest.length; i++) {
			String knnAnswer = getResult(kNearestIndecies(trimmedTest[i], training, k), training);
			if (knnAnswer.equals(test[i][test[i].length - 1].trim()))
			successes++;
			comparisons++;
		}
		System.out.println("Poprawnie zakwalifikowane przykłady: " + successes);
		System.out.println("Liczba porównanych kwiatów: " + comparisons);
		double percentage = (double) successes / comparisons * 100;
		System.out.println("Poprawność algorytmu:  " + String.format("%.2f", percentage) + "%");
	}

	/**
 * Determines how many of each species was among nearest neighbours
 *
 * This method iterates over the given indices, extracts the species name from the last column of the table,
 * and counts the occurrences of each species. It then returns the species with the highest count.
 *
 * @param indecies An array of nearest indecies.
 * @param table A 2D array where each row represents an observation and the last column contains the species name.
 * @return The species name with the highest count among the given indices.
 */
	public static String getResult(int[] indecies, String[][] table) {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("Iris-setosa", 0);
		map.put("Iris-versicolor", 0);
		map.put("Iris-virginica", 0);

		for (int index : indecies) {
			String key = table[index][table[0].length - 1];
			map.put(key, map.getOrDefault(key, 0) + 1);
		}
		int max = 0;
		String toReturn = "";
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				toReturn = entry.getKey();
			}
		}
		return toReturn;
	}

	/**
 * Calculates the indices of the k nearest observations to a given observation based on Euclidean distance.
 *
 * This method computes the Euclidean distance between the given observation and each observation in the compareToData array.
 * It then sorts these distances and returns the indices of the k observations with the smallest distances.
 *
 * @param observation The observation for which to find the nearest neighbors.
 * @param compareToData A 2D array containing the observations to compare against. Each row represents an observation.
 * @param k The number of how many nearest neighbors to find.
 * @return An array of indices representing rows from training data.
 */
	public static int[] kNearestIndecies(String[] observation, String[][] compareToData, int k) {

		double[][] distancesWithIndices = new double[compareToData.length][2];
		for (int i = 0; i < compareToData.length; i++) {
			distancesWithIndices[i][0] = euclideanDistance(observation, compareToData[i]);
			distancesWithIndices[i][1] = i;
		}

		Arrays.sort(distancesWithIndices, (a, b) -> Double.compare(a[0], b[0]));

		int[] closestIndecies = new int[k];
		for (int i = 0; i < k; i++) {
			closestIndecies[i] = (int) distancesWithIndices[i][1];
		}

		return closestIndecies;
	}

	/**
	 * Calculates distance using unnormalized Euclidean distance equation
	 * d(p,q) = sqrt ( sum( pow ( qi - pi , 2
	 * ) ) )
	 */
	public static double euclideanDistance(String[] observation, String[] compareToData) {
		double[] temp = new double[observation.length];
		for (int i = 0; i < observation.length; i++)
			temp[i] = Math.pow(Double.parseDouble(observation[i]) - Double.parseDouble(compareToData[i]), 2);

		double distance = 0;
		for (int i = 0; i < temp.length; i++)
			distance += temp[i];

		return Math.sqrt(distance);
	}

	/**
 * Trims array, (in this case used to get rid of decision attribute)
 *
 * @param arr The original 2D array.
 * @return A new 2D array with the last column removed.
 */
	public static String[][] trimArray(String[][] arr) {
		int rows = arr.length;
		int columns = arr[0].length;

		String[][] trimmed = new String[rows][columns - 1];

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns - 1; j++)
				trimmed[i][j] = arr[i][j];

		return trimmed;
	}

	/**
 * Trims array, (in this case used to get rid of decision attribute)
 *
 * @param filePath relative path to file
 * @return 2D string array that represents data in file, structure: rows -> columns
 */
	public static String[][] getData(String filePath) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			int noCols = lines.get(0).split("\t").length;
			int noRows = lines.size();

			String[][] toReturn = new String[noRows][noCols];
			for (int i = 0; i < noRows; i++) {
				toReturn[i] = lines.get(i).replace(",", ".").split("\t");
			}

			return toReturn;
		} catch (Exception e) {
			e.printStackTrace();
			return new String[0][0];
		}
	}
}