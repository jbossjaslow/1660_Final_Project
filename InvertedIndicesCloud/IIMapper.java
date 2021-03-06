import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class IIMapper extends Mapper<Object, Text, Text, Text> {

	Text fileNameAndPath;
	List<String> fileWhiteList;
	String currentFile;
	private ArrayList<String> stopWords;

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		// set up file white list
		Configuration conf = context.getConfiguration();
		String[] files = conf.get("filesList").split(",");
		fileWhiteList = Arrays.asList(files);

		Path path = ((FileSplit) context.getInputSplit()).getPath();
		String[] filePath = path.toString().split("/");
		String fileName = filePath[filePath.length - 1];
		String fileFolder = filePath[filePath.length - 2];
		fileNameAndPath = new Text(fileFolder + "/" + fileName);

		currentFile = fileName;

		stopWords = new ArrayList<String>();
		stopWords.add("the");
		stopWords.add("be");
		stopWords.add("to");
		stopWords.add("of");
		stopWords.add("and");
		stopWords.add("a");
		stopWords.add("in");
		stopWords.add("that");
		stopWords.add("have");
		stopWords.add("i");
	}

	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		if (!fileWhiteList.contains(currentFile)) // if current file is not in file white list, don't bother mapping it
			return;

		// adapted from
		// http://deepakmodi2006.blogspot.com/2012/05/jpos-source-code-in-internet.html
		StringTokenizer itr = new StringTokenizer(value.toString(), " \t\n\r\f\",.:;?![]"); // arg2 filters out unneeded punctuation

		String word;
		while (itr.hasMoreTokens()) {
			word = itr.nextToken().toLowerCase();
			if (!stopWords.contains(word))
				context.write(new Text(word), fileNameAndPath);
		}
	}
}
