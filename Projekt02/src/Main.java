import java.util.*;
import java.nio.file.*;

public class Main {
	static int _userBasedTries = 0;
	static int _userBasedSucceses = 0;

	public static void main(String[] args) {

		double[][] test = normalizeFeatures(convertToDoubleArray(getData("data\\iris_test.txt")));
		int[] testLabels = binarizeLabels(getLabels("data\\iris_test.txt"));

		double[][] training = normalizeFeatures(convertToDoubleArray(getData("data\\iris_training.txt")));
		int[] trainingLabels = binarizeLabels(getLabels("data\\iris_training.txt"));

		Perceptron perceptron = new Perceptron(test[0].length, 0.1);
		perceptron.train(training, trainingLabels, 15);
		System.out.println(perceptron.evaluate(test, testLabels));

	}

	public static int[] binarizeLabels(String[] data) {
		int[] labels = new int[data.length];
		for (int i = 0; i < data.length; i++) {
			labels[i] = data[i].toLowerCase().equals("iris-setosa") ? 1 : 0;
		}
		return labels;
	}

	public static String[] getLabels(String filePath) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			String[] labels = new String[lines.size()];

			int i = 0;
			for (String item : lines) {
				String[] temp = item.split("\t");
				labels[i] = temp[temp.length - 1];
				i++;
			}

			return labels;
		} catch (Exception e) {
			e.printStackTrace();
			return new String[0];
		}
	}

	public static double[][] convertToDoubleArray(String[][] input) {
    int rows = input.length; // Exclude the last row
    int cols = input[0].length - 1;
    double[][] result = new double[rows][cols];

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            result[i][j] = Double.parseDouble(input[i][j]);
        }
    }

    return result;
}

public static double[][] normalizeFeatures(double[][] data) {
	double[][] normalizedData = new double[data.length][data[0].length];
	for (int i = 0; i < data[0].length; i++) {
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			for (int j = 0; j < data.length; j++) {
					min = Math.min(min, data[j][i]);
					max = Math.max(max, data[j][i]);
			}
			for (int j = 0; j < data.length; j++) {
					normalizedData[j][i] = (data[j][i] - min) / (max - min);
			}
	}
	return normalizedData;
}

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
