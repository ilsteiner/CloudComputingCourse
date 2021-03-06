package hwk9_edu.edu.hu.client;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;


public class ClientSideQMonitor {
	/**
	 * prints current time and estimated length of queue (visible messages)
	 * @param args queueName (TestQueue), interval (5000 ms), iterations (10)
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        AmazonSQS sqs = new AmazonSQSClient();
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        sqs.setRegion(usEast1);
        
        String queueName = "TestQueue";
        int interval = 5000;
        int iterations = 10;
        
        if(args.length > 0){
        	queueName = args[0];
        }
        
        if(args.length > 1){
        	interval = Integer.parseInt(args[1]);
        }
        
        if(args.length > 2){
        	iterations = Integer.parseInt(args[2]);
        }
        
        // Create a queue
        System.out.println("Creating a new SQS queue called " + queueName + ".");
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        sqs.setEndpoint("http://queue.amazonaws.com/");

        // List queues
        System.out.println("Listing all queues in your account:");
        for (String queueUrl : sqs.listQueues().getQueueUrls()) {
            System.out.println("  QueueUrl: " + queueUrl);
        }
        
        System.out.println("Monitoring SQS");
        try {
	    // get queue length
            GetQueueAttributesRequest attrReq = new GetQueueAttributesRequest(myQueueUrl);
            attrReq. setAttributeNames( Arrays.asList(  
            		"ApproximateNumberOfMessages","ApproximateNumberOfMessagesNotVisible"
            		));            
            for(int i = 0;i<iterations;i++) {
            	GetQueueAttributesResult attrResult = sqs.getQueueAttributes(
            			attrReq );
            	Map<String,String> attrs = attrResult.getAttributes();
            	System.out.println(
            			(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format( new Date() ) )
            			+ ": " + attrs.get("ApproximateNumberOfMessages") );
            	Thread.sleep(interval);
            }
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
