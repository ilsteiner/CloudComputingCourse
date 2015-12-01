package finalProject.main;

import java.io.File;

import com.amazonaws.services.s3.model.PutObjectResult;

public class Application {
	public static void main(String[] args) {
		String filePath = args[0];
		
		Video video = new Video(filePath);
		
		PutObjectResult result = video.upload();
		
		String fileSize = BucketManager.readableFileSize(video.length());
		
		System.out.println(video.getName() + " (" + fileSize + ") " + "uploaded ");
	}
}
