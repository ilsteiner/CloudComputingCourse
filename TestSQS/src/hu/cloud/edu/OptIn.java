package hu.cloud.edu;

import java.util.List;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.ConfirmSubscriptionResult;
import com.amazonaws.services.sns.model.ConfirmSubscriptionRequest;
import com.amazonaws.services.sns.model.ListSubscriptionsResult;
public class OptIn {
    public static void main(String[] args) throws Exception {
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("ZoranJavaDSDK").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials",    e);
        }
        AmazonSNS sns = new AmazonSNSClient(credentials);
		
        System.out.println("===========================================");
        System.out.println("Accepting subscription, Opting-In");
        System.out.println("===========================================\n");
      try {
         String topicArn = "arn:aws:sns:us-east-1:951414139794:MyTopic11";
         String token = "2336412f37fb687f5d51e6e241d7700bdeeb338ec5a715807fdc9682f3a4ddfbd12e3dda5f159fa005f752a9c6772a22c225c0c1d68d75efbc51d75243c2659f4e46fb8b4556573ee1f1801b4aeb00b523308331b8f1b526a8cbd93925b241af9708d412fa4fc94f65db5f3ecbed2de5";               
          ConfirmSubscriptionResult conSubRes = sns.confirmSubscription(
            		new ConfirmSubscriptionRequest(topicArn, token));    
          String subscribedTopicArn = conSubRes.getSubscriptionArn();        
          // List Topic subscriptions
          ListSubscriptionsResult listSubResult = sns.listSubscriptions();
          List<Subscription> subscriptions = listSubResult.getSubscriptions();          
	       for (Subscription sub : subscriptions) {
	           System.out.println(sub.getEndpoint() + " " + sub.getOwner() + 
                " " + sub.getTopicArn());
		  }
      }
         catch(AmazonServiceException ase) { }
         catch(AmazonClientException ace) { }
   }
}
