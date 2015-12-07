package finalProject.main;

import java.io.IOException;

/**
 * CLI interface for the project.
 */
public class Transcoder {
	
	/**
	 * This is a CLI interface for this project.
	 *
	 * @param filePath a String representing a filepath to a valid video file
	 * @param format a String (GIF,HTML5) representing the format to which 
	 * 		  the video should be transcoded
	 */
	public static void main(String[] args) {
		String filePath = args[0];
		String transcodeFormat = null;
		
		if(args.length < 2 || args[1].length() == 0 || args[1].equals(Preset.HTML5.toString())){
			transcodeFormat = Preset.HTML5.toString();
		}
		else if(args[1].equals(Preset.GIF.toString())){
			transcodeFormat = Preset.GIF.toString();
		}
		else{
			System.out.println("Invalid transcode format, valid formats are: " + Preset.HTML5.toString() + "," + Preset.GIF.toString());
			System.out.println("Program terminated");
			return;
		}
		
		if(!transcodeFormat.equals(null)){
			Video video = new Video(filePath);
			
			setup();
			
			try {
				if(!video.isVideo()){
					System.out.println("Invalid file");
					System.out.println("Program terminated");
					return;
				}
				video.upload();
			} catch (IOException e) {
				System.out.println("Video file not found");
				return;
			}
			
			String fileSize = BucketManager.readableFileSize(video.length());
			
			System.out.println(video.getName() + " (" + fileSize + ") " + "uploaded ");
			
			System.out.println("Transcoding " + video.getName() + " to " + transcodeFormat);
			
			if(transcodeFormat.equals(Preset.HTML5.toString())){
				video.processToHTML5();
			}
			else{
				video.processToGIF();
			}
			
			while(true){
				if(video.getComplete().size() > 0){
					System.out.println("Processing complete!");
					video.deleteInputFile();
					video.clearQueues();
					System.out.println("New file available: " + video.getOutputURL());
					System.out.println("Complete!");
					break;
				}
				
				if(video.getError().size() > 0){
					System.out.println("Error while transcoding. Program terminated.");
					video.deleteInputFile();
					video.clearQueues();
					break;
				}
				
				for(String warning : video.getWarning()){
					System.out.println(warning);
				}
				
				if(video.getProgress().size() > 0){
					System.out.println("Processing...");
				}
			}
		}
	}
	
	/**
	 * Setup to run the application.
	 */
	private static void setup(){
		PipelineManager.createMainPipeline();
		BucketManager.createAllBuckets();
		NotificationManager.createAllTopics();
		NotificationManager.createAndSubscribe();
	}
}
