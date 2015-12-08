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

// TODO: Auto-generated Javadoc
/**
 * The Class BucketManager.
 */
public class BucketManager {
	
	/** The region in which to create buckets */
	private static final Region region = Region.getRegion(Regions.US_EAST_1);
	
	private static final AmazonS3Client client = new AmazonS3Client(new ProfileCredentialsProvider(PipelineManager.username))
			.withRegion(region);

	/**
	 * Creates the specified bucket if it does not exist.
	 *
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

	/**
	 * Creates the input bucket.
	 */
	public static void createInputBucket() {
		createBucket(BucketName.INPUT);
	}

	/**
	 * Creates the output bucket.
	 */
	public static void createOutputBucket() {
		createBucket(BucketName.OUTPUT);
	}

	/**
	 * Destroy input bucket.
	 */
	public static void destroyInputBucket() {
		destroyBucket(BucketName.INPUT);
	}

	/**
	 * Destroy output bucket.
	 */
	public static void destroyOutputBucket() {
		destroyBucket(BucketName.OUTPUT);
	}

	/**
	 * Destroy specified bucket.
	 *
	 * @param bucketName enum for the bucket name
	 */
	public static void destroyBucket(Enum<BucketName> bucketName) {
		if (client.doesBucketExist(bucketName.toString())) {
			clearBucket(bucketName);
			client.deleteBucket(bucketName.toString());
		}
	}

	/**
	 * Creates both buckets.
	 */
	public static void createAllBuckets() {
		createInputBucket();
		createOutputBucket();
	}

	/**
	 * Destroy both buckets.
	 */
	public static void destroyAllBuckets() {
		destroyInputBucket();
		destroyOutputBucket();
	}

	/**
	 * Clear input bucket.
	 */
	public static void clearInputBucket() {
		clearBucket(BucketName.INPUT);
	}

	/**
	 * Clear output bucket.
	 */
	public static void clearOutputBucket() {
		clearBucket(BucketName.OUTPUT);
	}

	/**
	 * Clear both buckets.
	 */
	public static void clearAllBuckets() {
		clearInputBucket();
		clearOutputBucket();
	}

	/**
	 * Clear specified bucket.
	 *
	 * @param bucketName enum for the bucket name
	 */
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

	/**
	 * Check if specified bucket exists.
	 *
	 * @param bucketName enum for the bucket name
	 * @return true, if bucket exists
	 */
	public static boolean bucketExists(Enum<BucketName> bucketName) {
		return client.doesBucketExist(bucketName.toString());
	}

	/**
	 * Check if file exists in the input bucket.
	 *
	 * @param objectKey the object key for the file to look for
	 * @return true, if file exists in input bucket
	 */
	public static boolean fileExists(String objectKey) {
		try {
			client.getObjectMetadata(BucketName.INPUT.toString(), objectKey);
			return true;
		} catch (AmazonS3Exception e) {
			return false;
		}
	}

	/**
	 * Adds file to input bucket.
	 *
	 * @param video the Video to add to the bucket
	 * @return the result of the put operation
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static PutObjectResult addInputFile(Video video) throws IOException {
		if(!isVideo(video)){
			throw new IllegalArgumentException(video.getName() + " is not a valid video file");
		}
		PutObjectRequest request = new PutObjectRequest(BucketName.INPUT.toString(), video.getObjectKey(),
				video);
		return client.putObject(request);
	}

	/**
	 * Gets a new random file ID. Guarantees a unique object key
	 * even if multiple files have the same base name. 
	 *
	 * @param filename the filename for which we need a unique id
	 * @return the unique id
	 */
	public static int getFileID(String filename) {
		Random random = new Random();

		int id = Math.abs(random.nextInt());

		while (fileExists(id + "_" + filename)) {
			id = random.nextInt();
		}

		return id;
	}

	/**
	 * Format length in Bytes to human-readable format.
	 * Source: https://stackoverflow.com/a/5599842
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

	/**
	 * Gets the URL of the file in the output bucket for this Video.
	 *
	 * @param video a Video with a transcoded file in the output bucket
	 * @return the URL of the transcoded file
	 */
	public static String getOutputURL(Video video) {
		//Make the output file publicly readable
		try {
			client.setObjectAcl(BucketName.OUTPUT.toString(), video.getObjectKey(), CannedAccessControlList.PublicRead);
		} catch (Exception e) {
			/**
			 * If there is an exception, it is probably because we asked for the
			 * file before it existed. We will do an exponential backoff, asking
			 * for the file at larger and larger time intervals until hopefully
			 * we get it. If we don't, then throw an exception. 
			 */
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

	/**
	 * Delete file from input bucket.
	 *
	 * @param video the Video to delete
	 */
	public static void deleteInputFile(Video video) {
		client.deleteObject(BucketName.INPUT.toString(), video.getObjectKey());
	}
	
	/**
	 * Checks if file is valid video.
	 *
	 * @param video the Video file
	 * @return true, if it is a video file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean isVideo(Video video) throws IOException{
		//Check if the file is actually a video
		Tika tika = new Tika();
		
		return tika.detect(video).contains("video");
	}
}
