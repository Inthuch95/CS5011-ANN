import java.io.File;
import java.io.IOException;
import joinery.DataFrame;

public class Dataset {

	public Dataset() {
		// TODO Auto-generated constructor stub
	}
	
	public static DataFrame<Object> readDataset(String filename){
		DataFrame<Object> df = new DataFrame<>();
		try {
			String processedFile = filename + "_processed";
			// check for pre-processed dataset
			boolean processed = checkDataset(processedFile);
			if(processed){
				df = DataFrame.readCsv(processedFile);
			}
			else{
				df = DataFrame.readCsv(filename);
				df = encodeInputOutput(df);
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
					default:
						break;
				}
			}
		}
		System.out.println("Encoding completed!");
		
		return df;
	}

}
