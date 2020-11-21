public class WordSearchResult implements Comparable<WordSearchResult> {

	String folder = "input";
	String fileName = "testFile";
	int freq = 0;

	public WordSearchResult(String folder, String fileName, int freq) {
		this.folder = folder;
		this.fileName = fileName;
		this.freq = freq;
	}

	/**
	 * Generate a unique document id for each file name. The result should be
	 * reproducible for the same input
	 * 
	 * @return An integer that is unique
	 */
	public int getDocID() {
		return fileName.hashCode();
	}

	@Override
	public int compareTo(WordSearchResult o) {
		return -Integer.compare(freq, o.freq);
	}
}
