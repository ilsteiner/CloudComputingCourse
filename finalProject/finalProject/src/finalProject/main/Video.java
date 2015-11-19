package finalProject.main;

import java.io.File;

import javax.validation.constraints.NotNull;

public class Video {
	@NotNull
	File videoFile;
	String fileName;
	
	public Video(File videoFile) {
		this.videoFile = videoFile;
		fileName = videoFile.getName();
	}
	
}
