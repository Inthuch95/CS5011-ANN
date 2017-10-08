package main;

import java.util.Arrays;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import ann.NetworkUtil;
import dataset.GuessWhoDataset;

public class Learning1 {
	
	public static void main(String[] args) {
		// make sure that we have required command line argument
		checkArgs(args);
		
		// read dataset from csv file and create training data
		GuessWhoDataset gwd = new GuessWhoDataset(args[0]);
		MLDataSet trainingSet = new BasicMLDataSet(gwd.INPUT, gwd.OUTPUT);
		
		// declare network configurations and parameters
	    final int INPUT_UNITS = gwd.df.columns().size() - 1; // characters' features
		final int HIDDEN_UNITS = 9; // 9 is the best so far
		final int OUTPUT_UNITS = 3; // characters' binary code
		final double LEARNING_RATE = 0.7;
		final double MOMENTUM = 0.0;
		final String NETWORK_FILENAME = "network.eg";
		
		// create and train the network
		BasicNetwork network = NetworkUtil.createNetwork(INPUT_UNITS, HIDDEN_UNITS, OUTPUT_UNITS);
		Backpropagation train = new Backpropagation (network, trainingSet, LEARNING_RATE, MOMENTUM);
		NetworkUtil.trainWithBackpropagation(train);
		NetworkUtil.saveNetwork(network, NETWORK_FILENAME);
		network = NetworkUtil.loadNetwork(NETWORK_FILENAME);
		
		// test the network
		String[] guessedChars = NetworkUtil.getNetworkPredictions(gwd.INPUT, network);
		// add network test results to the DataFrame object
		gwd.df.add("Network output", Arrays.asList(guessedChars));
		int score = NetworkUtil.getClassificationScore(gwd.df);
		
		System.out.println(gwd.df);
		System.out.println("Correctly classified instances: " + 
		Integer.toString(score) + "/" + Integer.toString(gwd.df.length()));
	}
	
	private static void checkArgs(String[] args){
		// check command line argument
		if(args.length < 1){
			System.out.println("usage: java -jar Learning1.jar dataset_file");
			System.exit(0);
		}
	}

}
