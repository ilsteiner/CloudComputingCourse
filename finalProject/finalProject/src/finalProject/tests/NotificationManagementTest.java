package finalProject.tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.amazonaws.services.sns.model.Topic;

import finalProject.main.NotificationManager;

public class NotificationManagementTest {
	
	@SuppressWarnings("serial")
	@Test
	public void CreateTopics_TopicsCreated(){
		NotificationManager.createAllTopics();
		
		HashMap<String,Boolean> topicsExist = new HashMap<String,Boolean>(){{
			put("PipelineComplete",false);
			put("PipelineError",false);
			put("PipelineProgress",false);
			put("PipelineWarning",false);
		}};
		
		for(Topic topic : NotificationManager.listTopics()){
			for(String name : topicsExist.keySet()){
				if(topic.getTopicArn().contains(name)){
					topicsExist.put(name, true);
				}
			}
		}
		
		for(String name : topicsExist.keySet()){
			assertTrue(name + " topic should exist",topicsExist.get(name));
		}
	}
}
