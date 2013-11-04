
public class InteractionInfoUser extends InteractionInfo{
	
	
	public InteractionInfoUser(long user, int rating, String date){
		super(user, rating, date);
	}
	
	public InteractionInfoUser(long user, int rating) {
		super(user, rating);
	}

	public long getUserId(){
		return this.element;
	}
	
	public boolean equals (InteractionInfo o){
		return this.element==o.element;
	}
}
