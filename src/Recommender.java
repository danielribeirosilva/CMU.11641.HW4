import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;


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
		
		/*
		class IIUComparator implements Comparator<InteractionInfoUser>{

			@Override
			public int compare(InteractionInfoUser a, InteractionInfoUser b) {
				if(a.getUserId() < b.getUserId()) return -1;
				if(a.getUserId() > b.getUserId()) return 1;
				return 0;
			}
			
		}
		*/
		
		//Read training file and map data
		HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap = new HashMap<Long, LinkedList<InteractionInfoItem>>();
		try {
			trainUserItemInteractionMap = FileReader.readAndMapTrainFileIndexedByUser (trainFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		int limit = 0;
		for(long user : trainUserItemInteractionMap.keySet()){
			
			LinkedList<InteractionInfoItem> pq = trainUserItemInteractionMap.get(user);
			Iterator<InteractionInfoItem> it = pq.iterator();
			
			while(it.hasNext()){
				System.out.println(user + ": " + it.next());
			}
			
			limit++;
			if(limit>1)break;
		}

		

	}

}