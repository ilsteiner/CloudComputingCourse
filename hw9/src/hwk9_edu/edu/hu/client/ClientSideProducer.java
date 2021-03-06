package hwk9_edu.edu.hu.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class ClientSideProducer {
	/**
	 * Send a messages to the queue
	 * @param args queueName (TestQueue),interval (1000),messages (10)
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
	
	/* replace with your access key and secret key */
        AmazonSQS sqs = new AmazonSQSClient();

        System.out.println("===========================================");
        System.out.println("PRODUCER");
        System.out.println("===========================================\n");
        
        String queueName = "TestQueue";
        int interval = 5000;
        int messages = 10;
        
        if(args.length > 0){
        	queueName = args[0];
        }
        
        if(args.length > 1){
        	interval = Integer.parseInt(args[1]);
        }
        
        if(args.length > 2){
        	messages = Integer.parseInt(args[2]);
        }

        try {
        	for(int i = 0;i<messages;i++) {
        		String msg = "Message for " + queueName + " at: " 
				+ (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format( new Date() ) );
        		System.out.println("Sending a message: " + msg + "\n");
			/* replace with your sqs queue URL */
			sqs.sendMessage( new SendMessageRequest( "https://sqs.us-east-1.amazonaws.com/101517482224/" + queueName, msg ) );
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
