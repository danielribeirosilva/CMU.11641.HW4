import java.util.HashMap;
import java.util.LinkedList;


public class RatingPrediction {
	
	public static double simpleAveragePrediction (long targetItemId, LinkedList<IdSimilarityPair> topNeighborsList, HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap){
		
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
	
	public static double weightedAveragePrediction (long targetItemId, LinkedList<IdSimilarityPair> topNeighborsList, HashMap<Long, LinkedList<InteractionInfoItem>> trainUserItemInteractionMap){
		
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
}
