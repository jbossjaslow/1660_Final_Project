import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class IIReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();

		values.forEach(text -> {
			String str = text.toString();
			int count = hmap.containsKey(str) ? hmap.get(str) + 1 : 1;
			hmap.put(str, count);
		});

		StringBuilder indices = new StringBuilder(); // using stringbuilder to control string representation of hmap
		for (Entry<String, Integer> e : hmap.entrySet()) {
			indices.append(e.getKey() + "/" + e.getValue() + ",");
		}

		context.write(key, new Text(indices.toString()));
	}
}
