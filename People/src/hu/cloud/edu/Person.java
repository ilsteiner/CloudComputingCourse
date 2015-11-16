package hu.cloud.edu;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;

public class Person {
	protected String firstName;
	protected String lastName;
	protected String movie;
	protected String imageURL;
	protected String resumeURL;
	
	public Person(String firstName, String lastName, String movie, String imageURL, String resumeURL) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.movie = movie;
		this.imageURL = imageURL;
		this.resumeURL = resumeURL;
	}
	
	public String getKey(){
		return firstName + "-" + lastName;
	}
	
	public ReplaceableItem getReplaceableItem(){
		return new ReplaceableItem(getKey()).withAttributes(
	            new ReplaceableAttribute("First Name", firstName, true),
	            new ReplaceableAttribute("Last Name", lastName, true),
	            new ReplaceableAttribute("Popular Movie", movie, true),
	            new ReplaceableAttribute("Image", imageURL, true),
	            new ReplaceableAttribute("Resume", resumeURL, true));
	}
	
	public Map<String, AttributeValue> getAsItem(){
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		
		item.put("name", new AttributeValue(getKey()));
		item.put("Popular Movie", new AttributeValue(movie));
		item.put("Image", new AttributeValue(imageURL));
		item.put("Resume", new AttributeValue(resumeURL));
		
		return item;
	}
	
}
