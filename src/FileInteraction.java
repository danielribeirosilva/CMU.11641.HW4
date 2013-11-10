import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;


public class FileInteraction {
	
	//Two files available:
	// 1) test-all.csv : ("MovieID", "UserID") as (long, long)
	// 2) training_set.csv : ("MovieID","UserID","Rating", "RatingDate") as (long, long, int, String)
	
	public static HashMap<Long, LinkedList<Long>> readAndMapTestFile (String filePath, boolean indexByUser) throws FileNotFoundException{
		HashMap<Long, LinkedList<Long>> userItemPair = new HashMap<Long, LinkedList<Long>>();
		
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
	      long item = Long.parseLong(pair[0].trim());
	      long user = Long.parseLong(pair[1].trim());
	      
	      if(indexByUser){
	    	  if(userItemPair.containsKey(user) && !userItemPair.get(user).contains(item)){
	    		  userItemPair.get(user).add(item);
	    	  }
	    	  else{
	    		  LinkedList<Long> ll = new LinkedList<Long>();
	    		  ll.add(item);
	    		  userItemPair.put(user, ll);
	    	  }
	      }
	      else{
	    	  if(userItemPair.containsKey(item) && !userItemPair.get(item).contains(user)){
	    		  userItemPair.get(item).add(user);
	    	  }
	    	  else{
	    		  LinkedList<Long> ll = new LinkedList<Long>();
	    		  ll.add(user);
	    		  userItemPair.put(item, ll);
	    	  }
	      }

	    } while (scan.hasNext());
	    
	    //sort each list
	    for(long element : userItemPair.keySet()){
	    	Collections.sort(userItemPair.get(element));
	    }
	    
		return userItemPair;
	}
	
	
	
	public static HashMap<Long, LinkedList<InteractionInfoItem>> readAndMapTrainFileIndexedByUser (String filePath) throws FileNotFoundException{
		HashMap<Long, LinkedList<InteractionInfoItem>> trainingMap = new HashMap<Long, LinkedList<InteractionInfoItem>>();
		
		Scanner scan = new Scanner(new File(filePath));
	    String line = null;
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();

	    do{
	    	
	      line = scan.nextLine();
	      String[] pair = line.split(",");
	      if(pair.length != 4){
	    	  System.err.println("Error: train file has wrong format. It must be a 4-column CSV.");
	    	  System.exit(1);
	      }
	      
	      long item = Long.parseLong(pair[0].trim());
	      long user = Long.parseLong(pair[1].trim());
	      int rating = Integer.parseInt(pair[2].trim());
	      String ratingDate = pair[3].trim();
	      
	      if(trainingMap.containsKey(user)){
    		  trainingMap.get(user).add(new InteractionInfoItem(item, rating, ratingDate));
    	  }
    	  else{
    		  LinkedList<InteractionInfoItem> ll = new LinkedList<InteractionInfoItem>();
    		  ll.add(new InteractionInfoItem(item, rating, ratingDate));
    		  trainingMap.put(user, ll);
    	  }

	    }while(scan.hasNext());
	    
	    //sort each list
	    for(long user : trainingMap.keySet()){
	    	Collections.sort(trainingMap.get(user));
	    }
	    
		return trainingMap;
	}
	
	public static HashMap<Long, HashMap<Long, Integer>> generateUserItemRatingMapFromTrainFile (String filePath) throws FileNotFoundException{
		HashMap<Long, HashMap<Long, Integer>> trainingMap = new HashMap<Long, HashMap<Long, Integer>>();
		
		Scanner scan = new Scanner(new File(filePath));
	    String line = null;
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();

	    do{
	    	
	      line = scan.nextLine();
	      String[] pair = line.split(",");
	      if(pair.length != 4){
	    	  System.err.println("Error: train file has wrong format. It must be a 4-column CSV.");
	    	  System.exit(1);
	      }
	      
	      long item = Long.parseLong(pair[0].trim());
	      long user = Long.parseLong(pair[1].trim());
	      int rating = Integer.parseInt(pair[2].trim());
	      
	      if(trainingMap.containsKey(user)){
    		  trainingMap.get(user).put(item, rating);
    	  }
    	  else{
    		  HashMap<Long, Integer> hm = new HashMap<Long, Integer>();
    		  hm.put(item, rating);
    		  trainingMap.put(user, hm);
    	  }

	    }while(scan.hasNext());
	    
		return trainingMap;
	}
	
	public static HashMap<Long, HashMap<Long, RatingDatePair>> generateUserItemRatingDatePairMapFromTrainFile (String filePath) throws FileNotFoundException{
		HashMap<Long, HashMap<Long, RatingDatePair>> trainingMap = new HashMap<Long, HashMap<Long, RatingDatePair>>();
		
		Scanner scan = new Scanner(new File(filePath));
	    String line = null;
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();

	    do{
	    	
	      line = scan.nextLine();
	      String[] pair = line.split(",");
	      if(pair.length != 4){
	    	  System.err.println("Error: train file has wrong format. It must be a 4-column CSV.");
	    	  System.exit(1);
	      }
	      
	      long item = Long.parseLong(pair[0].trim());
	      long user = Long.parseLong(pair[1].trim());
	      int rating = Integer.parseInt(pair[2].trim());
	      String ratingDate = pair[3].trim();
	      
	      if(trainingMap.containsKey(user)){
    		  trainingMap.get(user).put(item, new RatingDatePair(rating, ratingDate));
    	  }
    	  else{
    		  HashMap<Long, RatingDatePair> hm = new HashMap<Long, RatingDatePair>();
    		  hm.put(item, new RatingDatePair(rating, ratingDate));
    		  trainingMap.put(user, hm);
    	  }

	    }while(scan.hasNext());
	    
		return trainingMap;
	}
	
	public static HashMap<Long, LinkedList<InteractionInfoUser>> readAndMapTrainFileIndexedByItem (String filePath) throws FileNotFoundException{
		HashMap<Long, LinkedList<InteractionInfoUser>> trainingMap = new HashMap<Long, LinkedList<InteractionInfoUser>>();
		
		Scanner scan = new Scanner(new File(filePath));
	    String line = null;
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();

	    do{
	    	
	      line = scan.nextLine();
	      String[] pair = line.split(",");
	      if(pair.length != 4){
	    	  System.err.println("Error: train file has wrong format. It must be a 4-column CSV.");
	    	  System.exit(1);
	      }
	      
	      long item = Long.parseLong(pair[0].trim());
	      long user = Long.parseLong(pair[1].trim());
	      int rating = Integer.parseInt(pair[2].trim());
	      String ratingDate = pair[3].trim();
	      
	      if(trainingMap.containsKey(item)){
    		  trainingMap.get(item).add(new InteractionInfoUser(user, rating, ratingDate));
    	  }
    	  else{
    		  LinkedList<InteractionInfoUser> ll = new LinkedList<InteractionInfoUser>();
    		  ll.add(new InteractionInfoUser(user, rating, ratingDate));
    		  trainingMap.put(item, ll);
    	  }

	    }while(scan.hasNext());
	    
	    //sort each list
	    for(long item : trainingMap.keySet()){
	    	Collections.sort(trainingMap.get(item));
	    }
	    
		return trainingMap;
	}
	
	public static void writePredictionsToFile(String testFile, String outputFileName, HashMap<Long, HashMap<Long, Double>> ratingPredictions) throws IOException{
		
		FileWriter fileWriter = new FileWriter(outputFileName);
	    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	    
	    Scanner scan = new Scanner(new File(testFile));
	    String line = null;
	    
	    boolean firstLine = true;
	    long currentUser = -1;
	    HashMap<Long, Double> currentUserPredictions = new HashMap<Long, Double>();
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();
	    
	    do {
	    	
	      line = scan.nextLine();
	      
	      String[] pair = line.split(",");
	      if(pair.length != 2){
	    	  System.err.println("Error: test file has wrong format. It must be a 2-column CSV.");
	    	  System.exit(1);
	      }
	      
	      long item = Long.parseLong(pair[0].trim());
	      long user = Long.parseLong(pair[1].trim());
	      
	      if(user != currentUser){
	    	  if(!ratingPredictions.containsKey(user)){
		    	  System.err.println("Error: test file contains user with uncomputed prediction.");
		    	  System.exit(1);
		      }
	    	  currentUserPredictions = ratingPredictions.get(user);
	      }
	      
	      if(!currentUserPredictions.containsKey(item)){
	    	  System.err.println("Error: test file contains item with uncomputed prediction.");
	    	  System.exit(1);
	      }
	    	  
	      double prediction = currentUserPredictions.get(item);
	      
	      if(!firstLine) bufferedWriter.newLine();
	      if(firstLine){firstLine=false;}
	      bufferedWriter.write(String.valueOf(prediction));

	    } while (scan.hasNext());
	    
	    bufferedWriter.close();
		
	}
	
	public static void writeInvertedPredictionsToFile(String testFile, String outputFileName, HashMap<Long, HashMap<Long, Double>> ratingPredictions) throws IOException{
		
		FileWriter fileWriter = new FileWriter(outputFileName);
	    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	    
	    Scanner scan = new Scanner(new File(testFile));
	    String line = null;
	    
	    boolean firstLine = true;
	    
	    //read and ignore first line (headers)
	    line = scan.nextLine();
	    
	    do {
	    	
	      line = scan.nextLine();
	      
	      String[] pair = line.split(",");
	      if(pair.length != 2){
	    	  System.err.println("Error: test file has wrong format. It must be a 2-column CSV.");
	    	  System.exit(1);
	      }
	      
	      long item = Long.parseLong(pair[0].trim());
	      long user = Long.parseLong(pair[1].trim());
	      
	      if(!ratingPredictions.containsKey(item)){
	    	  System.err.println("Error: test file contains item with uncomputed prediction.");
	    	  System.exit(1);
	      }
	      
	      if(!ratingPredictions.get(item).containsKey(user)){
	    	  System.err.println("Error: test file contains user with uncomputed prediction.");
	    	  System.exit(1);
	      }
	    	  
	      double prediction = ratingPredictions.get(item).get(user);
	      
	      if(!firstLine) bufferedWriter.newLine();
	      if(firstLine){firstLine=false;}
	      bufferedWriter.write(String.valueOf(prediction));

	    } while (scan.hasNext());
	    
	    bufferedWriter.close();
		
	}
	
}
