import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class UserBasedCF {
	public int k;
	public String trainFile; 
	public String testFile;
	public String outputFile;

	public UserBasedCF(int k, String trainFile, String testFile, String outputFile){
		this.k = k;
		this.trainFile = trainFile;
		this.testFile = testFile;
		this.outputFile = outputFile;
	}
	
	public void generateRecommendations(String similarityType, String predictionType) throws IOException{
		
		//long startTime, endTime;
		
		assert(similarityType.equals("dotProduct") || similarityType.equals("strictDotProduct") || similarityType.equals("cosine"));
		assert(predictionType.equals("mean") || predictionType.equals("weighted"));
		
		System.out.println("\nUser-based CF using "+similarityType+" similarity for top "+this.k+" neighbors");
		
		//Read training file and map data, indexing by user
		System.out.println("reading training data ...");
		HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap = FileInteraction.readAndMapTrainFileIndexedByUser (trainFile);
		HashMap<Long, Double> usersAvgRating = new HashMap<Long, Double>();
		HashMap<Long, HashMap<Long, Integer>> userItemRatingMap = FileInteraction.generateUserItemRatingMapFromTrainFile(trainFile);
		
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
		
		//pre-compute users' average ratings
		for(Long user : trainUserItemInteractionMap.keySet()){
			double sumOfRatings = 0d;
			for(InteractionInfoItem iii : trainUserItemInteractionMap.get(user)){
				sumOfRatings += iii.rating;
			}
			double avgRating = sumOfRatings / trainUserItemInteractionMap.get(user).size();
			usersAvgRating.put(user, avgRating);
		}
		
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
				
				double similarity = 0d;
				if(similarityType.equals("dotProduct")){
					similarity = UserSimilarity.dotProductSimilarity(testUserFV, neighborUserFV);
				}
				else if(similarityType.equals("strictDotProduct")){
					similarity = UserSimilarity.strictDotProductSimilarity(testUserFV, neighborUserFV);
				}
				else if(similarityType.equals("cosine")){
					similarity = UserSimilarity.cosineSimilarity(testUserFV, neighborUserFV);
				}
				
				IdSimilarityPair pair = new IdSimilarityPair(neighborUser, similarity);
				//if good enough, add similarity to PriorityQueue
				if(topNeighbors.size() < this.k){
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
				
				double itemRatingPrediction = 0d;
				if(predictionType.equals("mean")){
					itemRatingPrediction = RatingPrediction.userBasedSimpleAveragePrediction(itemId, topNeighborsList, userItemRatingMap, usersAvgRating);
				}
				else if(predictionType.equals("weighted")){
					itemRatingPrediction = RatingPrediction.userBasedWeightedAveragePrediction(itemId, topNeighborsList, trainUserItemInteractionMap);
				}
				
				
				currentUserPredictions.put(itemId, itemRatingPrediction);
			}
			
			ratingPredictions.put(testUser, currentUserPredictions);
			
			count++;
			if(count%100==0) System.out.println(count+"/"+testUserItemMap.size());	
		}
		
		//write predictions to output file in same order
		System.out.println("writing predictions to file ... ");
		FileInteraction.writePredictionsToFile(testFile, outputFile, ratingPredictions);
		System.out.println("process complete");
	}
	
	
	
	
	
	public void printUsersTopKNeighbors(long targetUser, String similarityType){
		
		assert(similarityType.equals("dotProduct") || similarityType.equals("strictDotProduct") || similarityType.equals("cosine"));
		
		//Read training file and map data, indexing by user
		System.out.println("Top "+this.k+" Neighbors for User "+targetUser+" using "+similarityType+" similarity");
		HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap = new HashMap<Long, LinkedList<InteractionInfoItem>>();
		try {
			trainUserItemInteractionMap = FileInteraction.readAndMapTrainFileIndexedByUser (trainFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		//get top users for this user
		LinkedList<InteractionInfoItem> testUserFV = trainUserItemInteractionMap.get(targetUser);
		PriorityQueue<IdSimilarityPair> topNeighbors = new PriorityQueue<IdSimilarityPair>();
		for(long neighborUser : trainUserItemInteractionMap.keySet()){
			if(targetUser==neighborUser) continue;
			
			LinkedList<InteractionInfoItem> neighborUserFV = trainUserItemInteractionMap.get(neighborUser);
			
			//compute similarity
			double similarity = 0d;
			if(similarityType.equals("dotProduct")){
				similarity = UserSimilarity.dotProductSimilarity(testUserFV, neighborUserFV);
			}
			else if(similarityType.equals("strictDotProduct")){
				similarity = UserSimilarity.strictDotProductSimilarity(testUserFV, neighborUserFV);
			}
			else if(similarityType.equals("cosine")){
				similarity = UserSimilarity.cosineSimilarity(testUserFV, neighborUserFV);
			}
			
			IdSimilarityPair pair = new IdSimilarityPair(neighborUser, similarity);
			//if good enough, add similarity to PriorityQueue
			if(topNeighbors.size() < this.k){
				topNeighbors.add(pair);
			}
			else{
				if(topNeighbors.peek().similarity < similarity){
					topNeighbors.poll();
					topNeighbors.add(pair);
				}
			}
		}
		
		//put top K neighbors in desc order in array
		int pos = Math.min(this.k, topNeighbors.size());
		IdSimilarityPair[] topK = new IdSimilarityPair[pos];
		while(!topNeighbors.isEmpty()){
			topK[--pos] = topNeighbors.poll();
		}
		for(IdSimilarityPair neighbor : topK){
			System.out.println("id: "+neighbor.id + " - similarity: "+neighbor.similarity);
		}
		
	}
}
