package finalProject.main;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.elastictranscoder.model.Notifications;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.Topic;
import com.amazonaws.services.sns.util.Topics;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class NotificationManager {
	private static final AmazonSNS sns = new AmazonSNSClient(new ProfileCredentialsProvider("finalProject"));
	private static final AmazonSQSClient sqs = new AmazonSQSClient(new ProfileCredentialsProvider("finalProject"));
	private static final String complete = "PipelineComplete";
	private static final String error = "PipelineError";
	private static final String progress = "PipelineProgress";
	private static final String warning = "PipelineWarning";
	private static final String queueName = "TranscoderQueue";
	
	public NotificationManager() {
		
	}

	public static List<String> createAllTopics() {
		ArrayList<String> arns = new ArrayList<>();
		
		arns.add(createCompleteTopic());
		arns.add(createErrorTopic());
		arns.add(createProgressTopic());
		arns.add(createWarningTopic());
		
		return arns;
	}

	public static String createCompleteTopic() {
		CreateTopicRequest request = new CreateTopicRequest().withName(complete);
		return sns.createTopic(request).getTopicArn();
	}

	public static String createErrorTopic() {
		CreateTopicRequest request = new CreateTopicRequest().withName(error);
		return sns.createTopic(request).getTopicArn();
	}

	public static String createProgressTopic() {
		CreateTopicRequest request = new CreateTopicRequest().withName(progress);
		return sns.createTopic(request).getTopicArn();
	}

	public static String createWarningTopic() {
		CreateTopicRequest request = new CreateTopicRequest().withName(warning);
		return sns.createTopic(request).getTopicArn();
	}

	public static List<Topic> listTopics() {
		return sns.listTopics().getTopics();
	}
	
	public static Notifications getNotifications(){
		Notifications notifications = new Notifications();
		
		for(String arn : createAllTopics()){
			if(arn.contains(complete))
				notifications.setCompleted(arn);
			if(arn.contains(error))
				notifications.setError(arn);
			if(arn.contains(progress))
				notifications.setProgressing(arn);
			if(arn.contains(warning))
				notifications.setWarning(arn);
		}
		
		return notifications;
	}
	
	private static String createQueue(){
		CreateQueueRequest request = new CreateQueueRequest(queueName);
		
		return sqs.createQueue(request).getQueueUrl();
	}
	
	public static void createAndSubscribe(){
		String queueURL = createQueue();
		
		for(Topic topic : listTopics()){
			Topics.subscribeQueue(sns, sqs, topic.getTopicArn(),queueURL);
		}
	}
	
	private static String getQueueURL(){
		for(String url : sqs.listQueues().getQueueUrls()){
			if(url.contains(queueName))
				return url;
		}
		
		return "";
	}
	
	public static List<String> pollQueues(Video video){
		ArrayList<String> messages = new ArrayList<String>();
		for(Message message : sqs.receiveMessage(getQueueURL()).getMessages()){
			message.getBody();
		}
		
		return messages;
	}
}
