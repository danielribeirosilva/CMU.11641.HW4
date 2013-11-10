import java.io.IOException;
import java.text.ParseException;


public class Recommender {

	public static void main(String[] argv) throws IOException, ParseException {
		
		if(argv.length < 3){
			System.err.println("Error: please specify test, train and output files on argv.");
			System.exit(1);
		}
		
		String trainFile = argv[0];
		String testFile = argv[1];
		String outputFile = argv[2];
		
		//DATA STATISTICS
		
		/*
		int totalMovies = DataStatistics.numberOfItems(trainFile);
		int totalUsers = DataStatistics.numberOfUsers(trainFile);
		int[] ratingDistribution = DataStatistics.ratingDistribution(trainFile);
		System.out.println("total movies: "+totalMovies);
		System.out.println("total users: "+totalUsers);
		for(int i=0; i<5; i++){
			System.out.println("# of times any movie was rated "+(i+1)+": "+ratingDistribution[i]);
		}
		System.out.println("rating average: "+DataStatistics.ratingAverage(ratingDistribution));
		DataStatistics.printUserStatistics(1234576, trainFile);
		DataStatistics.printItemStatistics(4321, trainFile);
		*/
		
		//2.0 -> NEAREST NEIGHBORS FOR SPECIFIC USERS & ITEMS
		
		/*
		UserBasedCF userBasedCF_2_0 = new UserBasedCF(5, trainFile, testFile, outputFile);
		userBasedCF_2_0.printUsersTopKNeighbors(1234576, "dotProduct");
		userBasedCF_2_0.printUsersTopKNeighbors(1234576, "cosine");
		ItemBasedCF itemBasedCF_2_0 = new ItemBasedCF(5, trainFile, testFile, outputFile);
		itemBasedCF_2_0.printItemsTopKNeighbors(4321, "dotProduct");
		itemBasedCF_2_0.printItemsTopKNeighbors(4321, "cosine");
		*/
		
		//EXPERIMENT 1 - USER-USER SIMILARITY
		//UserBasedCF userBasedCF = new UserBasedCF(50, trainFile, testFile, outputFile);
		//userBasedCF.generateRecommendations("dotProduct", "mean");
		
		
		//EXPERIMENT 2 - ITEM-ITEM SIMILARITY
		//ItemBasedCF itemBasedCF = new ItemBasedCF(50, trainFile, testFile, outputFile);
		//itemBasedCF.generateRecommendations("dotProduct", "mean");
		
		//EXPERIMENT 3 - PEARSON CORRELATION COEFFICIENT SIMILARITY
		//UserBasedCF userBasedCF = new UserBasedCF(50, trainFile, testFile, outputFile);
		//userBasedCF.generateRecommendations("pearson", "pearson");
		
		//EXPERIMENT 4.1 - DOUBLE-NORMALIZED USER-USER SIMILARITY
		//UserBasedCF normalizedUserBasedCF = new UserBasedCF(50, trainFile, testFile, "Norm_UserBased_Mean_Dot_10.txt");
		//normalizedUserBasedCF.generateRecommendations("normalizedDotProduct", "mean");
		
		//EXPERIMENT 4.2 - DOUBLE-NORMALIZED TIME-BOOSTED USER-USER SIMILARITY
		//UserBasedCF customUserBasedCF = new UserBasedCF(50, trainFile, testFile, "Custom_UserBased_50.txt");
		//customUserBasedCF.generateRecommendations("custom", "custom");
		
		//CHOSEN CUSTOM ALGORITHM
		UserBasedCF normalizedUserBasedCF = new UserBasedCF(100, trainFile, testFile, outputFile);
		normalizedUserBasedCF.generateRecommendations("normalizedDotProduct", "mean");
		
		
		
		//REPORT EXPERIMENTS
		//USER-BASED
		//UserBasedCF userBasedCF;
		/*
		userBasedCF = new UserBasedCF(10, trainFile, testFile, "UserBased_Mean_Dot_10.txt");
		userBasedCF.generateRecommendations("dotProduct", "mean");
		
		userBasedCF = new UserBasedCF(100, trainFile, testFile, "UserBased_Mean_Dot_100.txt");
		userBasedCF.generateRecommendations("dotProduct", "mean");
		
		userBasedCF = new UserBasedCF(500, trainFile, testFile, "UserBased_Mean_Dot_500.txt");
		userBasedCF.generateRecommendations("dotProduct", "mean");
		
		userBasedCF = new UserBasedCF(10, trainFile, testFile, "UserBased_Mean_Cosine_10.txt");
		userBasedCF.generateRecommendations("cosine", "mean");
		
		userBasedCF = new UserBasedCF(100, trainFile, testFile, "UserBased_Mean_Cosine_100.txt");
		userBasedCF.generateRecommendations("cosine", "mean");
		
		userBasedCF = new UserBasedCF(500, trainFile, testFile, "UserBased_Mean_Cosine_500.txt");
		userBasedCF.generateRecommendations("cosine", "mean");
		
		userBasedCF = new UserBasedCF(10, trainFile, testFile, "UserBased_Weighted_Cosine_10.txt");
		userBasedCF.generateRecommendations("cosine", "weighted");
		
		userBasedCF = new UserBasedCF(100, trainFile, testFile, "UserBased_Weighted_Cosine_100.txt");
		userBasedCF.generateRecommendations("cosine", "weighted");
		
		userBasedCF = new UserBasedCF(500, trainFile, testFile, "UserBased_Weighted_Cosine_500.txt");
		userBasedCF.generateRecommendations("cosine", "weighted");
		*/
		
		//ITEM BASED
		//ItemBasedCF itemBasedCF;
		/*
		itemBasedCF = new ItemBasedCF(10, trainFile, testFile, "ItemBased_Mean_Dot_10.txt");
		itemBasedCF.generateRecommendations("dotProduct", "mean");
		
		itemBasedCF = new ItemBasedCF(100, trainFile, testFile, "ItemBased_Mean_Dot_100.txt");
		itemBasedCF.generateRecommendations("dotProduct", "mean");
		
		itemBasedCF = new ItemBasedCF(500, trainFile, testFile, "ItemBased_Mean_Dot_500.txt");
		itemBasedCF.generateRecommendations("dotProduct", "mean");
		
		itemBasedCF = new ItemBasedCF(10, trainFile, testFile, "ItemBased_Mean_Cosine_10.txt");
		itemBasedCF.generateRecommendations("cosine", "mean");
		
		itemBasedCF = new ItemBasedCF(100, trainFile, testFile, "ItemBased_Mean_Cosine_100.txt");
		itemBasedCF.generateRecommendations("cosine", "mean");
		
		itemBasedCF = new ItemBasedCF(500, trainFile, testFile, "ItemBased_Mean_Cosine_500.txt");
		itemBasedCF.generateRecommendations("cosine", "mean");
		
		itemBasedCF = new ItemBasedCF(10, trainFile, testFile, "ItemBased_Weighted_Cosine_10.txt");
		itemBasedCF.generateRecommendations("cosine", "weighted");
		
		itemBasedCF = new ItemBasedCF(100, trainFile, testFile, "ItemBased_Weighted_Cosine_100.txt");
		itemBasedCF.generateRecommendations("cosine", "weighted");
	
		itemBasedCF = new ItemBasedCF(500, trainFile, testFile, "ItemBased_Weighted_Cosine_500.txt");
		itemBasedCF.generateRecommendations("cosine", "weighted");
		*/
		
		//PEARSON CORRELATION COEFFICIENT
		//UserBasedCF userBasedCF;
		
		/*
		userBasedCF = new UserBasedCF(10, trainFile, testFile, "UserBasedPCC_Pearson_10.txt");
		userBasedCF.generateRecommendations("pearson", "pearson");
		
		userBasedCF = new UserBasedCF(50, trainFile, testFile, "UserBasedPCC_Pearson_50.txt");
		userBasedCF.generateRecommendations("pearson", "pearson");
		
		userBasedCF = new UserBasedCF(100, trainFile, testFile, "UserBasedPCC_Pearson_100.txt");
		userBasedCF.generateRecommendations("pearson", "pearson");
		
		userBasedCF = new UserBasedCF(500, trainFile, testFile, "UserBasedPCC_Pearson_500.txt");
		userBasedCF.generateRecommendations("pearson", "pearson");
		
		userBasedCF = new UserBasedCF(10, trainFile, testFile, "UserBasedPCC_Weighted_10.txt");
		userBasedCF.generateRecommendations("pearson", "weighted");
		
		userBasedCF = new UserBasedCF(50, trainFile, testFile, "UserBasedPCC_Weighted_50.txt");
		userBasedCF.generateRecommendations("pearson", "weighted");
		
		userBasedCF = new UserBasedCF(100, trainFile, testFile, "UserBasedPCC_Weighted_100.txt");
		userBasedCF.generateRecommendations("pearson", "weighted");
		
		userBasedCF = new UserBasedCF(500, trainFile, testFile, "UserBasedPCC_Weighted_500.txt");
		userBasedCF.generateRecommendations("pearson", "weighted");
		
		userBasedCF = new UserBasedCF(10, trainFile, testFile, "UserBasedPCC_Mean_10.txt");
		userBasedCF.generateRecommendations("pearson", "mean");
		
		userBasedCF = new UserBasedCF(50, trainFile, testFile, "UserBasedPCC_Mean_50.txt");
		userBasedCF.generateRecommendations("pearson", "mean");
		
		userBasedCF = new UserBasedCF(100, trainFile, testFile, "UserBasedPCC_Mean_100.txt");
		userBasedCF.generateRecommendations("pearson", "mean");
		
		userBasedCF = new UserBasedCF(500, trainFile, testFile, "UserBasedPCC_Mean_500.txt");
		userBasedCF.generateRecommendations("pearson", "mean");
		*/
		
		//DOUBLE-NORMALIZED USER-BASED
		//UserBasedCF normalizedUserBasedCF;
		
		/*
		normalizedUserBasedCF = new UserBasedCF(10, trainFile, testFile, "Norm_UserBased_Mean_Dot_10.txt");
		normalizedUserBasedCF.generateRecommendations("normalizedDotProduct", "mean");
		
		normalizedUserBasedCF = new UserBasedCF(100, trainFile, testFile, "Norm_UserBased_Mean_Dot_100.txt");
		normalizedUserBasedCF.generateRecommendations("normalizedDotProduct", "mean");
		
		normalizedUserBasedCF = new UserBasedCF(500, trainFile, testFile, "Norm_UserBased_Mean_Dot_500.txt");
		normalizedUserBasedCF.generateRecommendations("normalizedDotProduct", "mean");
		
		normalizedUserBasedCF = new UserBasedCF(10, trainFile, testFile, "Norm_UserBased_Mean_Cosine_10.txt");
		normalizedUserBasedCF.generateRecommendations("normalizedCosine", "mean");
		
		normalizedUserBasedCF = new UserBasedCF(100, trainFile, testFile, "Norm_UserBased_Mean_Cosine_100.txt");
		normalizedUserBasedCF.generateRecommendations("normalizedCosine", "mean");
		
		normalizedUserBasedCF = new UserBasedCF(500, trainFile, testFile, "Norm_UserBased_Mean_Cosine_500.txt");
		normalizedUserBasedCF.generateRecommendations("normalizedCosine", "mean");
		
		normalizedUserBasedCF = new UserBasedCF(10, trainFile, testFile, "Norm_UserBased_Weighted_Cosine_10.txt");
		normalizedUserBasedCF.generateRecommendations("normalizedCosine", "weighted");
		
		normalizedUserBasedCF = new UserBasedCF(100, trainFile, testFile, "Norm_UserBased_Weighted_Cosine_100.txt");
		normalizedUserBasedCF.generateRecommendations("normalizedCosine", "weighted");
		
		normalizedUserBasedCF = new UserBasedCF(500, trainFile, testFile, "Norm_UserBased_Weighted_Cosine_500.txt");
		normalizedUserBasedCF.generateRecommendations("normalizedCosine", "weighted"); 
		*/

	}

	
	
	
}