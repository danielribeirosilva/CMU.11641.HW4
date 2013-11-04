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
		
		//DATA STATISTICS
		int totalMovies = DataStatistics.numberOfItems(trainFile);
		int totalUsers = DataStatistics.numberOfUsers(trainFile);
		int[] ratingDistribution = DataStatistics.ratingDistribution(trainFile);
		System.out.println("total movies: "+totalMovies);
		System.out.println("total users: "+totalUsers);
		for(int i=0; i<5; i++){
			System.out.println("# of times any movie was rated "+(i+1)+": "+ratingDistribution[i]);
		}
		DataStatistics.printUserStatistics(1234576, trainFile);
		
		//EXPERIMENT 1 - USER-USER SIMILARITY
		//UserBasedCF userBasedCF = new UserBasedCF(50, trainFile, testFile, outputFile);
		//userBasedCF.generateRecommendations();
		
		
		//EXPERIMENT 2 - ITEM-ITEM SIMILARITY
		//ItemBasedCF itemBasedCF = new ItemBasedCF(50, trainFile, testFile, outputFile);
		//itemBasedCF.generateRecommendations();
		
		
	}

}