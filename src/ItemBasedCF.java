import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class ItemBasedCF {
	public int k;
	public String trainFile; 
	public String testFile;
	public String outputFile;

	public ItemBasedCF(int k, String trainFile, String testFile, String outputFile){
		this.k = k;
		this.trainFile = trainFile;
		this.testFile = testFile;
		this.outputFile = outputFile;
	}
	
	public void generateRecommendations() throws IOException{
		
		//Read training file and map data, indexing by user
		System.out.println("reading training data ...");
		HashMap<Long, LinkedList<InteractionInfoUser>> trainItemUserInteractionMap = new HashMap<Long, LinkedList<InteractionInfoUser>>();
		try {
			trainItemUserInteractionMap = FileInteraction.readAndMapTrainFileIndexedByItem (trainFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		//Read test file and map data, indexing by user
		System.out.println("reading testing data ...");
		HashMap<Long, LinkedList<Long>> testItemUserMap = new HashMap<Long, LinkedList<Long>>();
		try {
			testItemUserMap = FileInteraction.readAndMapTestFile(testFile, false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// For each user in test set, compute user-user similarity with previous users
		// and get top K users for each of these test users
		System.out.println("doing item-item similiarity recommendation ...");
		
		int count = 0;
		
		//compute prediction for each user and item in the test file
		HashMap<Long, HashMap<Long, Double>> ratingPredictions = new HashMap<Long, HashMap<Long, Double>>(); 
		for(long testItem: testItemUserMap.keySet()){
			
			//get top items for this item
			LinkedList<InteractionInfoUser> testItemFV = trainItemUserInteractionMap.get(testItem);
			PriorityQueue<IdSimilarityPair> topNeighbors = new PriorityQueue<IdSimilarityPair>();
			for(long neighborItem : trainItemUserInteractionMap.keySet()){
				if(testItem == neighborItem) continue;
				
				LinkedList<InteractionInfoUser> neighborItemFV = trainItemUserInteractionMap.get(neighborItem);
				
				//compute similarity
				double similarity = ItemSimilarity.dotProductSimilarity(testItemFV, neighborItemFV);
				//double similarity = UserSimilarity.strictDotProductSimilarity(testUserFV, neighborUserFV);
				//double similarity = UserSimilarity.cosineSimilarity(testUserFV, neighborUserFV);
				
				IdSimilarityPair pair = new IdSimilarityPair(neighborItem, similarity);
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
			
			//free unused memory
			trainItemUserInteractionMap.clear();
			
			//reload train file, but now indexed by user
			HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap = FileInteraction.readAndMapTrainFileIndexedByUser(trainFile);
			
			//Compute prediction rating for required items
			HashMap<Long, Double> currentItemPredictions = new HashMap<Long, Double>();
			for(long userId : testItemUserMap.get(testItem) ){
				double itemRatingPrediction = RatingPrediction.itemBasedSimpleAveragePrediction(userId, topNeighborsList, trainUserItemInteractionMap);
				//double itemRatingPrediction = RatingPrediction.itemBasedWeightedAveragePrediction(userId, topNeighborsList, trainUserItemInteractionMap);
				currentItemPredictions.put(userId, itemRatingPrediction);

				//System.out.println("user: "+userId+" item: "+testItem+" predicition: "+itemRatingPrediction);
			}
			
			ratingPredictions.put(testItem, currentItemPredictions);
			
			count++;
			if(count%10==0) System.out.println(count+"/"+testItemUserMap.size());	
		}
		
		//write predictions to output file in same order
		System.out.println("writing predictions to file... ");
		FileInteraction.writeInvertedPredictionsToFile(testFile, outputFile, ratingPredictions);
	}
}
