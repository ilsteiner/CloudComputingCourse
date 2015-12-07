package finalProject.main;

/**
 * Valid preset names and their AWS ids.
 */
public enum Preset {

	GIF,HTML5;
	
	/**
	 * Gets the AWS preset id.
	 *
	 * @return the id of the preset
	 */
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
