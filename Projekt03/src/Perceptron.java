import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Perceptron {
	private static final Map<String, Function<Double, Double>> functions = Map.of(
			"identity", d -> d,
			"binaryStep", d -> d >= 0 ? 1.0 : 0.0,
			"relu", d -> Math.max(0, d),
			"sigmoid", d -> 1 / (1 + Math.exp(-d)),
			"tanh", d -> Math.tanh(d),
			"gelu", d -> 0.5 * d * (1 + Math.tanh(Math.sqrt(2 / Math.PI) * (d + 0.044715 * Math.pow(d, 3)))),
			"reluDerivative", d -> d > 0 ? 1.0 : 0.0,
			"sigmoidDerivative", d -> {
				double sigmoidValue = 1 / (1 + Math.exp(-d));
				return sigmoidValue * (1 - sigmoidValue);
			},
			"tanhDerivative", d -> 1 - Math.pow(Math.tanh(d), 2));

	private final String targetLanguage;

	private final double[] weights;
	private final double weightsLR;

	// private double bias;
	// private final double biasLR;

	public Perceptron(String tar, int numAtr, double wlr, double blr) {
		targetLanguage = tar;

		weightsLR = wlr;
		// biasLR = blr;

		weights = new double[numAtr];
		initializeWeightsAndBias();
	}

	public void train(List<Data.FileContent> dataList, int epochs) {
		double threshold = 0.01;
		double globalError = Double.MAX_VALUE;
		while (epochs-- > 0 && globalError > threshold) {
			double currentError = 0.0;
			for (Data.FileContent item : dataList) {
				double target = item.getLabel().equals(targetLanguage) ? 1.0 : -1.0;
				double output = functions.get("identity").apply(weightedSum(item.getPercentages()));
				currentError = target - output;
				globalError += Math.abs(currentError);

				for (int j = 0; j < weights.length; j++) {
					weights[j] = weights[j] + (weightsLR * currentError * item.getPercentages()[j]);
				}
				// bias = bias + biasLR * currentError;
			}
			normalizeWeights();
			globalError = currentError / dataList.size();
		}
	}

	public String getTarget() {
		return targetLanguage;
	}

	public double classify(Data.FileContent fileContent) {
		double sum = weightedSum(fileContent.getPercentages());
    return functions.get("identity").apply(sum);
	}

	public double classify(double[] item) {
		double sum = weightedSum(item);
    return functions.get("identity").apply(sum);
	}

	private double weightedSum(double[] attributes) {
		double toReturn = 0;
		for (int i = 0; i < attributes.length; i++)
			toReturn += weights[i] * attributes[i];
		return toReturn;
	}

	private void normalizeWeights() {
		double sumOfSquares = 0.0;
		for (double weight : weights) {
			sumOfSquares += weight * weight;
		}
		double magnitude = Math.sqrt(sumOfSquares);
		for (int i = 0; i < weights.length; i++) {
			weights[i] /= magnitude;
		}
	}

	private void initializeWeightsAndBias() {
		for (int i = 0; i < weights.length; i++)
			weights[i] = Math.random() * 0.1 - 0.05;
		// bias = Math.random() * 0.1 - 0.05;
	}
}