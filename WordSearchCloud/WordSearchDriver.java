import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class WordSearchDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		if (otherArgs.length == 3)
			conf.set("word", otherArgs[2]);
		else {
			System.err.println("Error: please provide two paths and a count");
			System.exit(2);
		}

		Job job = Job.getInstance(conf, "Word search");
		job.setJarByClass(WordSearchDriver.class);

		job.setMapperClass(WordSearchMapper.class);
		job.setReducerClass(WordSearchReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setNumReduceTasks(1); // if not set, MR will use several reducers, even though there are few inputs

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		job.waitForCompletion(true);
	}
}
