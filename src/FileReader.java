import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;


public class FileReader {
	
	//Two files available:
	// 1) test-all.csv : ("MovieID", "UserID") as (long, long)
	// 2) training_set.csv : ("MovieID","UserID","Rating", "RatingDate") as (long, long, int, String)
	
	public static HashMap<Long, HashSet<Long>> readAndMapTestFile (String filePath, boolean indexByUser) throws FileNotFoundException{
		HashMap<Long, HashSet<Long>> userItemPair = new HashMap<Long, HashSet<Long>>();
		
		Scanner scan = new Scanner(new File(filePath));
	    String line = null;
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();
	    
	    do {
	      line = scan.nextLine();
	      String[] pair = line.split(",");
	      if(pair.length != 2){
	    	  System.err.println("Error: test file has wrong format. It must be a 2-column CSV.");
	    	  System.exit(1);
	      }
	      long user = Long.parseLong(pair[0].trim());
	      long item = Long.parseLong(pair[1].trim());
	      
	      if(indexByUser){
	    	  if(userItemPair.containsKey(user)){
	    		  userItemPair.get(user).add(item);
	    	  }
	    	  else{
	    		  HashSet<Long> hs = new HashSet<Long>();
	    		  hs.add(item);
	    		  userItemPair.put(user, hs);
	    	  }
	      }
	      else{
	    	  if(userItemPair.containsKey(item)){
	    		  userItemPair.get(item).add(user);
	    	  }
	    	  else{
	    		  HashSet<Long> hs = new HashSet<Long>();
	    		  hs.add(user);
	    		  userItemPair.put(item, hs);
	    	  }
	      }

	    } while (scan.hasNext());
	    
		return userItemPair;
	}
	
	
	
	public static HashMap<Long, HashMap<Long, InteractionInfo>> readAndMapTrainFile (String filePath, boolean indexByUser) throws FileNotFoundException{
		HashMap<Long, HashMap<Long, InteractionInfo>> userItemPair = new HashMap<Long, HashMap<Long, InteractionInfo>>();
		
		Scanner scan = new Scanner(new File(filePath));
	    String line = null;
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();
	    
	    int count = 0;
	    
	    do{
	      count++;
	      if(count%1000==0)
	    	  System.out.println(count);
	    	
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
	      
	      if(indexByUser){
	    	  if(userItemPair.containsKey(user)){
	    		  userItemPair.get(user).put(item, new InteractionInfo(rating, ratingDate));
	    	  }
	    	  else{
	    		  HashMap<Long, InteractionInfo> hm = new HashMap<Long, InteractionInfo>();
	    		  hm.put(item, new InteractionInfo(rating, ratingDate));
	    		  userItemPair.put(user, hm);
	    	  }
	      }
	      else{
	    	  if(userItemPair.containsKey(item)){
	    		  userItemPair.get(item).put(user, new InteractionInfo(rating, ratingDate));
	    	  }
	    	  else{
	    		  HashMap<Long, InteractionInfo> hm = new HashMap<Long, InteractionInfo>();
	    		  hm.put(user, new InteractionInfo(rating, ratingDate));
	    		  userItemPair.put(item, hm);
	    	  }
	      }

	    }while(scan.hasNext());
	    
		return userItemPair;
	}
	
	
}
