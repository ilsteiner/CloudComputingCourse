import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import java.io.IOException;
import java.util.ArrayList;

public class BucketManager {
    private String name;
    AmazonS3 amazonS3 = new AmazonS3Client();
    S3Object bucket = new S3Object();

    public BucketManager(String name){
        this.name = name;
    }

    /**
     * Given a directory prefix, delete contents of the directory, then delete the directory
     * @param name the name of the directory to delete
     */
    public void deleteFolder(String name){

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

    public static void main(String[] args) {
        BucketManager bucketManager = new BucketManager("isteinere90");

        ArrayList<String> files = bucketManager.getFiles();

        for(String file:files){
            System.out.println(file);
        }
    }
}
