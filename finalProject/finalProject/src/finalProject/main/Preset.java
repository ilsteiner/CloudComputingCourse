package finalProject.main;

public enum Preset {
GIF,HTML5;
	
	public String getId(){
		switch (this) {
		case GIF:
			return "1351620000001-100200";
		case HTML5:
			return "1351620000001-100200";
		default:
			return "";
		}
	}
}
