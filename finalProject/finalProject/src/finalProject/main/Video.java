package finalProject.main;

import java.io.File;

import javax.validation.constraints.NotNull;

import com.amazonaws.services.elastictranscoder.model.CreateJobResult;
import com.amazonaws.services.s3.model.PutObjectResult;

public class Video extends File{
	@NotNull
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
}
