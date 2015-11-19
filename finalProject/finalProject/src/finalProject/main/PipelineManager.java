package finalProject.main;

import java.util.List;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClient;
import com.amazonaws.services.elastictranscoder.model.CreatePipelineRequest;
import com.amazonaws.services.elastictranscoder.model.CreatePipelineResult;
import com.amazonaws.services.elastictranscoder.model.Pipeline;

public class PipelineManager {
	public static final String name = "MainPipeline";
	public static final String role = "arn:aws:iam::101517482224:role/Elastic_Transcoder_Default_Role";

	public PipelineManager() {
		// TODO Auto-generated constructor stub
	}

	public static Pipeline createMainPipeline(){
		AmazonElasticTranscoderClient transcoder = new AmazonElasticTranscoderClient(new ProfileCredentialsProvider("finalProject"));
	
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
			
			//Can't figure out how to properly set the thumbnail bucket
			//request.setThumbnailConfig(new PipelineOutputConfig().withBucket(BucketName.THUMBNAILS.toString()).withStorageClass("ReducedRedundancy"));		
			CreatePipelineResult result = transcoder.createPipeline(request);
			
			returnPipeline = result.getPipeline();
		}
		
		return returnPipeline;
	}
	
	public static List<Pipeline> getPipelines(){
		AmazonElasticTranscoderClient transcoder = new AmazonElasticTranscoderClient(new ProfileCredentialsProvider("finalProject"));
		
		return transcoder.listPipelines().getPipelines();
	}
}
