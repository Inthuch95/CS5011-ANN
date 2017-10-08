package test;

import org.encog.neural.networks.BasicNetwork;

import ann.NetworkUtil;
import dataset.GuessWhoDataset;

public class EarlyGuessTest {

	public static void main(String[] args) {
		final String NETWORK_FILENAME = "network_extended.eg";
		// read dataset from csv file and create training data
		GuessWhoDataset gwd = new GuessWhoDataset("test data/char_early.csv");
		BasicNetwork network = NetworkUtil.loadNetwork(NETWORK_FILENAME);
		
		// test the network
		String[] guessedChars = NetworkUtil.getNetworkPredictions(gwd.INPUT, network);
		// save network test results to csv file
		NetworkUtil.saveNetworkTest(gwd, guessedChars, "early_guess_test.csv");
		int score = NetworkUtil.getClassificationScore(gwd.df);
		double percent = (score / (double)gwd.df.length()) * 100.0;
		
		System.out.println(gwd.df);
		System.out.println("Correctly classified instances: " + 
				Integer.toString(score) + "/" + Integer.toString(gwd.df.length()) + " (" + percent + "%)");
	}

}
