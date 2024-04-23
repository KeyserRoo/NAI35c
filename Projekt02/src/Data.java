import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    private double[][] attributes;
    private int[] labels;

    public static Data loadData(String path,String[] names) {
        List<double[]> attributesList = new ArrayList<>();
        List<String> labelsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\t");
                double[] attributes = new double[parts.length - 1];

                for (int i = 0; i < attributes.length; i++)
                    attributes[i] = Double.parseDouble(parts[i].replace(",", "."));

                attributesList.add(attributes);
                labelsList.add(parts[parts.length - 1].trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double[][] atr = attributesList.toArray(new double[0][]);
        int[] lab = Data.convertLabelsToIndices(labelsList.toArray(new String[0]), names);
        return new Data(atr, lab);
    }

    public double[][] getAttributes() {
        return Arrays.copyOf(attributes, attributes.length);
    }

    public int[] getLabels() {
        return Arrays.copyOf(labels, labels.length);
    }

    private static int[] convertLabelsToIndices(String[] labels, String[] names) {
        Map<String, Integer> nameToIndexMap = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            nameToIndexMap.put(names[i], i+1);
        }

        int[] toReturn = new int[labels.length];
        for (int i = 0; i < labels.length; i++) {
            Integer index = nameToIndexMap.get(labels[i]);
            if (index != null)
                toReturn[i] = index;
            else
                toReturn[i] = 0;
        }
        return toReturn;
    }

    private Data(double[][] a, int[] l) {
        attributes = a;
        labels = l;
    }
}