package hu.cloud.edu;

public class NobelLaureate extends Person {
	protected int winYear;
	protected String field;
	
	public NobelLaureate(String firstName, String lastName, String movie, String imageURL, String resumeURL,
			int winYear, String field) {
		super(firstName, lastName, movie, imageURL, resumeURL);
		this.winYear = winYear;
		this.field = field;
	}
}
