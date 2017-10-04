import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import static org.encog.persist.EncogDirectoryPersistence.saveObject;

import ann.NetworkUtil;
import dataset.GuessWhoDataset;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Learning1 {
	
	public static void main(String[] args) {
		// make sure that we have required command line argument
		checkArgs(args);
		// read dataset from csv file and create training data
		GuessWhoDataset gwd = new GuessWhoDataset(args[0]);
		MLDataSet trainingSet = new BasicMLDataSet(gwd.INPUT, gwd.OUTPUT);
		
		int inputUnits = gwd.df.columns().size() - 1; // characters' features
		int hiddenUnits = 9; // 9 is the best so far
		int outputUnits = 3; // characters' binary code
		BasicNetwork network = NetworkUtil.createNetwork(inputUnits, hiddenUnits, 
				outputUnits);
		
		// initialize parameters, train the network, and save network to a file
		double learningRate = 0.7;
		double momentum = 0.0;
		Backpropagation train = new Backpropagation (network, trainingSet, 
				learningRate, momentum);
		NetworkUtil.trainWithBackpropagation(train);
		String filename = "Trained_ANN.eg";
		NetworkUtil.saveNetwork(network, filename);
		
		// test the network
		double threshold = 0.5;
		String[] guessedChars = NetworkUtil.getNetworkPredictions(gwd.INPUT, 
				network, threshold);
		// save network test results to csv file
		NetworkUtil.saveNetworkTest(gwd, guessedChars);
		
		System.out.println(gwd.df);
	}
	
	private static void checkArgs(String[] args){
		// check command line argument
		if(args.length < 1){
			System.out.println("usage: java -jar Learning1.jar dataset_file");
			System.exit(0);
		}
	}

}
