
public class RatingDatePair {
	private int rating;
	private String ratingDate;
	
	public RatingDatePair(int rating, String ratingDate){
		this.rating = rating;
		this.ratingDate = ratingDate;
	}
	
	public int getRating(){
		return this.rating;
	} 
	
	public String getRatingDate(){
		return this.ratingDate;
	}
}
