package finalProject.main;

import java.io.File;
import java.util.List;

import com.amazonaws.services.elastictranscoder.model.CreateJobResult;
import com.amazonaws.services.s3.model.PutObjectResult;

public class Video extends File{
	private static final long serialVersionUID = -3812260948436342366L;
	private long id;
	private String objectKey;
	
	public Video(String filePath) {
		super(filePath);
		id = BucketManager.getFileID(this.getName());
		objectKey = id + "_" + this.getName();
	}

	public long getId() {
		return id;
	}

	public String getObjectKey() {
		return objectKey;
	}
	
	public PutObjectResult upload(){
		return BucketManager.addInputFile(this);
	}
	
	public CreateJobResult processToGIF(){
		return PipelineManager.createJob(this,Preset.GIF);
	}
	
	public CreateJobResult processToHTML5(){
		return PipelineManager.createJob(this,Preset.HTML5);
	}
	
	public List<String> getProgress(){
		return NotificationManager.getProgress(this);
	}
	
	public List<String> getError(){
		return NotificationManager.getError(this);
	}
	
	public List<String> getComplete(){
		return NotificationManager.getComplete(this);
	}
	
	public List<String> getWarning(){
		return NotificationManager.getWarning(this);
	}
	
	public void clearQueue(){
		NotificationManager.clearQueue(this);
	}

	public String getOutput(String filepath) {
		File inputFile = new File(filepath);
		File outputFile = BucketManager.getOutput(this,inputFile);
		
		return outputFile.getAbsolutePath();
	}

	public void deleteInputFile() {
		BucketManager.deleteInputFile(this);		
	}
}
