public class Main {

	public static void main(String[] args) {
		String trainFolder = "data\\trainData";
		Data trainData = Data.extractData(trainFolder);

		String testFolder = "data\\testData";
		Data testData = Data.extractData(testFolder);

		Layer layer = new Layer(trainData.getLanguages());

		layer.train(trainData);
		layer.test(testData);

		GUI gui = new GUI(layer);
		gui.run();
	}
}
