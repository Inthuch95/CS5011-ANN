package main;

import java.util.Arrays;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;

import ann.NetworkUtil;
import dataset.GuessWhoDataset;

public class Learning3 {

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
		String networkFile = "network_resilient.eg";
		
		// create and train the network with ResilientPropagation
		BasicNetwork network = NetworkUtil.createNetwork(INPUT_UNITS, HIDDEN_UNITS, OUTPUT_UNITS);
		network = resilientPropagationTraining(network, gwd, trainingSet, networkFile);
		
		// reset the network and train using ManhattanPropagation
		network.reset();
		gwd = new GuessWhoDataset(args[0]);
		networkFile = "network_manhattan.eg";
		network = manhattanPropagationTraining(network, gwd, LEARNING_RATE, trainingSet, networkFile);	
	}
	
	private static BasicNetwork resilientPropagationTraining(BasicNetwork network, GuessWhoDataset gwd, 
			MLDataSet trainingSet, String networkFile){
	
		ResilientPropagation train = new ResilientPropagation (network, trainingSet);
		NetworkUtil.trainWithResilientPropagation(train);
		NetworkUtil.saveNetwork(network, networkFile);
		network = NetworkUtil.loadNetwork(networkFile);
		
		// test the network
		String[] guessedChars = NetworkUtil.getNetworkPredictions(gwd.INPUT, network);
		// add network test results to the DataFrame object
		gwd.df.add("Resilient", Arrays.asList(guessedChars));
		int score = NetworkUtil.getClassificationScore(gwd.df);
		double percent = (score / (double)gwd.df.length()) * 100.0;
		
		System.out.println(gwd.df);
		System.out.println("Correctly classified instances: " + 
				Integer.toString(score) + "/" + Integer.toString(gwd.df.length()) + " (" + percent + "%)");
		
		return network;
	}
	
	private static BasicNetwork manhattanPropagationTraining(BasicNetwork network, GuessWhoDataset gwd, 
			double learningRate, MLDataSet trainingSet, String networkFile){
	
		ManhattanPropagation train = new ManhattanPropagation (network, trainingSet, learningRate);
		NetworkUtil.trainWithManhattanPropagation(train);
		NetworkUtil.saveNetwork(network, networkFile);
		network = NetworkUtil.loadNetwork(networkFile);
		
		// test the network
		String[] guessedChars = NetworkUtil.getNetworkPredictions(gwd.INPUT, network);
		// add network test results to the DataFrame object
		gwd.df.add("Manhattan", Arrays.asList(guessedChars));
		int score = NetworkUtil.getClassificationScore(gwd.df);
		double percent = (score / (double)gwd.df.length()) * 100.0;
		
		System.out.println(gwd.df);
		System.out.println("Correctly classified instances: " + 
				Integer.toString(score) + "/" + Integer.toString(gwd.df.length()) + " (" + percent + "%)");
		return network;
	}
	
	private static void checkArgs(String[] args){
		// check command line argument
		if(args.length < 1){
			System.out.println("usage: java -jar Learning3.jar dataset_file");
			System.exit(0);
		}
	}

}
