package finalProject.main;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class BucketManager {
	private static final Region region = Region.getRegion(Regions.US_EAST_1);
	private static final AmazonS3 client = new AmazonS3Client().withRegion(region);
	
	public BucketManager() {
		// TODO Auto-generated constructor stub
	}

	public static String createBucket(String name){
		if(!client.doesBucketExist(name)){
			client.createBucket(name);
		}
		return client.getBucketLocation(name);
	}
	
	public static void destroyBucket(String name){
		if(client.doesBucketExist(name)){
			client.deleteBucket(name);
		}
	}
	
	public static boolean bucketExists(String name){
		return client.doesBucketExist(name);
	}
}
