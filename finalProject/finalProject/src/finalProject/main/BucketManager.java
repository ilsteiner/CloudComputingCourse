package finalProject.main;

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

	private static String createBucket(Enum<BucketName> bucketName){
		String name = bucketName.toString();
		if(!client.doesBucketExist(name)){
			client.createBucket(name);
		}
		return client.getBucketLocation(name);
	}
	
	public static void createInputBucket(){
		createBucket(BucketName.INPUT);
	}
	
	public static void createOutputBucket(){
		createBucket(BucketName.OUTPUT);
	}
	
	public static void createThumbBucket(){
		createBucket(BucketName.THUMBNAILS);
	}
	
	public static void destroyInputBucket(){
		destroyBucket(BucketName.INPUT);
	}
	
	public static void destroyOutputBucket(){
		destroyBucket(BucketName.OUTPUT);
	}
	
	public static void destroyThumbBucket(){
		destroyBucket(BucketName.THUMBNAILS);
	}
	
	public static void destroyBucket(Enum<BucketName> bucketName){
		if(client.doesBucketExist(bucketName.toString())){
			client.deleteBucket(bucketName.toString());
		}
	}
	
	public static boolean bucketExists(Enum<BucketName> bucketName){
		return client.doesBucketExist(bucketName.toString());
	}
}
