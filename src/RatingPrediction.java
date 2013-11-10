import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


public class RatingPrediction {
	
	/*------------ USER-BASED --------*/
	
	public static double userBasedSimpleAveragePrediction (long targetItemId, LinkedList<IdSimilarityPair> topNeighborsList, HashMap<Long, HashMap<Long, Integer>> userItemRatingMap, HashMap<Long, Double> usersAvgRating){
		
		double prediction = 0.d;
		
		for(IdSimilarityPair idSim : topNeighborsList){
			long currentNeighbor = idSim.id;
			HashMap<Long, Integer> neighborFV = userItemRatingMap.get(currentNeighbor);
			
			if(neighborFV.containsKey(targetItemId)){
				prediction += neighborFV.get(targetItemId);
			}
			else{
				double neighborAvgRating = usersAvgRating.get(currentNeighbor);
				prediction += neighborAvgRating;
			}
		}
		
		return prediction / topNeighborsList.size();
	}
	
	
	public static double userBasedWeightedAveragePrediction (long targetItemId, LinkedList<IdSimilarityPair> topNeighborsList, HashMap<Long, HashMap<Long, Integer>> userItemRatingMap, HashMap<Long, Double> usersAvgRating){
		
		double prediction = 0.d;
		double sumOfSimilarities = 0d;
		
		for(IdSimilarityPair idSim : topNeighborsList){
			long currentNeighbor = idSim.id;
			double currentSimilarity = idSim.similarity;
			sumOfSimilarities += currentSimilarity;
			HashMap<Long, Integer> neighborFV = userItemRatingMap.get(currentNeighbor);
			
			if(neighborFV.containsKey(targetItemId)){
				prediction += currentSimilarity*neighborFV.get(targetItemId);
			}
			else{
				prediction += currentSimilarity*usersAvgRating.get(currentNeighbor);
			}
		}
		
		if(sumOfSimilarities==0){
			double neighborsAvg = 0d;
			for(Long user : usersAvgRating.keySet()){
				neighborsAvg += usersAvgRating.get(user);
			}
			return neighborsAvg/usersAvgRating.keySet().size();
		}
		
		return prediction / sumOfSimilarities;
	}
	
	public static double userBasedPearsonPrediction (long targetItemId, long targetUser, LinkedList<IdSimilarityPair> topNeighborsList, HashMap<Long, HashMap<Long, Integer>> userItemRatingMap, HashMap<Long, Double> usersAvgRating){
		
		double neighborsContribution = 0.d;
		double sumOfSimilarities = 0d;
		
		for(IdSimilarityPair idSim : topNeighborsList){
			long currentNeighbor = idSim.id;
			double currentSimilarity = idSim.similarity;
			
			HashMap<Long, Integer> neighborFV = userItemRatingMap.get(currentNeighbor);
			if(neighborFV.containsKey(targetItemId)){
				double neighborRating = neighborFV.get(targetItemId);
				double neighborAvgRating = usersAvgRating.get(currentNeighbor);
				neighborsContribution += currentSimilarity*(neighborRating - neighborAvgRating);
				sumOfSimilarities += currentSimilarity;
			}
		}
		
		double targetUserAvg = usersAvgRating.get(targetUser);
		
		if(sumOfSimilarities==0){ 
			return targetUserAvg;
		}
		
		return targetUserAvg + ( neighborsContribution / sumOfSimilarities );
	}
	
	//custom prediction is an extension of the Weighted Average.
	//Extensions include: case amplification (Breese et al.), presence boost (boosting if neighbor has rated item), time-variant relevance
	public static double userBasedCustomPrediction (long targetItemId, LinkedList<IdSimilarityPair> topNeighborsList, HashMap<Long, HashMap<Long, RatingDatePair>> userItemRatingMap, HashMap<Long, Double> usersAvgRating, double amplification, double presenceBoost) throws ParseException{
		
		double prediction = 0.d;
		double sumOfSimilarities = 0d;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		for(IdSimilarityPair idSim : topNeighborsList){
			long currentNeighbor = idSim.id;
			double currentSimilarity = Math.pow(idSim.similarity, amplification);
			
			HashMap<Long, RatingDatePair> neighborFV = userItemRatingMap.get(currentNeighbor);
			
			if(neighborFV.containsKey(targetItemId)){
				//get oldest and most recent rating dates
				Date oldest = sdf.parse("2050-12-31");
				Date newest = sdf.parse("1970-01-01");
				for(Long itemKey : neighborFV.keySet()){
					Date currentDate = sdf.parse(neighborFV.get(itemKey).getRatingDate());
					if(currentDate.getTime() < oldest.getTime()){
						oldest = currentDate;
					}
					if(currentDate.getTime() > newest.getTime()){
						newest = currentDate;
					}
				}
				long ratingPeriod = (newest.getTime() - oldest.getTime())/(1000*60*60*24);
				Date ratingDate = sdf.parse(neighborFV.get(targetItemId).getRatingDate());
				double oldRate = (ratingPeriod<5) ? 0 : ( ( (newest.getTime() - ratingDate.getTime() ) / (1000d*60d*60d*24d) ) / (double)ratingPeriod );
				double freshnessBoosting = Math.exp((1d-oldRate)/3d);
				
				prediction += presenceBoost*freshnessBoosting*currentSimilarity*neighborFV.get(targetItemId).getRating();
				sumOfSimilarities += presenceBoost*currentSimilarity*freshnessBoosting;
			}
			else{
				prediction += currentSimilarity*usersAvgRating.get(currentNeighbor);
				sumOfSimilarities += currentSimilarity;
			}
		}
		
		if(sumOfSimilarities==0){
			double neighborsAvg = 0d;
			for(Long user : usersAvgRating.keySet()){
				neighborsAvg += usersAvgRating.get(user);
			}
			return neighborsAvg/usersAvgRating.keySet().size();
		}
		
		return prediction / sumOfSimilarities;
	}
	
	/*------------ ITEM-BASED --------*/
	
	public static double itemBasedSimpleAveragePrediction (long targetUserId, LinkedList<IdSimilarityPair> topNeighborsList, HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap){
		
		//user rating list
		LinkedList<InteractionInfoItem> targetUserRatings = trainUserItemInteractionMap.get(targetUserId);
		
		//put neighbors id and IdSimilarityPair in hash set so that "contains" will be in O(1) 
		HashSet<Long> topNeighborsIdHashSet = new HashSet<Long>();
		for(IdSimilarityPair pair : topNeighborsList){
			topNeighborsIdHashSet.add(pair.id);
		}
		
		double targetUserAvgRating = 0d;
		double prediction = 0d;
		int totalUnratedNeighbors = 0;
		
		for(InteractionInfoItem iii : targetUserRatings){
			
			if(topNeighborsIdHashSet.contains(iii.element)){
				prediction += iii.rating;
			}
			else{
				totalUnratedNeighbors++;
			}
			
			targetUserAvgRating += iii.rating;
		}
		targetUserAvgRating /= targetUserRatings.size();
		prediction =+ totalUnratedNeighbors*targetUserAvgRating;
		
		return prediction / targetUserRatings.size();
	}
	
	public static double itemBasedWeightedAveragePrediction (long targetUserId, LinkedList<IdSimilarityPair> topNeighborsList, HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap){
		
		//user rating list
		LinkedList<InteractionInfoItem> targetUserRatings = trainUserItemInteractionMap.get(targetUserId);
		
		//put ratings in hash set so that "contains" will be in O(1) 
		HashMap<Long,Integer> targetUserRatingsMap = new HashMap<Long,Integer>();
		double targetUserAvgRating = 0d;
		for(InteractionInfoItem iii : targetUserRatings){
			targetUserAvgRating += iii.rating;
			targetUserRatingsMap.put(iii.element, iii.rating);
		}
		targetUserAvgRating /= targetUserRatings.size();
		
		double prediction = 0d;
		double sumOfSimilarities = 0.d;
		
		for(IdSimilarityPair pair : topNeighborsList){
			long currentItem = pair.id;
			double currentSimilarity = pair.similarity;
			sumOfSimilarities += currentSimilarity;
			if(targetUserRatingsMap.containsKey(currentItem)){
				prediction += targetUserRatingsMap.get(currentItem)*currentSimilarity;
			}
			else{
				prediction += targetUserAvgRating*currentSimilarity;
			}
		}
		
		return prediction / sumOfSimilarities;
	}









}
