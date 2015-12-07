package finalProject.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amazonaws.services.elastictranscoder.model.CreateJobResult;
import com.amazonaws.services.s3.model.PutObjectResult;

/**
 * Extension of the File object o store data about videos to be transcoded.
 */
@SuppressWarnings("serial")
public class Video extends File {	
	private long id;
	
	/** Unique key that will be the file name on S3. */
	private String objectKey;

	/**
	 * Instantiates a new Video from the specified filepath.
	 *
	 * @param filepath the filepath to the video file
	 */
	public Video(String filepath) {
		super(filepath);
		id = BucketManager.getFileID(this.getName());
		objectKey = id + "_" + this.getName();
	}

	public long getId() {
		return id;
	}

	public String getObjectKey() {
		return objectKey;
	}

	/**
	 * Upload the video to the Input bucket.
	 *
	 * @return the result of uploading the Video, as returned by the S3 API
	 * @throws IOException File could not be found or could not be opened.
	 */
	public PutObjectResult upload() throws IOException {
		return BucketManager.addInputFile(this);
	}

	/**
	 * Transcode file to animated GIF.
	 *
	 * @return the the job result
	 */
	public CreateJobResult processToGIF() {
		return PipelineManager.createJob(this, Preset.GIF);
	}

	/**
	 * Transcode file to HTML5 (WebM).
	 *
	 * @return the job result
	 */
	public CreateJobResult processToHTML5() {
		return PipelineManager.createJob(this, Preset.HTML5);
	}

	/**
	 * Gets messages from the Progress queue.
	 *
	 * @return the messages
	 */
	public List<String> getProgress() {
		return NotificationManager.getProgress(this);
	}

	/**
	 * Gets messages from the Error queue.
	 *
	 * @return the messages
	 */
	public List<String> getError() {
		return NotificationManager.getError(this);
	}

	/**
	 * Gets messages from the Complete queue.
	 *
	 * @return the messages
	 */
	public List<String> getComplete() {
		return NotificationManager.getComplete(this);
	}

	/**
	 * Gets messages from the Warning queue.
	 *
	 * @return the messages
	 */
	public List<String> getWarning() {
		return NotificationManager.getWarning(this);
	}

	/**
	 * Clear all queues of all messages relating to this Video.
	 */
	public void clearQueues() {
		NotificationManager.clearQueues(this);
	}

	/**
	 * Delete input file uploaded for this Video to S3.
	 */
	public void deleteInputFile() {
		BucketManager.deleteInputFile(this);
	}
	
	/**
	 * Gets the output URL for the transcoded file created for this Video.
	 *
	 * @return the output url for the Video
	 */
	public String getOutputURL(){
		return BucketManager.getOutputURL(this);
	}
}
