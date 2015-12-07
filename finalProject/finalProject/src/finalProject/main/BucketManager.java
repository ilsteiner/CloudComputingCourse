package finalProject.main;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.tika.Tika;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class BucketManager {
	private static final Region region = Region.getRegion(Regions.US_EAST_1);
	private static final AmazonS3Client client = new AmazonS3Client(new ProfileCredentialsProvider(PipelineManager.username))
			.withRegion(region);

	/**
	 * Creates the specified bucket if it does not exist
	 * @param bucketName enum corresponding to a valid bucket name
	 * @return the URL of the bucket
	 */
	private static String createBucket(Enum<BucketName> bucketName) {
		String name = bucketName.toString();
		if (!client.doesBucketExist(name)) {
			client.createBucket(name);
		}
		return client.getBucketLocation(name);
	}

	public static void createInputBucket() {
		createBucket(BucketName.INPUT);
	}

	public static void createOutputBucket() {
		createBucket(BucketName.OUTPUT);
	}

	public static void createThumbBucket() {
		createBucket(BucketName.THUMBNAILS);
	}

	public static void destroyInputBucket() {
		destroyBucket(BucketName.INPUT);
	}

	public static void destroyOutputBucket() {
		destroyBucket(BucketName.OUTPUT);
	}

	public static void destroyThumbBucket() {
		destroyBucket(BucketName.THUMBNAILS);
	}

	public static void destroyBucket(Enum<BucketName> bucketName) {
		if (client.doesBucketExist(bucketName.toString())) {
			clearBucket(bucketName);
			client.deleteBucket(bucketName.toString());
		}
	}

	public static void createAllBuckets() {
		createInputBucket();
		createOutputBucket();
		createThumbBucket();
	}

	public static void destroyAllBuckets() {
		destroyInputBucket();
		destroyOutputBucket();
		destroyThumbBucket();
	}

	public static void clearInputBucket() {
		clearBucket(BucketName.INPUT);
	}

	public static void clearOutputBucket() {
		clearBucket(BucketName.OUTPUT);
	}

	public static void clearThumbBucket() {
		clearBucket(BucketName.THUMBNAILS);
	}

	public static void clearAllBuckets() {
		clearInputBucket();
		clearOutputBucket();
		clearThumbBucket();
	}

	public static void clearBucket(Enum<BucketName> bucketName) {
		if (client.doesBucketExist(bucketName.toString())) {
			ObjectListing objects = client.listObjects(bucketName.toString());
			// Modified from here:
			// https://docs.aws.amazon.com/AmazonS3/latest/dev/delete-or-empty-bucket.html
			while (true) {
				for (Iterator<?> iterator = objects.getObjectSummaries().iterator(); iterator.hasNext();) {
					S3ObjectSummary objectSummary = (S3ObjectSummary) iterator.next();
					client.deleteObject(bucketName.toString(), objectSummary.getKey());
				}

				if (objects.isTruncated()) {
					objects = client.listNextBatchOfObjects(objects);
				} else {
					break;
				}
			}
			;
		}
	}

	public static boolean bucketExists(Enum<BucketName> bucketName) {
		return client.doesBucketExist(bucketName.toString());
	}

	public static boolean fileExists(String objectKey) {
		try {
			client.getObjectMetadata(BucketName.INPUT.toString(), objectKey);
			return true;
		} catch (AmazonS3Exception e) {
			return false;
		}
	}

	public static PutObjectResult addInputFile(Video video) throws IOException {
		if(!isVideo(video)){
			throw new IllegalArgumentException(video.getName() + " is not a valid video file");
		}
		PutObjectRequest request = new PutObjectRequest(BucketName.INPUT.toString(), video.getObjectKey(),
				video);
		return client.putObject(request);
	}

	public static int getFileID(String filename) {
		Random random = new Random();

		int id = Math.abs(random.nextInt());

		while (fileExists(id + "_" + filename)) {
			id = random.nextInt();
		}

		return id;
	}

	/**
	 * Format length in Bytes to human-readable format. Source:
	 * https://stackoverflow.com/a/5599842
	 * 
	 * @param size file size in bytes
	 * @return string representing file format
	 */
	public static String readableFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public static String getOutputURL(Video video) {
		try {
			client.setObjectAcl(BucketName.OUTPUT.toString(), video.getObjectKey(), CannedAccessControlList.PublicRead);
		} catch (Exception e) {
			int tries = 8;
			int waitSeconds = 2;
			for (int i = 0; i < tries; i++) {
				try {
					TimeUnit.SECONDS.sleep(waitSeconds);
					client.setObjectAcl(BucketName.OUTPUT.toString(), video.getObjectKey(),
							CannedAccessControlList.PublicRead);
					waitSeconds = waitSeconds * 2;
				} 
				catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				catch (Exception e1) {
					System.out.println("Could not make output file public.");
				} 
			}
		}
		return client.getResourceUrl(BucketName.OUTPUT.toString(), video.getObjectKey());
	}

	public static void deleteInputFile(Video video) {
		client.deleteObject(BucketName.INPUT.toString(), video.getObjectKey());
	}
	
	public static boolean isVideo(Video video) throws IOException{
		//Check if the file is actually a video
		Tika tika = new Tika();
		
		return tika.detect(video).contains("video");
	}
}
