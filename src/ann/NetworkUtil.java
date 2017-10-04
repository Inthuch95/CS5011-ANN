package ann;

import static org.encog.persist.EncogDirectoryPersistence.saveObject;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import dataset.GuessWhoDataset;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class NetworkUtil {
	
	public static BasicNetwork createNetwork(int inputUnits, int hiddenUnits, 
			int outputUnits){
		BasicNetwork network = new BasicNetwork();
		// input Layer
		network.addLayer(new BasicLayer(null, true, inputUnits));
		// hidden Layer
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,hiddenUnits));
		// output Layer
		network.addLayer(new BasicLayer(new ActivationSigmoid(),false,outputUnits));
		network.getStructure().finalizeStructure();
		network.reset();
		
		return network;
	}
	
	public static void trainWithBackpropagation(Backpropagation train){
		//train the network
		int epoch = 1;
		do {
		train.iteration();
		System.out.println("Epoch #" + epoch + " Error:" + train.getError());
		epoch++;
		} while(train.getError() > 0.01);
		train.finishTraining();
	}
	
	public static void saveNetwork(BasicNetwork network, String filename){
		//save the network
		saveObject(new File(filename), network);
	}
	
	public static void saveNetworkTest(GuessWhoDataset guessWho, String[] guessedChars){
		guessWho.df.add("Network output", Arrays.asList(guessedChars));
		try {
			guessWho.df.writeCsv("Network_Test.csv");
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static String[] getNetworkPredictions(double[][]INPUT, 
			BasicNetwork network, double threshold){
		// compute data role by role
		double[][] predictions = new double[INPUT.length][3];
		String[] predictionsStr = new String[INPUT.length];
		for(int i=0;i<INPUT.length;i++){
			double[] input = INPUT[i];
			MLData data = new BasicMLData(input);
			MLData output = network.compute(data);
			for(int j=0;j<output.size();j++){
				if(output.getData(j) >= threshold){
					predictions[i][j] = 1;
				}
				else{
					predictions[i][j] = 0;
				}
			}
			if(Arrays.equals(predictions[i], GuessWhoDataset.charMap.get("Alex"))){
				predictionsStr[i] = "Alex";
			}
			else if(Arrays.equals(predictions[i], GuessWhoDataset.charMap.get("Alfred"))){
				predictionsStr[i] = "Alfred";
			}
			else if(Arrays.equals(predictions[i], GuessWhoDataset.charMap.get("Anita"))){
				predictionsStr[i] = "Anita";
			}
			else if(Arrays.equals(predictions[i], GuessWhoDataset.charMap.get("Anne"))){
				predictionsStr[i] = "Anne";
			}
			else if(Arrays.equals(predictions[i], GuessWhoDataset.charMap.get("Bernard"))){
				predictionsStr[i] = "Bernard";
			}
			else{
				predictionsStr[i] = "Unknown character!";
			}
		}
		
		return predictionsStr;
	}
}
