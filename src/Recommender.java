import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;


public class Recommender {

	public static void main(String[] argv) {
		
		if(argv.length < 3){
			System.err.println("Error: please specify test, train and output files on argv.");
			System.exit(1);
		}
		
		String trainFile = argv[0];
		String testFile = argv[1];
		String outputFile = argv[2];
		
		/*
		//Read test file and map data
		HashMap<Long, HashSet<Long>> testUserItemMap = new HashMap<Long, HashSet<Long>>();
		try {
			testUserItemMap = FileReader.readAndMapTestFile(testFile, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		*/
		
		//Read training file and map data
		HashMap<Long, HashMap<Long, InteractionInfo>> trainUserItemInteractionMap = new HashMap<Long, HashMap<Long, InteractionInfo>>();
		try {
			trainUserItemInteractionMap = FileReader.readAndMapTrainFile (trainFile, false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		

	}

}