package finalProject.main;

import java.util.List;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClient;
import com.amazonaws.services.elastictranscoder.model.CreateJobOutput;
import com.amazonaws.services.elastictranscoder.model.CreateJobRequest;
import com.amazonaws.services.elastictranscoder.model.CreateJobResult;
import com.amazonaws.services.elastictranscoder.model.CreatePipelineRequest;
import com.amazonaws.services.elastictranscoder.model.CreatePipelineResult;
import com.amazonaws.services.elastictranscoder.model.JobInput;
import com.amazonaws.services.elastictranscoder.model.Pipeline;

/**
 * Methods to create and manage the transcoder pipeline.
 */
public class PipelineManager {
	
	/** The name of the pipeline. */
	public static final String name = "MainPipeline";
	
	/** The role the transcoder client will operate under. */
	public static final String role = "arn:aws:iam::101517482224:role/Elastic_Transcoder_Default_Role";
	
	/** The username to use when accessing AWS resources **/
	public static final String username = "finalProject";
	
	/** The transcoder client. */
	public static final AmazonElasticTranscoderClient transcoder = new AmazonElasticTranscoderClient(new ProfileCredentialsProvider(username));

	/**
	 * Creates the main pipeline.
	 *
	 * @return the pipeline
	 */
	public static Pipeline createMainPipeline(){	
		Pipeline returnPipeline = null;
		
		for(Pipeline pipeline : transcoder.listPipelines().getPipelines()){
			if(pipeline.getName().equals(name)){
				returnPipeline = pipeline;
			}
		}
		
		if(returnPipeline == null){			
			CreatePipelineRequest request = new CreatePipelineRequest();
			request.setName(name);
			request.setInputBucket(BucketName.INPUT.toString());
			request.setOutputBucket(BucketName.OUTPUT.toString());
			request.setRole(role);
			request.setNotifications(NotificationManager.getNotifications());
			
			//Can't figure out how to properly set the thumbnail bucket
			//request.setThumbnailConfig(new PipelineOutputConfig().withBucket(BucketName.THUMBNAILS.toString()).withStorageClass("ReducedRedundancy"));		
			CreatePipelineResult result = transcoder.createPipeline(request);
			
			returnPipeline = result.getPipeline();
		}
		
		return returnPipeline;
	}
	
	/**
	 * Gets the all pipelines.
	 *
	 * @return the pipelines
	 */
	public static List<Pipeline> getPipelines(){		
		return transcoder.listPipelines().getPipelines();
	}
	
	/**
	 * Creates a job to transcode the specified Video.
	 *
	 * @param video the Video to transcode
	 * @param preset an enum representing a valid transcoding preset
	 * @return the job result
	 */
	public static CreateJobResult createJob(Video video,Preset preset){
		JobInput input = new JobInput().withKey(video.getObjectKey());
		CreateJobOutput output = new CreateJobOutput().withKey(video.getObjectKey()).withPresetId(preset.getId());
		CreateJobRequest request = new CreateJobRequest().withInput(input).withPipelineId(createMainPipeline().getId()).withOutput(output);
		
		return transcoder.createJob(request);
	}
}
