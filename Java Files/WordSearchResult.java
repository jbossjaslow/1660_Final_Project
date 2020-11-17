public class WordSearchResult implements Comparable<WordSearchResult> {

	int docId = 0;
	String folder = "input";
	String fileName = "testFile";
	int freq = 0;

	public WordSearchResult(String folder, String fileName, int freq) {
		this.folder = folder;
		this.fileName = fileName;
		this.freq = freq;
	}

	@Override
	public int compareTo(WordSearchResult o) {
		return -Integer.compare(freq, o.freq);
	}
}
