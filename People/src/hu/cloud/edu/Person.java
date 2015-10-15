package hu.cloud.edu;

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
	
}
