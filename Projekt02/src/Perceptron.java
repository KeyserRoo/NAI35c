public class Perceptron {
	private double[] weights;
	private double bias;
	private double learningRate;

	public Perceptron(int numFeatures, double learningRate) {
		this.weights = new double[numFeatures];
		for (int i = 0; i < numFeatures; i++) {
			this.weights[i] = Math.random() - 0.5;
		}
		this.bias = Math.random() - 0.5;
		this.learningRate = learningRate;
	}

	private int activationFunction(double sum) {
		return sum > 0 ? 1 : 0;
	}

	public int predict(double[] features) {
		double sum = 0;
		for (int i = 0; i < features.length; i++) {
			sum += features[i] * weights[i];
		}
		sum += bias;
		return activationFunction(sum);
	}

	public void train(double[][] trainingData, int[] labels, int maxIterations) {
		for (int iteration = 0; iteration < maxIterations; iteration++) {
			boolean converged = true;
			for (int i = 0; i < trainingData.length; i++) {
				double[] inputs = trainingData[i];
				int prediction = predict(inputs);
				if (prediction != labels[i]) {
					converged = false;
					for (int j = 0; j < weights.length; j++) {
						weights[j] += (labels[i] - prediction) * inputs[j] * learningRate;
					}
					bias += (labels[i] - prediction) * learningRate;
				}
			}
			if (converged) {
				break;
			}
		}
	}

	public double evaluate(double[][] testData, int[] testLabels) {
		int correctPredictions = 0;
		for (int i = 0; i < testData.length; i++) {
			double[] inputs = testData[i];
			if (predict(inputs) == testLabels[i]) {
				correctPredictions++;
			}
		}
		return (double) correctPredictions / testData.length;
	}
}