package finalProject.main;

import com.amazonaws.services.s3.model.Bucket;

public class InputBucket extends Bucket {
	public static final String name = "TranscoderInput";

	public InputBucket() {
		super(name);
		BucketManager.createBucket(name);
	}
}
