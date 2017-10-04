package dataset;

import joinery.DataFrame;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GuessWhoDataset {
	
	public static final Map<String, double[]> CHARACTER_MAP = createCharMap();
	public static final Map<String, Double> INPUT_MAP = createInputMap();
    public DataFrame<Object> df;
    public double[][] INPUT, OUTPUT; 
    
    public GuessWhoDataset(String filename){
    	try {
			this.df = DataFrame.readCsv(filename);
		} 
    	catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
    	this.INPUT = createInputArray();
    	this.OUTPUT = createOutputArray();
    }
    
    private static Map<String, double[]> createCharMap(){
    	// maps characters' name to the binary arrays that represent them
    	Map<String, double[]> charMap = new HashMap<String, double[]>();
    	double[] Alex = {0, 0, 0};
    	double[] Alfred = {0, 0, 1};
    	double[] Anita = {0, 1, 0};
    	double[] Anne = {0, 1, 1};
    	double[] Bernard = {1, 0, 0};
    	
        charMap.put("Alex", Alex);
        charMap.put("Alfred", Alfred);
        charMap.put("Anita", Anita);
        charMap.put("Anne", Anne);
        charMap.put("Bernard", Bernard);
        
        return charMap;
    }
    
    private static Map<String, Double> createInputMap(){
    	// maps input to the corresponding number
    	Map<String, Double> charMap = new HashMap<String, Double>();
        charMap.put("Yes", 1.0);
        charMap.put("No", 0.0);
        
        return charMap;
    }
	
	private double[][] createInputArray(){
		double[][] INPUT = new double[this.df.length()][this.df.columns().size()-1];
		String featureInput;
		// generate input array from DataFrame
		for(int i=0;i<this.df.length();i++){
			for(int j=0;j<(this.df.row(i).size()-1);j++){
				featureInput = this.df.row(i).get(j).toString();
				INPUT[i][j] = INPUT_MAP.get(featureInput);
			}
		}
		
		return INPUT;
	}
	
	private double[][] createOutputArray(){
		double[][] OUTPUT = new double[this.df.length()][3];
		String name;
		// generate output array from DataFrame
		for(int i=0;i<this.df.length();i++){
			name = this.df.row(i).get(this.df.columns().size()-1).toString();
			OUTPUT[i] = CHARACTER_MAP.get(name);
		}
		
		return OUTPUT;
	}

}
