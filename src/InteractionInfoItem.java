
public class InteractionInfoItem extends InteractionInfo {


	public InteractionInfoItem(long item, int rating, String date){
		super(item, rating, date);
	}
	
	public InteractionInfoItem(long item, int rating) {
		super(item, rating);
	}

	public long getItemId(){
		return this.element;
	}
	
	public boolean equals (InteractionInfo o){
		return this.element==o.element;
	}

}
