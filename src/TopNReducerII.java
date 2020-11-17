import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TopNReducerII extends Reducer<Text, LongWritable, Text, LongWritable> {

	private TreeMap<Long, String> tmap;
	static int topN;

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		tmap = new TreeMap<Long, String>();

		Configuration conf = context.getConfiguration();

		// we will use the value passed in myValue at runtime
		String param = conf.get("n");

		// converting the String value to integer
		topN = Integer.parseInt(param);
	}

	@Override
	public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

		// input data from mapper
		// key values
		// movie_name [ count ]
		String name = key.toString();
		long count = 0;

		for (LongWritable val : values) {
			count += val.get(); // needs to be += to work correctly
		}

		// insert data into treeMap,
		// we want top 10 viewed movies
		// so we pass count as key
		tmap.put(count, name);

		// we remove the first key-value
		// if it's size increases 10
		if (tmap.size() > topN) {
			tmap.remove(tmap.firstKey());
		}
	}

	@Override
	public void cleanup(Context context) throws IOException, InterruptedException {

		for (Entry<Long, String> entry : tmap.entrySet()) {

			long count = entry.getKey();
			String name = entry.getValue();
			context.write(new Text(name), new LongWritable(count));
		}
	}
}