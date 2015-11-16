package finalProject.main;

public enum BucketName {
	INPUT,OUTPUT,THUMBNAILS;
	
	@Override
	public String toString(){
		switch (this) {
		case INPUT:
			return "transcoder-input-isteiner".toLowerCase();
		case OUTPUT:
			return "transcoder-output-isteiner".toLowerCase();
		case THUMBNAILS:
			return "transcoder-thumbnails-isteiner".toLowerCase();
		default:
			return "";
		}
	}
}
