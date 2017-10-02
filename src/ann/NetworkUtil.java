package ann;

import java.util.Arrays;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;

public class NetworkUtil {
	// create characters map
	private static double[] Alex = {0, 0, 0};
	private static double[] Alfred = {0, 0, 1};
	private static double[] Anita = {0, 1, 0};
	private static double[] Anne = {0, 1, 1};
	private static double[] Bernard = {1, 0, 0};	
	
	public static String[] getNetworkPredictions(double[][]INPUT, BasicNetwork network){
		// TODO compute data role by role
		double[][] predictions = new double[INPUT.length][3];
		String[] predictionsStr = new String[INPUT.length];
		for(int i=0;i<INPUT.length;i++){
			double[] input = INPUT[i];
			MLData data = new BasicMLData(input);
			MLData output = network.compute(data);
			for(int j=0;j<output.size();j++){
				if(output.getData(j) >= 0.5){
					predictions[i][j] = 1;
				}
				else{
					predictions[i][j] = 0;
				}
			}
			if(Arrays.equals(predictions[i], Alex)){
				predictionsStr[i] = "Alex";
			}
			else if(Arrays.equals(predictions[i], Alfred)){
				predictionsStr[i] = "Alfred";
			}
			else if(Arrays.equals(predictions[i], Anita)){
				predictionsStr[i] = "Anita";
			}
			else if(Arrays.equals(predictions[i], Anne)){
				predictionsStr[i] = "Anne";
			}
			else if(Arrays.equals(predictions[i], Bernard)){
				predictionsStr[i] = "Bernard";
			}
			else{
				predictionsStr[i] = "Unknown character!";
			}
		}
		
		return predictionsStr;
	}
}
