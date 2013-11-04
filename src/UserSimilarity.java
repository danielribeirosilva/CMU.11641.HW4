import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class UserSimilarity {

	
	// if one of the users has not rated item, then use average rating from user
	public static double dotProductSimilarity(LinkedList<InteractionInfoItem> aFV, LinkedList<InteractionInfoItem> bFV){
		
		//get averages for aFV and bFV
		double avgRatingA = 0, avgRatingB = 0;
		int sizeA = 0, sizeB = 0;
		for(InteractionInfoItem ii : aFV){
			avgRatingA += ii.rating;
			sizeA++;
		}
		avgRatingA /= sizeA;
		for(InteractionInfoItem ii : bFV){
			avgRatingB += ii.rating;
			sizeB++;
		}
		avgRatingB /= sizeB;
		
		
		Iterator<InteractionInfoItem> itA = aFV.iterator();
		Iterator<InteractionInfoItem> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoItem a = itA.next();
		InteractionInfoItem b = itB.next();
		
		double similarity = 0.d;
		
		while(true){
			
			// id(a) < id(b)
			while(a.compareTo(b) < 0){
				similarity += a.rating * avgRatingB;
				if(itA.hasNext()){
					a = itA.next();
				}
				else{
					return similarity;
				}
			}
			// id(a) > id(b)
			while(a.compareTo(b) > 0){
				similarity += b.rating * avgRatingA;
				if(itB.hasNext()){
					b = itB.next();
				}
				else{
					return similarity;
				}
			}
			// same id -> compute similarity
			if(a.compareTo(b) == 0){
				similarity += a.rating * b.rating;
				if(itA.hasNext() && itB.hasNext()){
					a = itA.next();
					b = itB.next();
				}
				else{
					break;
				}
			}
			
		}
			
		return similarity;
	}
	
	// if one of the users has not rated item, then the product will be 0 (won't use average rating)
	public static double strictDotProductSimilarity(List<InteractionInfoItem> aFV, List<InteractionInfoItem> bFV){
		
		Iterator<InteractionInfoItem> itA = aFV.iterator();
		Iterator<InteractionInfoItem> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoItem a = itA.next();
		InteractionInfoItem b = itB.next();
		
		double similarity = 0.d;
		
		while(true){
			
			// id(a) < id(b)
			while(a.compareTo(b) < 0){
				if(itA.hasNext()){
					a = itA.next();
				}
				else{
					return similarity;
				}
			}
			// id(a) > id(b)
			while(a.compareTo(b) > 0){
				if(itB.hasNext()){
					b = itB.next();
				}
				else{
					return similarity;
				}
			}
			// same id -> compute similarity
			if(a.compareTo(b) == 0){
				similarity += a.rating * b.rating;
				if(itA.hasNext() && itB.hasNext()){
					a = itA.next();
					b = itB.next();
				}
				else{
					break;
				}
			}
			
		}
			
		return similarity;
	}

	// if one of the users has not rated item, then use average rating
	public static double cosineSimilarity(LinkedList<InteractionInfoItem> aFV, LinkedList<InteractionInfoItem> bFV){
	
		//get averages for aFV and bFV
		double sumRatingA = 0, sumRatingB = 0;
		//get norm of aFV and bFV
		int sizeA = 0, sizeB = 0;
		for(InteractionInfoItem ii : aFV){
			sumRatingA += ii.rating;
			sizeA++;
		}
		double avgRatingA = sumRatingA / (double)sizeA;
		for(InteractionInfoItem ii : bFV){
			sumRatingB += ii.rating;
			sizeB++;
		}
		double avgRatingB = sumRatingB / (double)sizeB;
		
		
		Iterator<InteractionInfoItem> itA = aFV.iterator();
		Iterator<InteractionInfoItem> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoItem a = itA.next();
		InteractionInfoItem b = itB.next();
		
		double similarity = 0d;
		double sumSquareRatingA = 0d, sumSquareRatingB = 0d;
		
		while(true){
			
			// id(a) < id(b)
			while(a.compareTo(b) < 0){
				similarity += a.rating * avgRatingB;
				sumSquareRatingA += Math.pow(a.rating, 2);
				sumSquareRatingB += Math.pow(avgRatingB, 2);
				if(itA.hasNext()){
					a = itA.next();
				}
				else{
					return similarity;
				}
			}
			// id(a) > id(b)
			while(a.compareTo(b) > 0){
				similarity += b.rating * avgRatingA;
				sumSquareRatingA += Math.pow(avgRatingA, 2);
				sumSquareRatingB += Math.pow(b.rating, 2);
				if(itB.hasNext()){
					b = itB.next();
				}
				else{
					return similarity;
				}
			}
			// same id -> compute similarity
			if(a.compareTo(b) == 0){
				similarity += a.rating * b.rating;
				sumSquareRatingA += Math.pow(a.rating, 2);
				sumSquareRatingB += Math.pow(b.rating, 2);
				if(itA.hasNext() && itB.hasNext()){
					a = itA.next();
					b = itB.next();
				}
				else{
					break;
				}
			}
	
		}
		
		double normA = Math.sqrt(sumSquareRatingA);
		double normB = Math.sqrt(sumSquareRatingB);
		similarity /=  (normA*normB);
			
		return similarity;
	}
	
	
	
	
	
	
}
