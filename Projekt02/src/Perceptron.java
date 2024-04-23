public class Perceptron {
	private final double[] weights;
	private double bias;
	private final double weightsLR;
	private final double biasLR;

	public Perceptron(int numAttributes, double wlr, double blr) {
		weights = new double[numAttributes];
		weightsLR = wlr;
		biasLR = blr;
		initializeWeightsAndBias();
	}

	public void test(Data data) {
		int correctPredictions = 0;
		int totalPredictions = 0;
		for (int i = 0; i < data.getAttributes().length; i++) {
			boolean isSetosa = classify(data.getAttributes()[i]);
			totalPredictions++;
			if ((isSetosa && data.getLabels()[i] == 1) || (!isSetosa && data.getLabels()[i] != 1))
				correctPredictions++;
		}

		double accuracy = (double) correctPredictions / data.getAttributes().length * 100;
		System.out.println("Number of classifications: " + totalPredictions);
		System.out.println("Number of correctly classified examples: " + correctPredictions);
		System.out.println("Experiment accuracy: " + accuracy + "%\n");
	}

	public void train(Data data, int limit) {
		double accuracy;
		int iterations = 0;
		do {
			iterations++;
			trainCycle(data);
			System.out.println("Iteration " + iterations + " completed");
			int correctPredictions = 0;
			for (int i = 0; i < data.getAttributes().length; i++) {
				boolean prediction = classify(data.getAttributes()[i]);
				if ((prediction ? 1 : 0) == data.getLabels()[i])
					correctPredictions++;
			}
			accuracy = (double) correctPredictions / data.getAttributes().length * 100;
			System.out.println("Current accuracy: " + accuracy + "%");
		} while (accuracy < 100.0 && iterations < limit);
		System.out.println("\nTraining completed with " + accuracy + "% accuracy after " + iterations + " iterations.");
	}

	private void trainCycle(Data data) {
		for (int i = 0; i < data.getAttributes().length; i++) {
			double[] attributes = data.getAttributes()[i];

			int target = data.getLabels()[i];
			int output = binaryStepFunction(weightedSum(attributes));

			for (int j = 0; j < weights.length; j++) {
				double error = target - output;
				weights[j] += weightsLR * error * attributes[j];
			}
		}
	}

	private boolean classify(double[] attributes) {
		double sum = 0;
		for (int i = 0; i < attributes.length; i++)
			sum += attributes[i] * weights[i];
		return sum > 0;
	}

	private double weightedSum(double[] attributes) {
		double toReturn = 0;
		for (int i = 0; i < attributes.length; i++)
			toReturn += weights[i] * attributes[i];
		return toReturn;
	}

	private void initializeWeightsAndBias() {
		for (int i = 0; i < weights.length; i++)
			weights[i] = Math.random() * 0.1 - 0.05;
		bias = Math.random() * 0.1 - 0.05;
	}

	private int binaryStepFunction(double weightedSum) {
		return (weightedSum >= 0) ? 1 : 0;
	}
}