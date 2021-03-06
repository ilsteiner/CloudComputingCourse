package hwk9_edu.edu.hu.as;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class ServerSideConsumer {
	
	public static void main(String[] args) throws Exception {

		AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials("", ""));

        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        sqs.setRegion(usEast1);
        
        int interval = 5000;
        
        if(args.length > 1){
        	interval = Integer.parseInt(args[1]);
        }

        System.out.println("===========================================");
        System.out.println("CONSUMER");
        System.out.println("===========================================\n");

        try {
        	while (true) {
        		// List queues
        		for (String queueUrl : sqs.listQueues().getQueueUrls()) {
        			System.out.println("\nQueueUrl: " + queueUrl);

        			// Receive messages
        			System.out.print("Checking msg: " );
        			Message msg = consumeMsg( sqs, queueUrl );
        			if ( msg != null ) {
        				System.out.println( msg.getBody() );
        				String messageReceiptHandle = msg.getReceiptHandle();
        				sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
        			} else {
        				System.out.println("No message consumed from queue.");
        			}
        		}
        		Thread.sleep(interval);
        		System.out.println();
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
    
    private static Message consumeMsg( AmazonSQS qsvc, String queueUrl ) {
    	// Receive messages
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
        	.withMaxNumberOfMessages( new Integer(1) );
        List<Message> messages = qsvc.receiveMessage(receiveMessageRequest).getMessages();
        return messages.isEmpty() ? null : messages.get( 0 );
    }

}
