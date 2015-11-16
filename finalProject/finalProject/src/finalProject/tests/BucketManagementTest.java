package finalProject.tests;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import finalProject.main.BucketManager;
import finalProject.main.BucketName;

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
	
	@AfterClass
	public static void CreateBuckets() {
		BucketManager.createInputBucket();
		BucketManager.createOutputBucket();
		BucketManager.createThumbBucket();
	}
}
