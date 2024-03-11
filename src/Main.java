import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int k = scanner.nextInt();
		String[][] test = getData("data\\iris_test.txt");
		String[][] training = getData("data\\iris_training.txt");
		for (int i = 0; i < training.length; i++) {
			for (int j = 0; j < training[i].length; j++) {
				System.out.print(training[i][j]);
			}
			System.out.println();
		}
		// KNN(test, training, k);
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

	public static String[] KNN(String[][] test, String[][] training, int k){
		
		return null;
	}

	public static int euclideanDistance(String[] a, String[] b,int columns){
		int distance=0;
			for (int i = 0; i < columns; i++) {
				distance += Math.sqrt(Integer.parseInt(a[i])-Integer.parseInt(b[i]));
			}
		return distance;
	}
}