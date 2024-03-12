import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
 
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        if (k <= 0)
            k = 3;
        String[][] test = getData("data\\iris_test.txt");
        String[][] training = getData("data\\iris_training.txt");
        analyze(test, training, k);
    }
 
    public static void analyze(String[][] test, String[][] training, int k){
		int i = 0;
		for (String[] element : test) {
			System.out.println(i+": "+getResult(kNearestIndecies(element, training, k), training));
			i++;
		}
	}
 
    public static String getResult(int[] indecies, String[][] table) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Iris-setosa", 0);
        map.put("Iris-versicolor", 0);
        map.put("Iris-virginica", 0);
 
        for (int index: indecies) {
            String key = table[index][table[0].length-1];
            map.put(key, map.getOrDefault(key, 0) + 1);
        }
		int max = 0;
		String toReturn="";
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if(entry.getValue()>max) {
				max = entry.getValue();
				toReturn = entry.getKey();
			}
        }
        return toReturn;
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
 
        return closestIndecies;
    }
 
    public static double euclideanDistance(String[] test, String[] training) {
        double[] temp = new double[test.length - 1];
        for (int i = 0; i < test.length - 1; i++)
            temp[i] = Math.pow(Double.parseDouble(test[i]) - Double.parseDouble(training[i]), 2);
 
        double distance = 0;
        for (int i = 0; i < temp.length; i++)
            distance += temp[i];
 
        return Math.sqrt(distance);
    }
 
    public static String[][] getData(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            // int noCols = lines.getFirst().split("\t").length;
            int noCols = lines.get(0).split("\t").length;
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