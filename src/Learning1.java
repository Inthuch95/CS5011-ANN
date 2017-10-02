import java.util.Arrays;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

import joinery.DataFrame;

public class Learning1 {
	
	public static void main(String[] args) {
		DataFrame<Object> df = new DataFrame<>();
		try{
			// import csv dataset using joinery library
			df = GuessWhoDataset.readDataset(args[0]);
			System.out.println(df);
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.out.println("usage: java -jar Learning1.jar filename");
			System.exit(0);
		}
		
		// training data
		double[][] INPUT = GuessWhoDataset.createInputArray(df);
		double[][] OUTPUT = GuessWhoDataset.createOutputArray(df);
		MLDataSet trainingSet = new BasicMLDataSet(INPUT, OUTPUT);
		
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
		
//		double[] h = new double[]{0,0};
//		MLData data = new BasicMLData(h);
//		MLData output = network.compute(data);
//		System.out.println("input =" + data.getData(0) + " " + data.getData(1));
//		System.out.println("actual = " + output.getData(0));
	}

}
