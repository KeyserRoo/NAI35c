import java.text.Normalizer;

public class Layer {
	private final Perceptron[] perceptrons;

	public Layer(String[] langs) {
		perceptrons = new Perceptron[langs.length];
		for (int i = 0; i < langs.length; i++) {
			perceptrons[i] = new Perceptron(langs[i], 26, 0.1, 0.1);
		}
	}

	public void train(Data trainData) {
		for (int i = 0; i < perceptrons.length; i++) {
			perceptrons[i].train(trainData.getData().get(perceptrons[i].getTarget()), 10);
		}
	}

	public void test(Data testData) {
		double total = 0.0, correct = 0.0;
		for (Data.FileContent fileContent : testData.getTestingList()) {
			double maxOutput = Double.NEGATIVE_INFINITY;
			String predictedLanguage = null;
			for (int i = 0; i < perceptrons.length; i++) {
				double output = perceptrons[i].classify(fileContent);
				if (output > maxOutput) {
					maxOutput = output;
					predictedLanguage = perceptrons[i].getTarget();
				}
			}
			System.out.println("Predicted: " + predictedLanguage + " Actual: " + fileContent.getLabel());
			if (predictedLanguage.equals(fileContent.getLabel()))
				correct += 1;
			total += 1;
		}
		System.out.println("Poprawnosc: " + ((correct / total) * 100));
	}

	public String classifyText(String text) {

		String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
		String noSpecialLetters = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		String noL = noSpecialLetters.replaceAll("ł", "l");
		String justLetters = noL.replaceAll("[^a-zA-Z]", "");
		String toProcess = justLetters.toLowerCase();

		double[] percentages = new double[26];
		int[] characters = new int[26];
		for (int i = 0; i < toProcess.length(); i++) {
			characters[toProcess.charAt(i) - 'a']++;
		}
		int sum = 0;
		for (int i = 0; i < characters.length; i++) {
			sum += characters[i];
		}
		for (int i = 0; i < percentages.length; i++) {
			String formatted = String.format("%.4f", ((double) characters[i] / sum));
			percentages[i] = Double.parseDouble(formatted.replaceAll(",", "."));
		}

		double maxOutput = Double.NEGATIVE_INFINITY;
		String predictedLanguage = null;
		for (Perceptron perceptron : perceptrons) {
			double output = perceptron.classify(percentages);
			if (output > maxOutput) {
				maxOutput = output;
				predictedLanguage = perceptron.getTarget();
			}
		}
		return predictedLanguage;
	}
}