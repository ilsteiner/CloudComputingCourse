import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BucketManager {
    private String name;
    AmazonS3 amazonS3 = new AmazonS3Client();

    public BucketManager(String name){
        this.name = name;
    }

    public ArrayList<String> getFiles(){
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(name);

        ObjectListing objectListing;

        objectListing = amazonS3.listObjects(listObjectsRequest);

        ArrayList<String> objects = new ArrayList<String>();

        for(S3ObjectSummary objectSummary:objectListing.getObjectSummaries()){
            objects.add(objectSummary.getKey());
        }

        return objects;
    }

    /**
     * Given a directory prefix, delete contents of the directory, then delete the directory
     * @param name the name of the directory to delete
     */
    public int deleteFolder(String name){
        ArrayList<String> files = getFiles();

        ArrayList<KeyVersion> keys = new ArrayList<KeyVersion>();

        for(String file:files){
            if(file.startsWith(name)){
                keys.add(new KeyVersion(file));
            }
        }

        if(keys.size() > 0){
            DeleteObjectsRequest request = new DeleteObjectsRequest(this.name);

            request.setKeys(keys);

            DeleteObjectsResult result = amazonS3.deleteObjects(request);

            return result.getDeletedObjects().size();
        }
        else{
            return 0;
        }
    }

    public void deleteBucket(){
        ArrayList<String> files = getFiles();

        ArrayList<KeyVersion> keys = new ArrayList<KeyVersion>();

        for(String file:files){
            keys.add(new KeyVersion(file));
        }

        //Delete all objects in the bucket
        if(keys.size() > 0) {
            DeleteObjectsRequest request = new DeleteObjectsRequest(this.name);

            request.setKeys(keys);

            System.out.println("Deleted " + amazonS3.deleteObjects(request).getDeletedObjects().size() + " objects");
        }

        //Finally, delete the bucket
        DeleteBucketRequest request = new DeleteBucketRequest(this.name);

        System.out.println("Deleting bucket " + this.name);

        amazonS3.deleteBucket(request);

        System.out.println(this.name + " bucket deleted");
    }

    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("What's the name of the bucket?");

        BucketManager bucketManager;
        try {
            bucketManager = new BucketManager(in.readLine());

            bucketManager.deleteBucket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
