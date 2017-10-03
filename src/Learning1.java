import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

import ann.NetworkUtil;

import static org.encog.persist.EncogDirectoryPersistence.saveObject;

import joinery.DataFrame;
import dataset.GuessWhoDataset;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Learning1 {
	
	public static void main(String[] args) {
		if(args.length < 1){
			System.out.println("usage: java -jar Learning1.jar dataset_file");
			System.exit(0);
		}
		
		GuessWhoDataset guessWho = new GuessWhoDataset(args[0]);
		System.out.println(guessWho.df);
		
		// training data
		MLDataSet trainingSet = new BasicMLDataSet(guessWho.INPUT, guessWho.OUTPUT);
		
		int inputUnits = 7; // characters' features
		int hiddenUnits = 9; // 9 is the best so far
		int outputUnits = 3; // characters' binary code
		
		BasicNetwork network = new BasicNetwork();
		// input Layer
		network.addLayer(new BasicLayer(null, true, inputUnits));
		// hidden Layer
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,hiddenUnits));
		// output Layer
		network.addLayer(new BasicLayer(new ActivationSigmoid(),false,outputUnits));
		network.getStructure().finalizeStructure();
		network.reset();
		
		double learningRate = 0.7;
		double momentum = 0.0;
		Backpropagation train = new Backpropagation (network, trainingSet, learningRate, momentum);
		
		//train the network
		int epoch = 1;
		do {
		train.iteration();
		System.out.println("Epoch #" + epoch + " Error:" + train.getError());
		epoch++;
		} while(train.getError() > 0.01);
		train.finishTraining();
		
		//save the network
		String filename = "Trained_ANN.eg";
		saveObject(new File(filename), network);
		
		// test the network
		String[] guessedChars = NetworkUtil.getNetworkPredictions(guessWho.INPUT, network);
		guessWho.df.add("Network output", Arrays.asList(guessedChars));
		try {
			guessWho.df.writeCsv("Network_Test.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println(guessWho.df);
	}

}
