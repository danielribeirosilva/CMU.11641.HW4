import java.util.Iterator;
import java.util.LinkedList;


public class ItemSimilarity {
	// if one of the users has not rated item, then use average rating from user
	public static double dotProductSimilarity(LinkedList<InteractionInfoUser> aFV, double avgRatingA, LinkedList<InteractionInfoUser> bFV, double avgRatingB){
		
		
		Iterator<InteractionInfoUser> itA = aFV.iterator();
		Iterator<InteractionInfoUser> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoUser a = itA.next();
		InteractionInfoUser b = itB.next();
		
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
	
	
	// if one of the users has not rated item, then use average rating
	public static double cosineSimilarity(LinkedList<InteractionInfoUser> aFV, double avgRatingA, LinkedList<InteractionInfoUser> bFV, double avgRatingB){
		
		
		Iterator<InteractionInfoUser> itA = aFV.iterator();
		Iterator<InteractionInfoUser> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoUser a = itA.next();
		InteractionInfoUser b = itB.next();
		
		double similarity = 0d;
		double sumSquareRatingA = 0d, sumSquareRatingB = 0d;
		boolean endReached = false;
		
		while(!endReached){
			
			// id(a) < id(b)
			while(a.compareTo(b) < 0){
				similarity += a.rating * avgRatingB;
				sumSquareRatingA += Math.pow(a.rating, 2);
				sumSquareRatingB += Math.pow(avgRatingB, 2);
				if(itA.hasNext()){
					a = itA.next();
				}
				else{
					endReached=true;
					break;
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
					endReached=true;
					break;
				}
			}
			// same id => compute similarity
			if(a.compareTo(b) == 0){
				similarity += a.rating * b.rating;
				sumSquareRatingA += Math.pow(a.rating, 2);
				sumSquareRatingB += Math.pow(b.rating, 2);
				if(itA.hasNext() && itB.hasNext()){
					a = itA.next();
					b = itB.next();
				}
				else{
					endReached=true;
				}
			}
	
		}
		
		double normA = Math.sqrt(sumSquareRatingA);
		double normB = Math.sqrt(sumSquareRatingB);
		similarity /=  (normA*normB);
			
		return similarity;
	}
	
	// if one of the users has not rated item, then the product will be 0 (won't use average rating)
	public static double normalizedDotProductSimilarity(LinkedList<InteractionInfoUser> aFV, double avgRatingA, LinkedList<InteractionInfoUser> bFV, double avgRatingB){
		
		Iterator<InteractionInfoUser> itA = aFV.iterator();
		Iterator<InteractionInfoUser> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoUser a = itA.next();
		InteractionInfoUser b = itB.next();
		
		double similarity = 0.d;
		boolean endReached = false;
		
		while(!endReached){
			
			// id(a) < id(b)
			while(a.compareTo(b) < 0){
				if(itA.hasNext()){
					a = itA.next();
				}
				else{
					endReached = true;
					break;
				}
			}
			// id(a) > id(b)
			while(a.compareTo(b) > 0){
				if(itB.hasNext()){
					b = itB.next();
				}
				else{
					endReached = true;
					break;
				}
			}
			// same id -> compute normalized similarity
			if(a.compareTo(b) == 0){
				similarity += (a.rating - avgRatingA) * (b.rating - avgRatingB);
				if(itA.hasNext() && itB.hasNext()){
					a = itA.next();
					b = itB.next();
				}
				else{
					endReached = true;
					break;
				}
			}
			
		}
			
		return similarity;
	}
	
	
	public static double normalizedCosineSimilarity(LinkedList<InteractionInfoUser> aFV, double avgRatingA, LinkedList<InteractionInfoUser> bFV, double avgRatingB){
		
		Iterator<InteractionInfoUser> itA = aFV.iterator();
		Iterator<InteractionInfoUser> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoUser a = itA.next();
		InteractionInfoUser b = itB.next();
		
		double similarity = 0d;
		double sumSquareRatingA = 0d, sumSquareRatingB = 0d;
		boolean endReached = false;
		
		while(!endReached){
			
			// id(a) < id(b)
			while(a.compareTo(b) < 0){
				if(itA.hasNext()){
					a = itA.next();
				}
				else{
					endReached=true;
					break;
				}
			}
			// id(a) > id(b)
			while(a.compareTo(b) > 0){
				if(itB.hasNext()){
					b = itB.next();
				}
				else{
					endReached=true;
					break;
				}
			}
			// same id => compute similarity
			if(a.compareTo(b) == 0){
				double diffA = (double)a.rating - avgRatingA;
				double diffB = (double)b.rating - avgRatingB;
				similarity += diffA * diffB;
				sumSquareRatingA += Math.pow( diffA, 2);
				sumSquareRatingB += Math.pow( diffB, 2);
				if(itA.hasNext() && itB.hasNext()){
					a = itA.next();
					b = itB.next();
				}
				else{
					endReached=true;
				}
			}
	
		}
		
		double normA = Math.sqrt(sumSquareRatingA);
		double normB = Math.sqrt(sumSquareRatingB);
		double normalizationFactor = normA*normB;
		return (normalizationFactor==0) ? 0 : (similarity/normalizationFactor);
	}
	
	
}
