package game;

import static org.encog.persist.EncogDirectoryPersistence.loadObject;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import dataset.GuessWhoDataset;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class GuessWhoGame {
	public static final Map<String, String> FEATURE_MAP = createFeatureMap();
	public static final Map<String, Double> USER_INPUT_MAP = createUserInputMap();
	public static final Map<String, double[]> CHARACTER_MAP = GuessWhoDataset.CHARACTER_MAP;
	public static final double THRESHOLD = 0.5;
	public BasicNetwork network;
	
	public GuessWhoGame(String filename){
		// load the network from a file
		this.network = (BasicNetwork)loadObject(new File(filename));
	}
	
	private static Map<String, String> createFeatureMap(){
		// maps feature to corresponding question
		Map<String, String> FEATURE_MAP = new LinkedHashMap<String, String>();
		FEATURE_MAP.put("Curly hair", "Does your character have curly hair?");
		FEATURE_MAP.put("Blond", "Is your character blond?");
		FEATURE_MAP.put("Red Cheecks", "Does your character have red cheeks?");
		FEATURE_MAP.put("Moustache", "Does your character have a moustache?");
		FEATURE_MAP.put("Beard", "Does your character have a beard?");
		FEATURE_MAP.put("ear rings", "Does your character wear ear rings?");
		FEATURE_MAP.put("female", "Is your character female?");
		FEATURE_MAP.put("Tattoo", "Does your character have a tattoo?");
		
		return FEATURE_MAP;
	}
	
	private static Map<String, Double> createUserInputMap(){
		Map<String, Double> USER_INPUT_MAP = new HashMap<String, Double>();
		USER_INPUT_MAP.put("yes", 1.0);
		USER_INPUT_MAP.put("no", 0.0);
		
		return USER_INPUT_MAP;
	}
	
	public void startGame(){
		String guessedChar;
		
		System.out.println("Welcome to Guess Who!\n");
		// ask the questions and guess the character
		double[] features = this.collectFeaturesFromUser();
		System.out.println("Network input: " + Arrays.toString(features));
		System.out.println("Let me think...\n");
		guessedChar = this.guessCharacter(features);
		System.out.println("It's " + guessedChar + "!");
	}
	
	private double[] collectFeaturesFromUser(){
		double[] features = new double[FEATURE_MAP.size()];
		Scanner scanner = new Scanner(System.in);
		int i = 0;
		for(String feature : FEATURE_MAP.keySet()){ 
			// ask questions about the characters
			System.out.println(FEATURE_MAP.get(feature));
			String userInput = scanner.next();
			try{
				features[i] = USER_INPUT_MAP.get(userInput.toLowerCase());
			}
			catch(NullPointerException e){
				System.out.println("I can only accept 'Yes' or 'No'");
				System.exit(0);
			}
			i++;
		}
		scanner.close();
		
		return features;
	}
	
	private String guessCharacter(double[] features){
		String guessedChar="";
		double[] netOutput = new double[this.network.getOutputCount()];
		MLData data = new BasicMLData(features);
		MLData output = this.network.compute(data);
		// use neural network to compute output
		for(int i=0;i<output.size();i++){
			if(output.getData(i) >= THRESHOLD){
				netOutput[i] = 1.0;
			}
			else{
				netOutput[i] = 0.0;
			}
		}
		// retrieve character name
		for(Map.Entry<String, double[]> entry : CHARACTER_MAP.entrySet()){
			if(Arrays.equals(netOutput, entry.getValue())){
				guessedChar = entry.getKey();
				break;
			}
		}
		
		return guessedChar;
	}
	
}
