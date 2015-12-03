package finalProject.main;

import java.io.File;

import com.amazonaws.services.elastictranscoder.model.Pipeline;
import com.amazonaws.services.s3.model.PutObjectResult;

public class Application {
	public static void main(String[] args) {
		String filePath = args[0];
		
		Video video = new Video(filePath);
		
		setup();
		
		video.upload();
		
		String fileSize = BucketManager.readableFileSize(video.length());
		
		System.out.println(video.getName() + " (" + fileSize + ") " + "uploaded ");
		
		video.processToHTML5();
		
		while(true){
			if(video.getComplete().size() > 0){
				System.out.println("Processing complete!");
				video.deleteInputFile();
				video.clearQueue();
				System.out.println("New file saved: " + video.getOutput(filePath));
				break;
			}
			
			for(String warning : video.getWarning()){
				System.out.println(warning);
			}
			
			for(String error : video.getError()){
				System.out.println(error);
				break;
			}
			
			if(video.getProgress().size() > 0){
				System.out.println("Processing...");
			}
		}
	}
	
	private static void setup(){
		PipelineManager.createMainPipeline();
		BucketManager.createAllBuckets();
		NotificationManager.createAllTopics();
		NotificationManager.createAndSubscribe();
	}
}
