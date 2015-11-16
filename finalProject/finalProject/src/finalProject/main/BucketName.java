package finalProject.main;

public enum BucketName {
	INPUT,OUTPUT;
	
	@Override
	public String toString(){
		switch (this) {
		case INPUT:
			return "transcoder-input-isteiner".toLowerCase();
		case OUTPUT:
			return "transcoder-output-isteiner".toLowerCase();
		default:
			return "";
		}
	}
}
