import java.io.IOException;


public class Recommender {

	public static void main(String[] argv) throws IOException {
		
		long startTime, endTime;
		
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
		
		
		
		
		
		
		
		
		//REPORT EXPERIMENTS
		
		/*
		startTime = System.currentTimeMillis();
		UserBasedCF userBasedCF = new UserBasedCF(10, trainFile, testFile, "UserBased_Mean_Dot_10.txt");
		userBasedCF.generateRecommendations("dotProduct", "mean");
		
		endTime   = System.currentTimeMillis();
		System.out.println(((endTime - startTime)/1000.d) + " seconds");
		startTime = System.currentTimeMillis();
		
		userBasedCF = new UserBasedCF(100, trainFile, testFile, "UserBased_Mean_Dot_100.txt");
		userBasedCF.generateRecommendations("dotProduct", "mean");
		
		endTime   = System.currentTimeMillis();
		System.out.println(((endTime - startTime)/1000.d) + " seconds");
		startTime = System.currentTimeMillis();
		
		userBasedCF = new UserBasedCF(500, trainFile, testFile, "UserBased_Mean_Dot_500.txt");
		userBasedCF.generateRecommendations("dotProduct", "mean");
		
		endTime   = System.currentTimeMillis();
		System.out.println(((endTime - startTime)/1000.d) + " seconds");
		startTime = System.currentTimeMillis(); 
		*/
		startTime = System.currentTimeMillis();
		UserBasedCF userBasedCF = new UserBasedCF(10, trainFile, testFile, "UserBased_Mean_Cosine_10.txt");
		userBasedCF.generateRecommendations("cosine", "mean");
		
		endTime   = System.currentTimeMillis();
		System.out.println(((endTime - startTime)/1000.d) + " seconds");
		startTime = System.currentTimeMillis();
		
		userBasedCF = new UserBasedCF(100, trainFile, testFile, "UserBased_Mean_Cosine_100.txt");
		userBasedCF.generateRecommendations("cosine", "mean");
		
		endTime   = System.currentTimeMillis();
		System.out.println(((endTime - startTime)/1000.d) + " seconds");
		startTime = System.currentTimeMillis();
		
		userBasedCF = new UserBasedCF(500, trainFile, testFile, "UserBased_Mean_Cosine_500.txt");
		userBasedCF.generateRecommendations("cosine", "mean");
		
		endTime   = System.currentTimeMillis();
		System.out.println(((endTime - startTime)/1000.d) + " seconds");
		startTime = System.currentTimeMillis(); 
		
		
	}

}