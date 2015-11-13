package finalProject.main;

import com.amazonaws.services.s3.model.Bucket;

public class OutputBucket extends Bucket {
	public static final String name = "TranscoderOutput";

	public OutputBucket() {
		super(name);
		BucketManager.createBucket(name);
	}
}