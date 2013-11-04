
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
	
	public String toString(){
		return "[ " + id+" "+similarity + "]";
	}

}
