package game;

import static org.encog.persist.EncogDirectoryPersistence.loadObject;
import org.encog.neural.networks.BasicNetwork;
import dataset.GuessWhoDataset;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GuessWhoGame {
	public static final Map<String, String> featMap = createFeatureMap();
	public static final Map<String, double[]> charMap = GuessWhoDataset.charMap;
	public BasicNetwork network;
	
	public GuessWhoGame(String filename){
		this.network = (BasicNetwork)loadObject(new File(filename));
	}
	
	private static Map<String, String> createFeatureMap(){
		// maps feature to corresponding question
		Map<String, String> featMap = new HashMap<String, String>();
		featMap.put("Curly hair", "Does your character have curly hair?");
		featMap.put("Blond", "Is your character blond?");
		featMap.put("Red Cheecks", "Does your character have red cheeks?");
		featMap.put("Moustache", "Does your character have a moustache?");
		featMap.put("Beard", "Does your character have a beard?");
		featMap.put("ear rings", "Does your character wear ear rings?");
		featMap.put("female", "Is your character female?");
		
		return featMap;
	}
}
