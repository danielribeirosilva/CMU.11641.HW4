
public class IdSimilarityPair implements Comparable<IdSimilarityPair> {
	
	public long id;
	public double similarity;
	
	public IdSimilarityPair(long id, double similarity){
		this.id = id;
		this.similarity = similarity;
	}
	
	@Override
	public int compareTo(IdSimilarityPair o) {
		return Double.compare(this.similarity, o.similarity);
	}
	
	 @Override
    public int hashCode() {
        Long thisId = new Long(this.id);
        return thisId.hashCode();
    }
	
	public String toString(){
		return "[ " + id+" "+similarity + "]";
	}

}
