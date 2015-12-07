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

/**
 * Methods to create and manage notifications using SNS.
 */
public class NotificationManager {
	
	/** SNS client. */
	private static final AmazonSNS sns = new AmazonSNSClient(new ProfileCredentialsProvider(PipelineManager.username));
	
	/** SQS client. */
	private static final AmazonSQSClient sqs = new AmazonSQSClient(new ProfileCredentialsProvider(PipelineManager.username));
	
	private static final String complete = "PipelineComplete";
	
	private static final String error = "PipelineError";
	
	private static final String progress = "PipelineProgress";
	
	private static final String warning = "PipelineWarning";
	
	/**
	 * Creates all required topics.
	 *
	 * @return a list of topics created
	 */
	public static List<String> createAllTopics() {
		ArrayList<String> arns = new ArrayList<>();
		
		arns.add(createCompleteTopic());
		arns.add(createErrorTopic());
		arns.add(createProgressTopic());
		arns.add(createWarningTopic());
		
		return arns;
	}

	/**
	 * Creates the Complete topic.
	 *
	 * @return the topic ARN
	 */
	public static String createCompleteTopic() {
		CreateTopicRequest request = new CreateTopicRequest().withName(complete);
		return sns.createTopic(request).getTopicArn();
	}

	/**
	 * Creates the Error topic.
	 *
	 * @return topic ARN
	 */
	public static String createErrorTopic() {
		CreateTopicRequest request = new CreateTopicRequest().withName(error);
		return sns.createTopic(request).getTopicArn();
	}

	/**
	 * Creates the Progress topic.
	 *
	 * @return the topic ARN
	 */
	public static String createProgressTopic() {
		CreateTopicRequest request = new CreateTopicRequest().withName(progress);
		return sns.createTopic(request).getTopicArn();
	}

	/**
	 * Creates the Warning topic.
	 *
	 * @return the topic ARN
	 */
	public static String createWarningTopic() {
		CreateTopicRequest request = new CreateTopicRequest().withName(warning);
		return sns.createTopic(request).getTopicArn();
	}

	/**
	 * List all topics topics.
	 *
	 * @return the list of topics
	 */
	public static List<Topic> listTopics() {
		return sns.listTopics().getTopics();
	}
	
	/**
	 * Creates a Notifications object for use when creating the Pipeline.
	 * This determines the topics to which the Pipeline will publish updates 
	 * as jobs progress.
	 *
	 * @return the Notifications object for all the required topics
	 */
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
	
	/**
	 * Creates a queue with the specified name.
	 *
	 * @param queueName the queue name
	 * @return the queue URL
	 */
	private static String createQueue(String queueName){
		CreateQueueRequest request = new CreateQueueRequest(queueName);
		
		return sqs.createQueue(request).getQueueUrl();
	}
	
	/**
	 * Creates all of the required queues and subscribes them to the correct Topic.
	 */
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
	
	/**
	 * Gets the URL for the queue with the specified name.
	 *
	 * @param name the name of the queue
	 * @return the queue url
	 */
	private static String getQueueURL(String name){
		for(String url : sqs.listQueues().getQueueUrls()){
			if(url.contains(name))
				return url;
		}
		
		return "";
	}
	
	/**
	 * Get a batch of messages from the queue with the specified name if they
	 * correspond to the supplied Video.
	 *
	 * @param video the Video for which messages should be returned
	 * @param queueName the queue to poll
	 * @return the list of messages
	 */
	public static List<String> pollQueue(Video video,String queueName){
		ArrayList<String> messages = new ArrayList<String>();
		
			for(Message message : sqs.receiveMessage(getQueueURL(queueName)).getMessages()){
				messages.add(message.toString());
				sqs.deleteMessage(new DeleteMessageRequest(getQueueURL(queueName),message.getReceiptHandle()));
		}
		
		return messages;
	}
	
	/**
	 * Poll the Progress queue.
	 *
	 * @param video the Video for which messages should be returned
	 * @return the list of messages
	 */
	public static List<String> getProgress(Video video){
		return pollQueue(video,progress);
	}
	
	/**
	 * Poll the Error queue.
	 *
	 * @param video the Video for which messages should be returned
	 * @return the list of messages
	 */
	public static List<String> getError(Video video){
		return pollQueue(video,error);
	}
	
	/**
	 * Poll the Complete queue.
	 *
	 * @param video the Video for which messages should be returned
	 * @return the list of messages
	 */
	public static List<String> getComplete(Video video){
		return pollQueue(video,complete);
	}
	
	/**
	 * Poll the Warning queue.
	 *
	 * @param video the Video for which messages should be returned
	 * @return the list of messages
	 */
	public static List<String> getWarning(Video video){
		return pollQueue(video,warning);
	}

	/**
	 * Clear all queues of all messages relating to the specified Video.
	 *
	 * @param video the video
	 */
	public static void clearQueues(Video video) {
		while(getProgress(video).size() > 0);
		while(getError(video).size() > 0);
		while(getComplete(video).size() > 0);
		while(getWarning(video).size() > 0);
	}
}
