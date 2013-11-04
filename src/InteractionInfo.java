
public abstract class InteractionInfo implements Comparable<InteractionInfo>{

	public long element;
	public int rating;
	public String ratingDate;
	
	public InteractionInfo(long element, int rating, String date){
		this.element = element;
		this.rating = rating;
		this.ratingDate = date;
	}
	
	public InteractionInfo(long element, int rating){
		this.element = element;
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
		return "[" + this.element + " : " + Integer.toString(this.rating) + " / " + this.ratingDate + "]";
	}
	
	@Override
	public int compareTo(InteractionInfo o) {
		if(this.element < o.element)
			return -1;
		if(this.element > o.element)
			return 1;
		return 0;
	}

}
