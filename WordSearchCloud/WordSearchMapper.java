import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordSearchMapper extends Mapper<Object, Text, Text, Text> {

	static String wordToFind;

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();

		// we will use the value passed in myValue at runtime
		wordToFind = conf.get("word");
	}

	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String[] tokens = value.toString().split("\t");
		String currentWord = tokens[0];
		if (currentWord.equals(wordToFind)) {
			context.write(new Text(currentWord), new Text(tokens[1]));
		}
	}
}
