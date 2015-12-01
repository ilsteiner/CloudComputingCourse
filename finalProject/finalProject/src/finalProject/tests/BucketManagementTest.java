package finalProject.tests;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import finalProject.main.BucketManager;
import finalProject.main.BucketName;
import finalProject.main.Video;

public class BucketManagementTest {

	@Test
	public void CreateBuckets_BucketsCreated() {
		BucketManager.destroyInputBucket();
		BucketManager.destroyOutputBucket();
		BucketManager.destroyThumbBucket();
		
		assertFalse(BucketName.INPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.INPUT));
		assertFalse(BucketName.OUTPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.OUTPUT));
		assertFalse(BucketName.THUMBNAILS.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.THUMBNAILS));
		
		BucketManager.createInputBucket();
		BucketManager.createOutputBucket();
		BucketManager.createThumbBucket();
		
		assertTrue(BucketName.INPUT.toString() + " bucket should exist",BucketManager.bucketExists(BucketName.INPUT));
		assertTrue(BucketName.OUTPUT.toString()+ " bucket should exist",BucketManager.bucketExists(BucketName.OUTPUT));
		assertTrue(BucketName.THUMBNAILS.toString()+ " bucket should exist",BucketManager.bucketExists(BucketName.THUMBNAILS));
	}
	
	@Test
	public void DestroyBuckets_BothBucketsDestroyed() {
		BucketManager.destroyInputBucket();
		BucketManager.destroyOutputBucket();
		BucketManager.destroyThumbBucket();
		
		assertFalse(BucketName.INPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.INPUT));
		assertFalse(BucketName.OUTPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.OUTPUT));
		assertFalse(BucketName.THUMBNAILS.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.THUMBNAILS));
	}
	
	@Test
	public void AddFileToInputBucket_FileAddedToInputBucket() {
		BucketManager.createInputBucket();
		String filePath = "./src/finalProject/tests/resources/clouds.mp4";
		Video testVideo = new Video(filePath);
		assertTrue("File at '" + filePath + "' should exist",testVideo.canRead());
		
		testVideo.upload();
		
		assertTrue(BucketManager.fileExists(testVideo.getObjectKey()));
	}
	
	@Test
	public void ClearBuckets_BucketsClearedOfFiles() {
		BucketManager.createInputBucket();
		String filePath = "./src/finalProject/tests/resources/clouds.mp4";
		Video testVideo = new Video(filePath);
		assertTrue("File at '" + filePath + "' should exist",testVideo.canRead());
		
		testVideo.upload();
		
		assertTrue(BucketManager.fileExists(testVideo.getObjectKey()));
		
		BucketManager.clearInputBucket();
		
		assertFalse(BucketManager.fileExists(testVideo.getObjectKey()));
	}
	
	@Test
	public void GetNextInputFileID_NextInputFileIDReturned() {
		BucketManager.createInputBucket();
		Video testVideo1 = new Video("./src/finalProject/tests/resources/clouds.mp4");
		
		testVideo1.upload();
		
		Video testVideo2 = new Video("./src/finalProject/tests/resources/clouds.mp4");
		
		assertTrue("Filenames should be the same",testVideo1.getName().equals(testVideo2.getName()));
		
		assertFalse("IDs should be different",testVideo1.getObjectKey().equals(testVideo2.getObjectKey()));
	}
	
	@AfterClass
	public static void ResetBuckets() {		
		BucketManager.createAllBuckets();
		BucketManager.clearAllBuckets();
	}
}
