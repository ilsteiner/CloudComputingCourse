package finalProject.main;

import java.io.File;

import com.amazonaws.services.elastictranscoder.model.Pipeline;
import com.amazonaws.services.s3.model.PutObjectResult;

public class Application {
	public static void main(String[] args) {
		String filePath = args[0];
		
		Video video = new Video(filePath);
		
		setup();
		
		PutObjectResult result = video.upload();
		
		String fileSize = BucketManager.readableFileSize(video.length());
		
		System.out.println(video.getName() + " (" + fileSize + ") " + "uploaded ");
		
		video.processToGIF();
		
		System.out.println(video.getName() + " processing...");
	}
	
	private static void setup(){
		PipelineManager.createMainPipeline();
		BucketManager.createAllBuckets();
		NotificationManager.createAllTopics();
		NotificationManager.createAndSubscribe();
	}
}
