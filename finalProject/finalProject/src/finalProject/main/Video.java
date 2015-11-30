package finalProject.main;

import java.io.File;

import javax.validation.constraints.NotNull;

public class Video {
	@NotNull
	private File videoFile;
	private String fileName;
	
	public Video(File videoFile) {
		this.videoFile = videoFile;
		fileName = videoFile.getName();
	}

	public File getVideoFile() {
		return videoFile;
	}

	public String getFileName() {
		return fileName;
	}
}
