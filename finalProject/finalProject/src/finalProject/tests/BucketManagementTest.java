package finalProject.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.s3.model.Bucket;

import finalProject.main.BucketManager;
import finalProject.main.InputBucket;
import finalProject.main.OutputBucket;

public class BucketManagementTest {

	@Test
	public void CreateBuckets_NeitherInputNorOutputBucketsExist_BucketsCreated() {
		BucketManager.destroyBucket(InputBucket.name);
		BucketManager.destroyBucket(OutputBucket.name);
		
		assertFalse(InputBucket.name + " bucket should not exist",BucketManager.bucketExists(InputBucket.name));
		assertFalse(OutputBucket.name + " bucket should not exist",BucketManager.bucketExists(OutputBucket.name));
		
		InputBucket input = new InputBucket();
		OutputBucket output = new OutputBucket();
		
		assertTrue(InputBucket.name + " bucket should exist",BucketManager.bucketExists(InputBucket.name));
		assertTrue(OutputBucket.name + " bucket should exist",BucketManager.bucketExists(OutputBucket.name));
	}

	@Test
	public void CreateBuckets_OnlyInputBucketExists_OutputBucketCreated() {
		fail("Not yet implemented");
	}
	
	@Test
	public void CreateBuckets_OnlyOutputBucketExists_InputBucketCreated() {
		fail("Not yet implemented");
	}
	
	@Test
	public void CreateBuckets_BothBucketsExist_NoBucketsCreated() {
		fail("Not yet implemented");
	}

	@Test
	public void DestroyBuckets_NeitherInputNorOutputBucketsExist_BucketsNotChanged() {
		fail("Not yet implemented");
	}

	@Test
	public void DestroyBuckets_OnlyInputBucketExists_InputBucketDestroyed() {
		fail("Not yet implemented");
	}
	
	@Test
	public void DestroyBuckets_OnlyOutputBucketExists_OutputBucketDestroyed() {
		fail("Not yet implemented");
	}
	
	@Test
	public void DestroyBuckets_BothBucketsExist_BothBucketsDestroyed() {
		fail("Not yet implemented");
	}
}
