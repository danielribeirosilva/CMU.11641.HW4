import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class Recommender {

	public static void main(String[] argv) throws IOException {
		
		if(argv.length < 3){
			System.err.println("Error: please specify test, train and output files on argv.");
			System.exit(1);
		}
		
		String trainFile = argv[0];
		String testFile = argv[1];
		String outputFile = argv[2];
		
		
		//EXPERIMENT 1 - USER-USER SIMILARITY
		
		int k = 50; //number of neighbors in k-NN
		
		//Read training file and map data, indexing by user
		System.out.println("reading training data ...");
		HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap = new HashMap<Long, LinkedList<InteractionInfoItem>>();
		try {
			trainUserItemInteractionMap = FileInteraction.readAndMapTrainFileIndexedByUser (trainFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		//Read test file and map data, indexing by user
		System.out.println("reading testing data ...");
		HashMap<Long, LinkedList<Long>> testUserItemMap = new HashMap<Long, LinkedList<Long>>();
		try {
			testUserItemMap = FileInteraction.readAndMapTestFile(testFile, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// For each user in test set, compute user-user similarity with previous users
		// and get top K users for each of these test users
		System.out.println("doing user-user similiarity recommendation ...");
		
		int count = 0;
		
		//compute prediction for each user and item in the test file
		HashMap<Long, HashMap<Long, Double>> ratingPredictions = new HashMap<Long, HashMap<Long, Double>>(); 
		for(long testUser : testUserItemMap.keySet()){
			
			//get top users for this user
			LinkedList<InteractionInfoItem> testUserFV = trainUserItemInteractionMap.get(testUser);
			PriorityQueue<IdSimilarityPair> topNeighbors = new PriorityQueue<IdSimilarityPair>();
			for(long neighborUser : trainUserItemInteractionMap.keySet()){
				if(testUser==neighborUser) continue;
				
				LinkedList<InteractionInfoItem> neighborUserFV = trainUserItemInteractionMap.get(neighborUser);
				
				//compute similarity
				double similarity = UserSimilarity.dotProductSimilarity(testUserFV, neighborUserFV);
				//double similarity = UserSimilarity.strictDotProductSimilarity(testUserFV, neighborUserFV);
				//double similarity = UserSimilarity.cosineSimilarity(testUserFV, neighborUserFV);
				
				IdSimilarityPair pair = new IdSimilarityPair(neighborUser, similarity);
				//if good enough, add similarity to PriorityQueue
				if(topNeighbors.size() < k){
					topNeighbors.add(pair);
				}
				else{
					if(topNeighbors.peek().similarity < similarity){
						topNeighbors.poll();
						topNeighbors.add(pair);
					}
				}
			}
			
			//put top neighbors in list
			LinkedList<IdSimilarityPair> topNeighborsList = new LinkedList<IdSimilarityPair>();
			while(!topNeighbors.isEmpty()){
				topNeighborsList.add(topNeighbors.poll());
			}
			
			//Compute prediction rating for required items
			HashMap<Long, Double> currentUserPredictions = new HashMap<Long, Double>();
			for(long itemId : testUserItemMap.get(testUser) ){
				//double itemRatingPrediction = ItemPrediction.simpleAveragePrediction(itemId, topNeighborsList, trainUserItemInteractionMap);
				double itemRatingPrediction = RatingPrediction.weightedAveragePrediction(itemId, topNeighborsList, trainUserItemInteractionMap);
				currentUserPredictions.put(itemId, itemRatingPrediction);

				//System.out.println("user: "+testUser+" item: "+itemId+" predicition: "+itemRatingPrediction);
			}
			
			ratingPredictions.put(testUser, currentUserPredictions);
			
			count++;
			if(count%10==0) System.out.println(count+"/"+testUserItemMap.size());	
		}
		
		//write predictions to output file in same order
		System.out.println("writing predictions to file... ");
		FileInteraction.writePredictionsToFile(testFile, "predictionsExp1.txt", ratingPredictions);
		

	}

}