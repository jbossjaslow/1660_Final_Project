import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class IIDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		if (otherArgs.length < 3) {
			System.err.println("Error: please provide an input path, output path, and file whitelist");
			System.exit(2);
		}

		conf.set("filesList", otherArgs[2]);

		Job job = Job.getInstance(conf, "Create inverted indices");
		job.setJarByClass(IIDriver.class);

		job.setMapperClass(IIMapper.class);
		job.setReducerClass(IIReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		job.waitForCompletion(true);
	}
}
