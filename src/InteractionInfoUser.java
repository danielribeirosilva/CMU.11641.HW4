
public class InteractionInfoUser implements Comparable<InteractionInfoUser>{
	private long userId;
	private int rating;
	private String ratingDate;
	
	public InteractionInfoUser(long userId, int rating, String date){
		this.userId = userId;
		this.rating = rating;
		this.ratingDate = date;
	}
	
	public InteractionInfoUser(long userId, int rating){
		this.userId = userId;
		this.rating = rating;
		this.ratingDate = "";
	}
	
	public long getUserId(){
		return this.userId;
	}
	
	public int getRating(){
		return this.rating;
	}
	
	public String getRatingDate(){
		return this.ratingDate;
	}
	
	public String toString(){
		return "[" + this.userId + " : " + Integer.toString(this.rating) + " / " + this.ratingDate + "]";
	}

	@Override
	public int compareTo(InteractionInfoUser o) {
		if(this.userId < o.userId)
			return -1;
		if(this.userId > o.userId)
			return 1;
		return 0;
	}
	
}
