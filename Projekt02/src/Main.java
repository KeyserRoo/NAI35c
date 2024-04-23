public class Main {

	public static void main(String[] args) {
		String trainPath = "data/iris_training.txt";
		String testPath = "data/iris_test.txt";
		String[] names = new String[] { "Iris-setosa" };
		Data trainData = Data.loadData(trainPath, names);
		Data testData = Data.loadData(testPath, names);

		printData(trainData);
		printData(testData);

		double weightsLR = 0.01;
		double biasLR = 0.01;

		Perceptron perceptron = new Perceptron(trainData.getAttributes()[0].length, weightsLR, biasLR);

		System.out.println("Training the perceptron...");
		perceptron.train(trainData, 15);

		System.out.println("\n\nTesting the perceptron on test data:");
		perceptron.test(testData);
	}

	public static void printData(Data data) {
		for (int i = 0; i < data.getAttributes().length; i++) {
			for (int j = 0; j < data.getAttributes()[i].length; j++) {
				System.out.print(data.getAttributes()[i][j] + " ");
			}
			System.out.println(data.getLabels()[i]);
		}
		System.out.println("\n\n");
	}
}
