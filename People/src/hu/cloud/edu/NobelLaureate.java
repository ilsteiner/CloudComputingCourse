package hu.cloud.edu;

import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;

public class NobelLaureate extends Person {
	protected int winYear;
	protected String field;
	
	public NobelLaureate(String firstName, String lastName, String movie, String imageURL, String resumeURL,
			int winYear, String field) {
		super(firstName, lastName, movie, imageURL, resumeURL);
		this.winYear = winYear;
		this.field = field;
	}
	
	public ReplaceableItem getReplaceableItem(){
		return new ReplaceableItem(getKey()).withAttributes(
	            new ReplaceableAttribute("First Name", firstName, true),
	            new ReplaceableAttribute("Last Name", lastName, true),
	            new ReplaceableAttribute("Popular Movie", movie, true),
	            new ReplaceableAttribute("Image", imageURL, true),
	            new ReplaceableAttribute("Resume", resumeURL, true),
	            new ReplaceableAttribute("Year Won", Integer.toString(winYear), true),
	            new ReplaceableAttribute("Field of Study", field, true));
	}
}
