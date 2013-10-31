
public class InteractionInfo {
	private int rating;
	private String ratingDate;
	
	public InteractionInfo(int rating, String date){
		this.rating = rating;
		this.ratingDate = date;
	}
	
	public InteractionInfo(int rating){
		this.rating = rating;
		this.ratingDate = "";
	}
	
	public int getRating(){
		return this.rating;
	}
	
	public String getRatingDate(){
		return this.ratingDate;
	}
	
	public String toString(){
		return "[" + Integer.toString(this.rating) + " / " + this.ratingDate + "]";
	}
	
}
