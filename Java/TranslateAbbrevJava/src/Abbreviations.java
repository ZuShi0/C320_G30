import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Abbreviations {
	private static HashMap<String, String> translations = new HashMap<>();
	// private static TreeMap<String, String> translations = new TreeMap<>();
	private static ArrayList<String> data = new ArrayList<>();
	private static ArrayList<Integer> nVals = new ArrayList<>();
	private static ArrayList<Long> tVals = new ArrayList<>();
	private static ArrayList<String> runData = new ArrayList<>();

	public static void main(String[] args) {
		loadWordsFromCSV("processed_abbreviations.csv");
		loadDataFromCSV("content.csv");
		int maxN = data.size();
		int N = 1;

		while (N < maxN) {
			runData.clear();
			nVals.add(N);
			Long start = System.currentTimeMillis();
			run_N_times(N - 1);
			Long end = System.currentTimeMillis();
			tVals.add(end - start);
			N += 50000;
			System.out.println("Run done");
			if (N == 100001) {
				FileWriter fw;
				try {
					fw = new FileWriter("output.csv");
					for (String str : runData) {
						fw.write(str + System.lineSeparator());
					}
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		String nV = "";
		String tV = "";
		for (int i = 0; i < nVals.size(); i++) {
			if (i + 1 == nVals.size()) {
				nV += nVals.get(i).toString();
				tV += tVals.get(i).toString();
			} else {
				nV += nVals.get(i).toString() + ", ";
				tV += tVals.get(i).toString() + ", ";
			}
		}
		System.out.println("N: " + nV);
		System.out.println("T: " + tV);
	}

	private static void run_N_times(int N) {
		for (int i = 0; i <= N; i++) {
			String text = data.get(i);
			runData.add(translateText(text));
		}
	}

	private static void loadDataFromCSV(String fileName) // loads the comma-separated values into the class variable
															// 'translations'
															// 'translations' is a HashMap, where: the key is the word,
															// and the value is the definition of the word.
	{
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;

			while ((line = br.readLine()) != null) {
				data.add(line);
			}
		} catch (Exception e) {
			System.out.println("Error reading from file: " + e.getMessage());
		}
	}

	private static void loadWordsFromCSV(String fileName) // loads the comma-separated values into the class variable
															// 'translations'
															// 'translations' is a HashMap, where: the key is the word,
															// and the value is the definition of the word.
	{
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;

			while ((line = br.readLine()) != null) {
				String[] words = line.split(",");
				translations.put(words[0].toLowerCase(), words[1]);
			}
		} catch (Exception e) {
			System.out.println("Error reading from file: " + e.getMessage());
		}
	}

	private static String translateText(String text) {
		// String[] words = text.split("[!._,@? ]");
		// List<String> words = new ArrayList<>();
		// words.addAll(Arrays.asList(text.split("[!._,@? ]")));
		// List<String> words = Arrays.asList(text.split("[!._,@? ]"));
		HashSet<String> words = new HashSet<>(Arrays.asList(text.split("[!._,@? ]")));

		for (String word : words) {
			String wordLC = word.toLowerCase();
			String translation = translations.get(wordLC);
			if (translation != null) {

				text = text.replaceAll("(?i)\\b" + word + "\\b", translation);
			}
		}

		return text;
	}
}
