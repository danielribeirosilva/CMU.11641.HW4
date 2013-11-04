import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;


public class DataStatistics {

	public static int numberOfItems (String filePath) throws FileNotFoundException{
		HashSet<Long> itemsSet = new HashSet<Long>();
		
		Scanner scan = new Scanner(new File(filePath));
	    String line = null;
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();
	    
	    do {
	      line = scan.nextLine();
		  String[] pair = line.split(",");
		  if(pair.length != 4){
			  System.err.println("Error: train file has wrong format. It must be a 4-column CSV.");
			  System.exit(1);
		  }
		  
		  long item = Long.parseLong(pair[1].trim());
	      
	      itemsSet.add(item);

	    } while (scan.hasNext());
	    
		return itemsSet.size();
	}
	
	public static int numberOfUsers (String filePath) throws FileNotFoundException{
		HashSet<Long> usersSet = new HashSet<Long>();
		
		Scanner scan = new Scanner(new File(filePath));
	    String line = null;
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();
	    
	    do {
	      line = scan.nextLine();
	      String[] pair = line.split(",");
	      if(pair.length != 4){
	    	  System.err.println("Error: train file has wrong format. It must be a 4-column CSV.");
	    	  System.exit(1);
	      }
	      
	      long user = Long.parseLong(pair[0].trim());
		      
	      usersSet.add(user);

	    } while (scan.hasNext());
	    
		return usersSet.size();
	}
	
	public static int[] ratingDistribution (String filePath) throws FileNotFoundException{
		int[] ratingDistribution = new int[5];
		
		Scanner scan = new Scanner(new File(filePath));
	    String line = null;
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();
	    
	    do {
	      line = scan.nextLine();
		  String[] pair = line.split(",");
		  if(pair.length != 4){
			  System.err.println("Error: train file has wrong format. It must be a 4-column CSV.");
			  System.exit(1);
		  }
		  
		  int rating = Integer.parseInt(pair[2].trim());
	      
		  ratingDistribution[rating-1]++;

	    } while (scan.hasNext());
	    
		return ratingDistribution;
 	}
	
	public static void printUserStatistics (long targetUserId, String filePath) throws FileNotFoundException{
		
		int[] ratingDistribution = new int[5];
		double avgRating = 0d;
		int totalMoviesRated = 0;
		
		
		Scanner scan = new Scanner(new File(filePath));
	    String line = null;
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();
	    
	    do {
	      line = scan.nextLine();
		  String[] pair = line.split(",");
		  if(pair.length != 4){
			  System.err.println("Error: train file has wrong format. It must be a 4-column CSV.");
			  System.exit(1);
		  }
		  
		  long user = Long.parseLong(pair[0].trim());
	      long item = Long.parseLong(pair[1].trim());
	      int rating = Integer.parseInt(pair[2].trim());
	      String ratingDate = pair[3].trim();
	      
	      if(targetUserId == user){
	    	  totalMoviesRated++;
	    	  avgRating += rating;
	    	  ratingDistribution[rating-1]++;
	      }
		  
	    } while (scan.hasNext());
	    
	    avgRating /= totalMoviesRated;
	    
	    System.out.println("total movies rated: " + totalMoviesRated);
		System.out.println("average rating: " + avgRating);
		for(int i=0; i<5; i++){
			System.out.println("# of times user rated any movie "+(i+1)+": "+ratingDistribution[i]);
		}
		
	}
}
