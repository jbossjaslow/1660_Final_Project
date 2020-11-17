import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class TopNIIDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		if (otherArgs.length == 3)
			conf.set("n", otherArgs[2]);
		else {
			System.err.println("Error: please provide two paths and a count");
			System.exit(2);
		}

		Job job = Job.getInstance(conf, "top N from Inverted Indices");
		job.setJarByClass(TopNIIDriver.class);

		job.setMapperClass(TopNMapperII.class);
		job.setCombinerClass(TopNReducerII.class);
		job.setReducerClass(TopNReducerII.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		job.setNumReduceTasks(1); // if not set, MR will use several reducers, even though there are few inputs

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
