package hu.cloud.edu;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.GetTopicAttributesRequest;
import com.amazonaws.services.sns.model.GetTopicAttributesResult;
import com.amazonaws.services.sns.model.SetTopicAttributesRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.Topic;

public class CreateTopic {

	public static void main(String[] args) throws Exception {
        AmazonSNS sns = new AmazonSNSClient();
		try {
			// Create a Topic
			System.out.println("Creating MyTopic.\n");
			CreateTopicRequest createTopicRequest = new CreateTopicRequest().withName("MyTopic");
			
			// Retrieve Amazon Resource Name
			String myTopicArn = sns.createTopic(createTopicRequest).getTopicArn();
			System.out.println("Topic created: " + myTopicArn);
			
			Thread.sleep(1000);
			
			sns.setTopicAttributes(new SetTopicAttributesRequest(myTopicArn,"DisplayName","TestTopic"));
			
			// List Topics
			System.out.println("List topics: ");
			for (Topic topic : sns.listTopics().getTopics()) {
				System.out.println(" TopicArn: " + topic.getTopicArn());
			}
			
			// Subscribe email to topic
			//subscribe(sns, myTopicArn, "ilebwohlsteiner@gmail.com");
			//subscribe(sns, myTopicArn, "16314069666");

			// Fetch Topic attributes
			System.out.println("Topic attributes: ");
			fetchAttr(sns, myTopicArn);
			
			//Send a message
			System.out.println("Sending message with id " + sns.publish(myTopicArn, "This is the message that I'm sending via both SMTP and SMS!").getMessageId());
			
			//Print subscription arns
			System.out.println(sns.listSubscriptions().toString());
			
			// Delete a Topic
			// System.out.println("Deleting the test Topic.\n");
			// sns.deleteTopic(newDeleteTopicRequest().withTopicArn(myTopicArn));
		} catch (AmazonServiceException ase) {
			System.out.println("Error Message: " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code: " + ase.getErrorCode());
			System.out.println("Error Type: " + ase.getErrorType());
			System.out.println("Request ID: " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
	private static void subscribe(AmazonSNS sns, String myTopicArn, String dest) {
		String numRegex = "[0-9]+";
		String emailRegex = ".+@.+";
		
		SubscribeRequest subReq;
		
		if(dest.matches(numRegex)){
			subReq = new SubscribeRequest(myTopicArn, "sms", dest);
		}
		
		else if(dest.matches(emailRegex)){
			subReq = new SubscribeRequest(myTopicArn, "email", dest);
		}
		else{
			throw new InvalidParameterException();
		}
		
		SubscribeResult subRes = sns.subscribe(subReq);
		String subscribedTopicArn = subRes.getSubscriptionArn();
		System.out.println("Subscribed " + dest + " to topic: " + subscribedTopicArn);
	}

	private static void fetchAttr(AmazonSNS sns, String myTopicArn) {
		GetTopicAttributesRequest getTARequest = new GetTopicAttributesRequest().withTopicArn(myTopicArn);
		GetTopicAttributesResult getTAResult = sns.getTopicAttributes(getTARequest);
		Map<String, String> attributes = new HashMap<String, String>();
		attributes = getTAResult.getAttributes();
		for (String key : attributes.keySet()) {
			System.out.println(key + ": " + attributes.get(key));
		}
	}
}
