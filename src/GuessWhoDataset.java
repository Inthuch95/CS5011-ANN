import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import joinery.DataFrame;

public class GuessWhoDataset {
	// create characters map
	public static final Map<String, double[]> charMap = createCharMap();
    private static Map<String, double[]> createCharMap(){
    	// maps characters' name to the binary arrays that represent them
    	Map<String,double[]> charMap = new HashMap<String,double[]>();
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
    
	/**
	 * Import dataset from csv file and pre-process input and outputs appropriately
	 * @param filename The name of dataset file
	 * @return DataFrame object containing the data from guess who dataset
	 */
	public static DataFrame<Object> readDataset(String filename){
		DataFrame<Object> df = new DataFrame<>();
		try {
			String processedFile = filename.substring(0, filename.length()-4) 
					+ "_processed.csv";
			// check for pre-processed dataset
			boolean processed = checkDataset(processedFile);
			if(processed){
				df = DataFrame.readCsv(processedFile);
			}
			else{
				// pre-process the data and save it to the new csv file
				df = DataFrame.readCsv(filename);
				df = encodeInputOutput(df);
				df.writeCsv(filename.substring(0, filename.length()-4) 
						+ "_processed.csv");
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		return df;
	}
	
	public static boolean checkDataset(String processedFile){
		boolean processed = false;
		File f = new File(processedFile);
		// check if pre-processed dataset exist or not
		if(f.exists() && !f.isDirectory()) { 
		    processed = true;
		}
		
		return processed;
	}
	
	public static DataFrame<Object> encodeInputOutput(DataFrame<Object> df){
		System.out.println("Encoding dataset...");
		// replace Yes/No with 0/1
		for(int i=0;i<df.length();i++){
			for(int j=0;j<(df.row(i).size()-1);j++){
				switch(df.row(i).get(j).toString()){
					case "Yes":
						df.set(i, j, 1);
						break;
					case "No":
						df.set(i, j, 0);
						break;
					default:
						System.out.println("Invalid input!");
						break;
				}
			}
		}
		System.out.println("Encoding completed!");
		
		return df;
	}
	
	public static double[][] createInputArray(DataFrame<Object> df){
		double[][] INPUT = new double[df.length()][df.columns().size()-1];
		for(int i=0;i<df.length();i++){
			for(int j=0;j<(df.row(i).size()-1);j++){
				INPUT[i][j] = Double.parseDouble(df.row(i).get(j).toString());
			}
		}
		
		return INPUT;
	}
	
	public static double[][] createOutputArray(DataFrame<Object> df){
		double[][] OUTPUT = new double[df.length()][3];
		String name;
		for(int i=0;i<df.length();i++){
			name = df.row(i).get(df.columns().size()-1).toString();
			OUTPUT[i] = charMap.get(name);
		}
		
		return OUTPUT;
	}

}
