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
		
		assertFalse(BucketName.INPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.INPUT));
		assertFalse(BucketName.OUTPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.OUTPUT));
		
		BucketManager.createInputBucket();
		BucketManager.createOutputBucket();
		
		assertTrue(BucketName.INPUT.toString() + " bucket should exist",BucketManager.bucketExists(BucketName.INPUT));
		assertTrue(BucketName.OUTPUT.toString()+ " bucket should exist",BucketManager.bucketExists(BucketName.OUTPUT));
	}
	
	@Test
	public void DestroyBuckets_BothBucketsDestroyed() {
		BucketManager.destroyInputBucket();
		BucketManager.destroyOutputBucket();
		
		assertFalse(BucketName.INPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.INPUT));
		assertFalse(BucketName.OUTPUT.toString() + " bucket should not exist",BucketManager.bucketExists(BucketName.OUTPUT));
	}
	
	@AfterClass
	public static void DestroyBuckets() {
		BucketManager.destroyInputBucket();
		BucketManager.destroyOutputBucket();
	}
}
