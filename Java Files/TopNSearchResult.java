
public class TopNSearchResult implements Comparable<TopNSearchResult> {

	String term;
	int totalFreq;

	public TopNSearchResult(String term, int totalFreq) {
		this.term = term;
		this.totalFreq = totalFreq;
	}

	@Override
	public int compareTo(TopNSearchResult o) {
		// TODO Auto-generated method stub
		return -Integer.compare(totalFreq, o.totalFreq);
	}

}
