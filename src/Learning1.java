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
			df = Dataset.readDataset(args[0]);
			System.out.println(df);
		}
		catch(ArrayIndexOutOfBoundsException e){
			System.out.println("usage: java -jar Learning1.jar filename");
			System.exit(0);
		}
	}

}
