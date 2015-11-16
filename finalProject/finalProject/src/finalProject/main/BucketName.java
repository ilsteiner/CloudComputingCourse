package finalProject.main;

public enum BucketName {
	INPUT,OUTPUT;
	
	@Override
	public String toString(){
		switch (this) {
		case INPUT:
			return "transcoderinput".toLowerCase();
		case OUTPUT:
			return "transcoderoutput".toLowerCase();
		default:
			return "";
		}
	}
}
