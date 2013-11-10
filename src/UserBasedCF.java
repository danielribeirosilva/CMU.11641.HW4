import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
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
	
	public void generateRecommendations(String similarityType, String predictionType) throws IOException, ParseException{
		
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		
		assert(similarityType.equals("dotProduct") || similarityType.equals("cosine") || similarityType.equals("normalizedDotProduct") || similarityType.equals("normalizedCosine") || similarityType.equals("pearson") || similarityType.equals("custom") );
		assert(predictionType.equals("mean") || predictionType.equals("weighted") || predictionType.equals("pearson") || predictionType.equals("custom"));
		
		System.out.println("\nUser-based CF using "+predictionType+" and "+similarityType+" similarity for top "+this.k+" neighbors");
		
		//Read training file and map data, indexing by user
		System.out.println("reading training data (indexing by user) ...");
		HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap = FileInteraction.readAndMapTrainFileIndexedByUser (trainFile);
		HashMap<Long, Double> usersAvgRating = new HashMap<Long, Double>();
		HashMap<Long, HashMap<Long, Integer>> userItemRatingMap = FileInteraction.generateUserItemRatingMapFromTrainFile(trainFile);
		HashMap<Long, HashMap<Long, RatingDatePair>> userItemRatingDateMap = FileInteraction.generateUserItemRatingDatePairMapFromTrainFile(trainFile);
		
		//Read training file and map data, indexing by item
		System.out.println("reading training data (indexing by item) ...");
		HashMap<Long, Double> itemsAvgRating = new HashMap<Long, Double>();
		HashMap<Long, LinkedList<InteractionInfoUser>> trainItemUserInteractionMap = FileInteraction.readAndMapTrainFileIndexedByItem (trainFile);
		
		//Read test file and map data, indexing by user
		System.out.println("reading testing data ...");
		HashMap<Long, LinkedList<Long>> testUserItemMap = FileInteraction.readAndMapTestFile(testFile, true);
		
		 //global average rating
		 double globalRatingAvg = DataStatistics.getGlobalRatingAverage(trainFile);
		
		//pre-compute items' average ratings
		for(Long item : trainItemUserInteractionMap.keySet()){
			double sumOfRatings = 0d;
			for(InteractionInfoUser iiu : trainItemUserInteractionMap.get(item)){
				sumOfRatings += iiu.rating;
			}
			double avgRating = sumOfRatings / trainItemUserInteractionMap.get(item).size();
			itemsAvgRating.put(item, avgRating);
		}
		
		//pre-compute users' average ratings
		for(Long user : trainUserItemInteractionMap.keySet()){
			double sumOfRatings = 0d;
			for(InteractionInfoItem iii : trainUserItemInteractionMap.get(user)){
				sumOfRatings += iii.rating;
			}
			double avgRating = sumOfRatings / trainUserItemInteractionMap.get(user).size();
			usersAvgRating.put(user, avgRating);
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
				
				double avgRatingTestUser = usersAvgRating.get(testUser);
				double avgRatingNeighbor = usersAvgRating.get(neighborUser);
				double similarity = 0d;
				if(similarityType.equals("dotProduct")){
					similarity = UserSimilarity.dotProductSimilarity(testUserFV, avgRatingTestUser, neighborUserFV, avgRatingNeighbor);
				}
				else if(similarityType.equals("cosine")){
					similarity = UserSimilarity.cosineSimilarity(testUserFV, avgRatingTestUser, neighborUserFV, avgRatingNeighbor);
				}
				else if(similarityType.equals("normalizedDotProduct")){
					similarity = UserSimilarity.normalizedDotProductSimilarity(testUserFV, avgRatingTestUser, neighborUserFV, avgRatingNeighbor, itemsAvgRating, globalRatingAvg);
				}
				else if(similarityType.equals("normalizedCosine")){
					similarity = UserSimilarity.normalizedCosineSimilarity(testUserFV, avgRatingTestUser, neighborUserFV, avgRatingNeighbor, itemsAvgRating, globalRatingAvg);
				}
				else if(similarityType.equals("pearson")){
					similarity = UserSimilarity.pearsonSimilarity(testUserFV, avgRatingTestUser, neighborUserFV, avgRatingNeighbor);
				}
				else if(similarityType.equals("custom")){
					similarity = UserSimilarity.customSimilarity(testUserFV, avgRatingTestUser, neighborUserFV, avgRatingNeighbor, itemsAvgRating, globalRatingAvg);
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
					itemRatingPrediction = RatingPrediction.userBasedWeightedAveragePrediction(itemId, topNeighborsList, userItemRatingMap, usersAvgRating);
				}
				else if(predictionType.equals("pearson")){
					itemRatingPrediction = RatingPrediction.userBasedPearsonPrediction(itemId, testUser, topNeighborsList, userItemRatingMap, usersAvgRating);
				}
				else if(predictionType.equals("custom")){
					itemRatingPrediction = RatingPrediction.userBasedCustomPrediction(itemId, topNeighborsList, userItemRatingDateMap, usersAvgRating, 2d, 3d);
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
		endTime   = System.currentTimeMillis();
		System.out.println("Total run time: "+((endTime - startTime)/1000.d) + " seconds");
	}
	
	
	
	
	
	public void printUsersTopKNeighbors(long targetUser, String similarityType) throws FileNotFoundException{
		
		assert(similarityType.equals("dotProduct") || similarityType.equals("cosine") || similarityType.equals("normalizedDotProduct") || similarityType.equals("normalizedCosine") );
		
		//Read training file and map data, indexing by user
		System.out.println("Top "+this.k+" Neighbors for User "+targetUser+" using "+similarityType+" similarity");
		HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap = FileInteraction.readAndMapTrainFileIndexedByUser (trainFile);
		
		//get top users for this user
		LinkedList<InteractionInfoItem> testUserFV = trainUserItemInteractionMap.get(targetUser);
		PriorityQueue<IdSimilarityPair> topNeighbors = new PriorityQueue<IdSimilarityPair>();
		
		//Read training file and map data, indexing by item
		System.out.println("reading training data (indexing by item) ...");
		HashMap<Long, Double> itemsAvgRating = new HashMap<Long, Double>();
		HashMap<Long, LinkedList<InteractionInfoUser>> trainItemUserInteractionMap = FileInteraction.readAndMapTrainFileIndexedByItem (trainFile);

		//global average rating
		double globalRatingAvg = DataStatistics.getGlobalRatingAverage(trainFile);
		
		//Pre-compute rating averages
		HashMap<Long, Double> usersAvgRating = new HashMap<Long, Double>();
		for(Long user : trainUserItemInteractionMap.keySet()){
			double sumOfRatings = 0d;
			for(InteractionInfoItem iii : trainUserItemInteractionMap.get(user)){
				sumOfRatings += iii.rating;
			}
			double avgRating = sumOfRatings / trainUserItemInteractionMap.get(user).size();
			usersAvgRating.put(user, avgRating);
		}
		
		//pre-compute items' average ratings
		for(Long item : trainItemUserInteractionMap.keySet()){
			double sumOfRatings = 0d;
			for(InteractionInfoUser iiu : trainItemUserInteractionMap.get(item)){
				sumOfRatings += iiu.rating;
			}
			double avgRating = sumOfRatings / trainItemUserInteractionMap.get(item).size();
			itemsAvgRating.put(item, avgRating);
		}

		
		for(long neighborUser : trainUserItemInteractionMap.keySet()){
			if(targetUser==neighborUser) continue;
			
			LinkedList<InteractionInfoItem> neighborUserFV = trainUserItemInteractionMap.get(neighborUser);
			
			//compute similarity
			double similarity = 0d;
			double avgRatingTestUser = usersAvgRating.get(targetUser);
			double avgRatingNeighbor = usersAvgRating.get(neighborUser);
			if(similarityType.equals("dotProduct")){
				similarity = UserSimilarity.dotProductSimilarity(testUserFV, avgRatingTestUser, neighborUserFV, avgRatingNeighbor);
			}
			else if(similarityType.equals("cosine")){
				similarity = UserSimilarity.cosineSimilarity(testUserFV, avgRatingTestUser, neighborUserFV, avgRatingNeighbor);
			}
			else if(similarityType.equals("normalizedDotProduct")){
				similarity = UserSimilarity.normalizedDotProductSimilarity(testUserFV, avgRatingTestUser, neighborUserFV, avgRatingNeighbor, itemsAvgRating, globalRatingAvg);
			}
			else if(similarityType.equals("normalizedCosine")){
				similarity = UserSimilarity.normalizedCosineSimilarity(testUserFV, avgRatingTestUser, neighborUserFV, avgRatingNeighbor, itemsAvgRating, globalRatingAvg);
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
