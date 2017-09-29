import java.io.IOException;

import joinery.DataFrame;

public class PracticalMain {
	
	public static void main(String[] args) {
		try {
			DataFrame df = new DataFrame();
			df = DataFrame.readCsv("char.csv");
			System.out.println(df);
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
