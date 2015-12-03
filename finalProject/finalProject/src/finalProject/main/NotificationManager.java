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
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class NotificationManager {
	private static final AmazonSNS sns = new AmazonSNSClient(new ProfileCredentialsProvider("finalProject"));
	private static final AmazonSQSClient sqs = new AmazonSQSClient(new ProfileCredentialsProvider("finalProject"));
	private static final String complete = "PipelineComplete";
	private static final String error = "PipelineError";
	private static final String progress = "PipelineProgress";
	private static final String warning = "PipelineWarning";
	
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
	
	private static String createQueue(String queueName){
		CreateQueueRequest request = new CreateQueueRequest(queueName);
		
		return sqs.createQueue(request).getQueueUrl();
	}
	
	public static void createAndSubscribe(){
		String completeQueue = createQueue(complete);
		String errorQueue = createQueue(error);
		String progressQueue = createQueue(progress);
		String warningQueue = createQueue(warning);
		
		for(Topic topic : listTopics()){
			if(topic.getTopicArn().contains(complete))
				Topics.subscribeQueue(sns, sqs, topic.getTopicArn(), completeQueue);
			if(topic.getTopicArn().contains(error))
				Topics.subscribeQueue(sns, sqs, topic.getTopicArn(), errorQueue);
			if(topic.getTopicArn().contains(progress))
				Topics.subscribeQueue(sns, sqs, topic.getTopicArn(), progressQueue);
			if(topic.getTopicArn().contains(warning))
				Topics.subscribeQueue(sns, sqs, topic.getTopicArn(), warningQueue);
		}
	}
	
	private static String getQueueURL(String name){
		for(String url : sqs.listQueues().getQueueUrls()){
			if(url.contains(name))
				return url;
		}
		
		return "";
	}
	
	public static List<String> pollQueue(Video video,String queueName){
		ArrayList<String> messages = new ArrayList<String>();
		
			for(Message message : sqs.receiveMessage(getQueueURL(queueName)).getMessages()){
				messages.add(message.toString());
				sqs.deleteMessage(new DeleteMessageRequest(getQueueURL(queueName),message.getReceiptHandle()));
		}
		
		return messages;
	}
	
	public static List<String> getProgress(Video video){
		return pollQueue(video,progress);
	}
	
	public static List<String> getError(Video video){
		return pollQueue(video,error);
	}
	
	public static List<String> getComplete(Video video){
		return pollQueue(video,complete);
	}
	
	public static List<String> getWarning(Video video){
		return pollQueue(video,warning);
	}

	public static void clearQueue(Video video) {
		while(getProgress(video).size() > 0);
		while(getError(video).size() > 0);
		while(getComplete(video).size() > 0);
		while(getWarning(video).size() > 0);
	}
}
