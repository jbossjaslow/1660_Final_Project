import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TopNMapperII extends Mapper<Object, Text, Text, LongWritable> {

	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		// input data format => movie_name
		// no_of_views (tab separated)
		// we split the input data
		String[] tokens = value.toString().split("\t");

		String word = tokens[0];
		String[] locations = tokens[1].split(",");
		for (String str : locations) {
			String count = str.split("/")[2];
			context.write(new Text(word), new LongWritable(Long.parseLong(count)));
		}
	}
}