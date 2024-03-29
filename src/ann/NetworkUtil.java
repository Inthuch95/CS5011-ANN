package ann;

import static org.encog.persist.EncogDirectoryPersistence.loadObject;
import static org.encog.persist.EncogDirectoryPersistence.saveObject;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import dataset.GuessWhoDataset;
import joinery.DataFrame;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class NetworkUtil {
	
	private static final double THRESHOLD = 0.5;
	
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
	
	public static void trainWithResilientPropagation(ResilientPropagation train){
		//train the network
		int epoch = 1;
		do {
		train.iteration();
		System.out.println("Epoch #" + epoch + " Error:" + train.getError());
		epoch++;
		} while(train.getError() > 0.01);
		train.finishTraining();
	}
	
	public static void trainWithManhattanPropagation(ManhattanPropagation train){
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
	
	public static BasicNetwork loadNetwork(String filename){
		// load the network
		BasicNetwork network = new BasicNetwork();
		network = (BasicNetwork)loadObject(new File(filename));
		
		return network;
	}
	
	public static void saveNetworkTest(GuessWhoDataset guessWho, String filename){
		try {
			guessWho.df.writeCsv(filename);
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static String[] getNetworkPredictions(double[][]INPUT, BasicNetwork network){
		// compute network output role by role
		double[][] predictions = new double[INPUT.length][3];
		String[] predictionsStr = new String[INPUT.length];
		
		for(int i=0;i<INPUT.length;i++){
			double[] input = INPUT[i];
			MLData data = new BasicMLData(input);
			MLData output = network.compute(data);
			// assign 1 if output >= 0.5
			// otherwise assign 0 
			for(int j=0;j<output.size();j++){
				if(output.getData(j) >= THRESHOLD){
					predictions[i][j] = 1.0;
				}
				else{
					predictions[i][j] = 0.0;
				}
			}
			// get the corresponding character name
			for(Map.Entry<String, double[]> entry : GuessWhoDataset.CHARACTER_MAP.entrySet()){
				if(Arrays.equals(predictions[i], entry.getValue())){
					predictionsStr[i] = entry.getKey();
					break;
				}
			}
			if(predictionsStr[i] == null){
				predictionsStr[i] = "Unknown character";
			}
		}
		
		return predictionsStr;
	}
	
	public static int getClassificationScore(DataFrame df){
		int score = 0;
		String observed, predicted; 
		for(int i=0;i<df.length();i++){
			observed = df.row(i).get(df.row(i).size()-2).toString();
			predicted = df.row(i).get(df.row(i).size()-1).toString();
			if(observed.equals(predicted)){
				score = score + 1;
			}
		}
		
		return score;
	}
}
