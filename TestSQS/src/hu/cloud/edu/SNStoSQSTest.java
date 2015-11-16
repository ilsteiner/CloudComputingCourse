package hu.cloud.edu;

import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Topic;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class SNStoSQSTest {
	public static final String queueName = "SNSQueue";
	public static String myQueueUrl;
	public static AmazonSQSClient sqs;
	public static AmazonSNSClient sns;
	public static String topicName = "TestTopic";
	public static String topicARN;

	public static void main(String[] args) {
		sqs = new AmazonSQSClient();
		sns = new AmazonSNSClient();
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        sqs.setRegion(usEast1);

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");
        
        
            try {
				// Create a queue
				System.out.println("Creating a new SQS queue called " + queueName + " .");
				CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
				myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
				sqs.setEndpoint("http://queue.amazonaws.com/");
				
				// List queues
				System.out.println("Listing all queues in your account.\n");
				for (String queueUrl : sqs.listQueues().getQueueUrls()) {
				    System.out.println("  QueueUrl: " + queueUrl);
				}
				
				List<Topic> topics = sns.listTopics().getTopics();
				
				for(Topic topic:topics){
					if(topic.getTopicArn().contains(topicName)){
						topicARN = topic.getTopicArn();
					}
				}
				
				Topics.subscribeQueue(sns, sqs, topicARN, myQueueUrl);
				
				System.out.println("Listing all subscriptions:");
				System.out.println(sns.listSubscriptions());
				
				System.out.println("Send a message!");
				sns.publish(new PublishRequest(topicARN,"Hey! Java sent a message to my queue!","Message from Java"));
				
				System.out.println("See if the message is there");
				printMessages();				
				
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
	
	public static List<Message> printMessages(){
    	// Receive messages
        System.out.println("Receiving messages from " + queueName);
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl).withWaitTimeSeconds(10);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        for (Message message : messages) {
            System.out.println("  Message");
            System.out.println("    MessageId:     " + message.getMessageId());
            System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("    Body:          " + message.getBody());
            for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("  Attribute");
                System.out.println("    Name:  " + entry.getKey());
                System.out.println("    Value: " + entry.getValue());
            }
        }
        
        return messages;
    }
}
