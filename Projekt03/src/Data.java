import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
	private Map<String, List<FileContent>> contentsInDirectory = new HashMap<>();

	public Map<String, List<FileContent>> getData() {
		return contentsInDirectory;
	}

	public String[] getLanguages() {
		return contentsInDirectory.keySet().stream().toArray(String[]::new);
	}

	public List<Data.FileContent> getTestingList() {
		List<Data.FileContent> toReturn = new ArrayList<Data.FileContent>();

		for (Map.Entry<String, List<FileContent>> entry : contentsInDirectory.entrySet()) {
			toReturn.addAll(entry.getValue());
		}

		return toReturn;
	}

	public static Data extractData(String path) {
		Data toReturn = new Data();
		try {
			Path startPath = Paths.get(path);

			String[] subfolders = Files.walk(startPath)
					.filter(Files::isDirectory)
					.filter(spath -> !spath.equals(startPath))
					.map(Path::getFileName)
					.map(Path::toString)
					.toArray(String[]::new);

			for (String language : subfolders) {
				toReturn.getData().put(language, new ArrayList<>());
			}

			Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					String language = file.getParent().getFileName().toString();
					String content = new String(Files.readAllBytes(file));

					FileContent element = new FileContent(content, language);
					toReturn.getData().get(language).add(element);

					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toReturn;
	}

	static class FileContent {
		private final double[] percentages = new double[26];
		private final String label;

		private FileContent(String content, String lab) {
			label = lab;

			String normalized = Normalizer.normalize(content, Normalizer.Form.NFD);
			String noSpecialLetters = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			String noL = noSpecialLetters.replaceAll("ł", "l");
			String justLetters = noL.replaceAll("[^a-zA-Z]", "");
			String toProcess = justLetters.toLowerCase();

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
		}

		public String getLabel() {
			return label;
		}

		public double[] getPercentages() {
			return percentages;
		}
	}

}