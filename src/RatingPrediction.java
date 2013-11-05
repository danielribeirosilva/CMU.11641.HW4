import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


public class RatingPrediction {
	
	/*------------ USER-BASED --------*/
	/*
	public static double userBasedSimpleAveragePredictionOld (long targetItemId, LinkedList<IdSimilarityPair> topNeighborsList, HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap){
		
		double prediction = 0.d;
		
		for(IdSimilarityPair idSim : topNeighborsList){
			long currentNeighbor = idSim.id;
			LinkedList<InteractionInfoItem> neighborFV = trainUserItemInteractionMap.get(currentNeighbor);
			
			Boolean containsTargetItemId = false;
			double sumNeighborRating = 0;
			for (InteractionInfoItem iii : neighborFV){
				if(iii.element != targetItemId){
					sumNeighborRating += iii.rating;
				}
				else if(iii.element == targetItemId){
					containsTargetItemId = true;
					prediction += iii.rating;
					break;
				}
			}
			//if neighbor hasn't rated that movie, get average rating
			if(!containsTargetItemId){
				prediction += sumNeighborRating/neighborFV.size();
			}
		}
		
		return prediction / topNeighborsList.size();
	}
	*/
	
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
	
	
	public static double userBasedWeightedAveragePrediction (long targetItemId, LinkedList<IdSimilarityPair> topNeighborsList, HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap){
		
		double prediction = 0.d;
		double sumOfSimilarities = 0.d;
		
		for(IdSimilarityPair idSim : topNeighborsList){
			long currentNeighbor = idSim.id;
			double currentSimilarity = idSim.similarity;
			sumOfSimilarities += currentSimilarity; 
			
			LinkedList<InteractionInfoItem> neighborFV = trainUserItemInteractionMap.get(currentNeighbor);
			
			Boolean containsTargetItemId = false;
			double sumNeighborRating = 0;
			for (InteractionInfoItem iii : neighborFV){
				if(iii.element != targetItemId){
					sumNeighborRating += iii.rating;
				}
				else if(iii.element == targetItemId){
					containsTargetItemId = true;
					prediction += currentSimilarity*iii.rating;
					break;
				}
			}
			//if neighbor hasn't rated that movie, get average rating
			if(!containsTargetItemId){
				prediction += currentSimilarity*(sumNeighborRating/(double)neighborFV.size());
			}
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
