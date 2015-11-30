package finalProject.main;

import java.io.File;

public class Application {
	public static void main(String[] args) {
		String filePath = args[0];
		
		File videoFile = new File(filePath);
		
		Video video = new Video(videoFile);
		
		System.out.println(video.getFileName() + " imported");
	}
}
