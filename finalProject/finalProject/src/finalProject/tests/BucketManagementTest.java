package finalProject.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Test;

import finalProject.main.BucketManager;
import finalProject.main.BucketName;
import finalProject.main.Video;

/**
 * Testing the BuckerManager class.
 */
public class BucketManagementTest {

	/**
	 * Tests that buckets are created.
	 */
	@Test
	public void CreateBuckets_BucketsCreated() {
		BucketManager.destroyInputBucket();
		BucketManager.destroyOutputBucket();
		
		assertFalse(BucketName.INPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.INPUT));
		assertFalse(BucketName.OUTPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.OUTPUT));
		
		BucketManager.createInputBucket();
		BucketManager.createOutputBucket();
		
		assertTrue(BucketName.INPUT.toString() + " bucket should exist",BucketManager.bucketExists(BucketName.INPUT));
		assertTrue(BucketName.OUTPUT.toString()+ " bucket should exist",BucketManager.bucketExists(BucketName.OUTPUT));
	}
	
	/**
	 * Tests that buckets are destroyed.
	 */
	@Test
	public void DestroyBuckets_BothBucketsDestroyed() {
		BucketManager.destroyInputBucket();
		BucketManager.destroyOutputBucket();
		
		assertFalse(BucketName.INPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.INPUT));
		assertFalse(BucketName.OUTPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.OUTPUT));
	}
	
	/**
	 * Tests that files can be added to the Input bucket.
	 */
	@Test
	public void AddFileToInputBucket_FileAddedToInputBucket() {
		BucketManager.createInputBucket();
		String filePath = "./src/finalProject/tests/resources/clouds.mp4";
		Video testVideo = new Video(filePath);
		assertTrue("File at '" + filePath + "' should exist",testVideo.canRead());
		
		try {
			testVideo.upload();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertTrue(BucketManager.fileExists(testVideo.getObjectKey()));
	}
	
	/**
	 * Tests that all files are removed from the Input bucket.
	 */
	@Test
	public void ClearInputBucket_BucketClearedOfFiles() {
		BucketManager.createInputBucket();
		String filePath = "./src/finalProject/tests/resources/clouds.mp4";
		Video testVideo = new Video(filePath);
		assertTrue("File at '" + filePath + "' should exist",testVideo.canRead());
		
		try {
			testVideo.upload();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertTrue(BucketManager.fileExists(testVideo.getObjectKey()));
		
		BucketManager.clearInputBucket();
		
		assertFalse(BucketManager.fileExists(testVideo.getObjectKey()));
	}
	
	/**
	 * Tests that generated input file IDs are unique..
	 */
	@Test
	public void GetNextInputFileID_NextInputFileIDReturned() {
		BucketManager.createInputBucket();
		Video testVideo1 = new Video("./src/finalProject/tests/resources/clouds.mp4");
		
		try {
			testVideo1.upload();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Video testVideo2 = new Video("./src/finalProject/tests/resources/clouds.mp4");
		
		assertTrue("Filenames should be the same",testVideo1.getName().equals(testVideo2.getName()));
		
		assertFalse("IDs should be different",testVideo1.getObjectKey().equals(testVideo2.getObjectKey()));
	}
	
	/**
	 * Reset buckets are tests.
	 */
	@AfterClass
	public static void ResetBuckets() {		
		BucketManager.createAllBuckets();
		BucketManager.clearAllBuckets();
	}
}
