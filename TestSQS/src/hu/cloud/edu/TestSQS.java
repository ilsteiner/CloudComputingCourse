/*
 * Copyright 2010-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package hu.cloud.edu;
import java.util.List;
import java.util.Map.Entry;
import javax.net.ssl.SSLException;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;

public class TestSQS {
	public static final String queueName = "TestQueue";
	public static String myQueueUrl;
	public static AmazonSQSClient sqs;
	public static final int sleepTime = 30000;

    public static void main(String[] args) throws Exception {

        sqs = new AmazonSQSClient();
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
            
            // Send a message
            System.out.println("Sending the first message to " + queueName);
            sqs.sendMessage(new SendMessageRequest(myQueueUrl, "This is my first message"));
            
            // Send another message
            System.out.println("Sending the second message to " + queueName);
            sqs.sendMessage(new SendMessageRequest(myQueueUrl, "This is my second message."));
            
            for(int i=0;i<5;i++){
            	int messageNum = i + 3;
            	System.out.println("Sending the message " + messageNum + " to " + queueName);
            	sqs.sendMessage(new SendMessageRequest(myQueueUrl, "This is message number " + messageNum));
            }
            
            printMessages(true);

            List<Message> messages = printMessages(true);

            // Delete a message
            System.out.println("Deleting a message.\n");
            String messageRecieptHandle = messages.get(0).getReceiptHandle();
            sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageRecieptHandle));
            
            printMessages(true);
            
          //Sleep for a bit to allow it to come back
           Thread.sleep(sleepTime);
           
           printMessages(true);
            
            System.out.println(sqs.getQueueAttributes(new GetQueueAttributesRequest(myQueueUrl).withAttributeNames("All")));
            
            System.out.println(sqs.getQueueAttributes(new GetQueueAttributesRequest(myQueueUrl).withAttributeNames("ApproximateNumberOfMessages")));

            // Delete a queue
            System.out.println("Deleting the test queue.\n");
            sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
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
    
    public static List<Message> printMessages(Boolean printCount){
    	if(printCount){
    		printMessageCount();
    	}
    	
    	return printMessages();
    }
    
    public static void printMessageCount(){
        System.out.println("Number of messages in queue " + myQueueUrl + " " +
          getMessageCount());
    }
    
    public static int getMessageCount(){
    	// Fetch Queue attribute ApproximateNumberOfMessages
        GetQueueAttributesRequest getQARequest = 
             new GetQueueAttributesRequest().withQueueUrl(myQueueUrl);
        GetQueueAttributesResult getQAResult =  sqs.getQueueAttributes(
        		  getQARequest.withAttributeNames("ApproximateNumberOfMessages"));        
        Map <String,String> attributeMap = getQAResult.getAttributes();
        
        return Integer.parseInt(attributeMap.get("ApproximateNumberOfMessages"));
    }
}
