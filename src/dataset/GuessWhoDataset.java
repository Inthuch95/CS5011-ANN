package dataset;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import joinery.DataFrame;

public class GuessWhoDataset {
	// create characters map
	public Map<String, double[]> charMap = new HashMap<String,double[]>();
    public DataFrame<Object> df;
    public double[][] INPUT, OUTPUT; 
    
    public GuessWhoDataset(String filename){
    	this.charMap = createCharMap();
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
    
    private Map<String, double[]> createCharMap(){
    	// maps characters' name to the binary arrays that represent them
    	double[] Alex = {0, 0, 0};
    	double[] Alfred = {0, 0, 1};
    	double[] Anita = {0, 1, 0};
    	double[] Anne = {0, 1, 1};
    	double[] Bernard = {1, 0, 0};
    	
        this.charMap.put("Alex", Alex);
        this.charMap.put("Alfred", Alfred);
        this.charMap.put("Anita", Anita);
        this.charMap.put("Anne", Anne);
        this.charMap.put("Bernard", Bernard);
        
        return charMap;
    }
	
	private double[][] createInputArray(){
		double[][] INPUT = new double[this.df.length()][this.df.columns().size()-1];
		for(int i=0;i<this.df.length();i++){
			for(int j=0;j<(this.df.row(i).size()-1);j++){
				if(this.df.row(i).get(j).toString().equals("Yes")){
					INPUT[i][j] = 1;
				}
				else{
					INPUT[i][j] = 0;
				}
			}
		}
		
		return INPUT;
	}
	
	private double[][] createOutputArray(){
		double[][] OUTPUT = new double[this.df.length()][3];
		String name;
		for(int i=0;i<this.df.length();i++){
			name = this.df.row(i).get(this.df.columns().size()-1).toString();
			OUTPUT[i] = this.charMap.get(name);
		}
		
		return OUTPUT;
	}

}
