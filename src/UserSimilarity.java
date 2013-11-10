import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;

public class UserSimilarity {

	
	// if one of the users has not rated item, then use average rating from user
	public static double dotProductSimilarity(LinkedList<InteractionInfoItem> aFV, double avgRatingA, LinkedList<InteractionInfoItem> bFV, double avgRatingB){
		
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
	
	
	// if one of the users has not rated item, then use average rating
	public static double cosineSimilarity(LinkedList<InteractionInfoItem> aFV, double avgRatingA,  LinkedList<InteractionInfoItem> bFV, double avgRatingB){
		
		
		Iterator<InteractionInfoItem> itA = aFV.iterator();
		Iterator<InteractionInfoItem> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoItem a = itA.next();
		InteractionInfoItem b = itB.next();
		
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
					endReached = true;
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
					endReached = true;
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
					endReached = true;
				}
			}
	
		}
		
		double normA = Math.sqrt(sumSquareRatingA);
		double normB = Math.sqrt(sumSquareRatingB);
		similarity /=  (normA*normB);
			
		return similarity;
	}
	
	

	// if one of the users has not rated item, then the product will be 0 (won't use average rating)
	public static double normalizedDotProductSimilarity(LinkedList<InteractionInfoItem> aFV, double avgRatingA,  LinkedList<InteractionInfoItem> bFV, double avgRatingB, HashMap<Long, Double> itemsAvgRating, double globalRatingAvg){
		
		Iterator<InteractionInfoItem> itA = aFV.iterator();
		Iterator<InteractionInfoItem> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoItem a = itA.next();
		InteractionInfoItem b = itB.next();
		
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
			// same id -> compute similarity
			if(a.compareTo(b) == 0){
				double itemAvgRating = itemsAvgRating.get(a.element);
				double realDiffA = (a.rating - avgRatingA) - (itemAvgRating - globalRatingAvg);
				double realDiffB = (b.rating - avgRatingB) - (itemAvgRating - globalRatingAvg);
				similarity += realDiffA * realDiffB;
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

	

	public static double normalizedCosineSimilarity(LinkedList<InteractionInfoItem> aFV, double avgRatingA,  LinkedList<InteractionInfoItem> bFV, double avgRatingB, HashMap<Long, Double> itemsAvgRating, double globalRatingAvg){
		
		
		Iterator<InteractionInfoItem> itA = aFV.iterator();
		Iterator<InteractionInfoItem> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoItem a = itA.next();
		InteractionInfoItem b = itB.next();
		
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
			// same id => compute similarity
			if(a.compareTo(b) == 0){
				double itemAvgRating = itemsAvgRating.get(a.element);
				double realDiffA = (a.rating - avgRatingA) - (itemAvgRating - globalRatingAvg);
				double realDiffB = (b.rating - avgRatingB) - (itemAvgRating - globalRatingAvg);
				similarity += realDiffA * realDiffB;
				sumSquareRatingA += Math.pow( realDiffA, 2);
				sumSquareRatingB += Math.pow( realDiffB, 2);
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
		double normalizationFactor = normA*normB;
		return (normalizationFactor==0) ? 0d : (similarity/normalizationFactor);
	}
	
public static double pearsonSimilarity(LinkedList<InteractionInfoItem> aFV, double avgRatingA,  LinkedList<InteractionInfoItem> bFV, double avgRatingB){
		
		
		Iterator<InteractionInfoItem> itA = aFV.iterator();
		Iterator<InteractionInfoItem> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoItem a = itA.next();
		InteractionInfoItem b = itB.next();
		
		double similarity = 0d;
		double sumSquareUnbiasedRatingA = 0d, sumSquareUnbiasedRatingB = 0d;
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
			// same id => compute similarity
			if(a.compareTo(b) == 0){
				double realDiffA = (a.rating - avgRatingA);
				double realDiffB = (b.rating - avgRatingB);
				similarity += realDiffA * realDiffB;
				sumSquareUnbiasedRatingA += Math.pow( realDiffA, 2);
				sumSquareUnbiasedRatingB += Math.pow( realDiffB, 2);
				if(itA.hasNext() && itB.hasNext()){
					a = itA.next();
					b = itB.next();
				}
				else{
					break;
				}
			}
	
		}
		
		double normA = Math.sqrt(sumSquareUnbiasedRatingA);
		double normB = Math.sqrt(sumSquareUnbiasedRatingB);
		double normalizationFactor = normA*normB;
		return (normalizationFactor==0) ? 0d : (similarity/normalizationFactor);
	}
	
	//this custom smilarity measure is an extension of the normalized cosine similarity
	//extensions include: time-variable similarity based on rating date
	public static double customSimilarity(LinkedList<InteractionInfoItem> aFV, double avgRatingA,  LinkedList<InteractionInfoItem> bFV, double avgRatingB, HashMap<Long, Double> itemsAvgRating, double globalRatingAvg) throws ParseException{
		
		
		Iterator<InteractionInfoItem> itA = aFV.iterator();
		Iterator<InteractionInfoItem> itB = bFV.iterator();
		
		if(aFV.size()<1 || bFV.size()<1)
			return 0.d;
		
		InteractionInfoItem a = itA.next();
		InteractionInfoItem b = itB.next();
		
		//date component
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date oldestA = sdf.parse("2050-12-31");
		Date newestA = sdf.parse("1970-01-01");
		for(InteractionInfoItem iii : aFV){
			Date currentDate = sdf.parse(iii.getRatingDate());
			if(currentDate.getTime() < oldestA.getTime()){
				oldestA = currentDate;
			}
			if(currentDate.getTime() > newestA.getTime()){
				newestA = currentDate;
			}
		}
		long ratingPeriodA = (newestA.getTime() - oldestA.getTime())/(1000*60*60*24);
		Date oldestB = sdf.parse("2050-12-31");
		Date newestB = sdf.parse("1970-01-01");
		for(InteractionInfoItem iii : bFV){
			Date currentDate = sdf.parse(iii.getRatingDate());
			if(currentDate.getTime() < oldestB.getTime()){
				oldestB = currentDate;
			}
			if(currentDate.getTime() > newestB.getTime()){
				newestB = currentDate;
			}
		}
		long ratingPeriodB = (newestB.getTime() - oldestB.getTime())/(1000*60*60*24);
		
		
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
			// same id => compute similarity
			if(a.compareTo(b) == 0){
				//bias
				double itemAvgRating = itemsAvgRating.get(a.element);
				double realDiffA = (a.rating - avgRatingA) - (itemAvgRating - globalRatingAvg);
				double realDiffB = (b.rating - avgRatingB) - (itemAvgRating - globalRatingAvg);
				//time component
				double freshnessA = (ratingPeriodA < 5) ? 0 : ( (newestA.getTime() - sdf.parse(a.ratingDate).getTime()) / (24d*60d*60d*1000d) ) / ratingPeriodA;
				double freshnessB = (ratingPeriodB < 5) ? 0 : ( (newestB.getTime() - sdf.parse(b.ratingDate).getTime()) / (24d*60d*60d*1000d) ) / ratingPeriodB;
				double freshnessBoostingA = Math.exp((1d-freshnessA)/3d);
				double freshnessBoostingB = Math.exp((1d-freshnessB)/3d);
				double freshnessBoosting = freshnessBoostingA * freshnessBoostingB;
				similarity += freshnessBoosting * realDiffA * realDiffB;
				sumSquareRatingA += Math.pow( realDiffA, 2);
				sumSquareRatingB += Math.pow( realDiffB, 2);
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
		double normalizationFactor = normA*normB;
		return (normalizationFactor==0) ? 0d : (similarity/normalizationFactor);
	}
	
	
	
	
}
