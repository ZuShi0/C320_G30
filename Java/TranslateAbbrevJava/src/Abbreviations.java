import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Abbreviations {
	private static HashMap<String, String> translations = new HashMap<>();
	private static ArrayList<String> data = new ArrayList<>();
	private static ArrayList<Integer> nVals = new ArrayList<>();
	private static ArrayList<Long> tVals = new ArrayList<>();
	private static ArrayList<String> runData = new ArrayList<>();

	/*
	 * This program reads a dictionary of abbreviations and their translations 
	 * from a CSV file and stores it in a HashMap. The program then loops through
	 * the data (in this case content.csv) and translates the abbreviations and
	 * acronyms in each string into their full words.
	 */
	public static void main(String[] args) {
		loadWordsFromCSV("processed_abbreviations.csv"); // Loads translation dictionary
		loadDataFromCSV("content.csv"); // Loads data to be translated
		int maxN = data.size();
		int N = 1;

		// Loops through the data at increasing N values to determine time complexity
		while (N < maxN) {
			runData.clear();
			nVals.add(N);
			Long start = System.currentTimeMillis();
			run_N_times(N - 1);
			Long end = System.currentTimeMillis();
			tVals.add(end - start);
			N += 50000;
			System.out.println("Run done");

			// Writes the translated data at this iteration to view results
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
		// Prints the n and time values to the console so they can be graphed
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

	// Function that runs the N times to translate the data
	private static void run_N_times(int N) {
		for (int i = 0; i <= N; i++) {
			String text = data.get(i);
			runData.add(translateText(text)); 
		}
	}

	// loads the comma-separated values into the class variable
	// 'translations'
	// 'translations' is a HashMap, where: the key is the word,
	// and the value is the definition of the word.
	private static void loadDataFromCSV(String fileName)
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

	// loads the comma-separated values into the class variable
	// 'translations'
	// 'translations' is a HashMap, where: the key is the word,
	// and the value is the definition of the word.
	private static void loadWordsFromCSV(String fileName)
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

	// Translates abbreviations and acronyms into full words
	private static String translateText(String text) {
		
		// Store individual words in a HashSet to prevent duplicates
		HashSet<String> words = new HashSet<>(Arrays.asList(text.split("[!._,@? ]")));

		// Loop through each word in the HashSet
		for (String word : words) {
			String wordLC = word.toLowerCase();
			String translation = translations.get(wordLC);
			if (translation != null) { // If the word is in the HashMap, replace it with the translation

				text = text.replaceAll("(?i)\\b" + word + "\\b", translation); 
			}
		}

		return text; // Return the translated text
	}
}
