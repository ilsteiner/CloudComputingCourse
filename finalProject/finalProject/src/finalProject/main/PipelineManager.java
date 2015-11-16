package finalProject.main;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.regions.Region;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder;
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoderClient;
import com.amazonaws.services.elastictranscoder.model.CancelJobRequest;
import com.amazonaws.services.elastictranscoder.model.CancelJobResult;
import com.amazonaws.services.elastictranscoder.model.CreateJobRequest;
import com.amazonaws.services.elastictranscoder.model.CreateJobResult;
import com.amazonaws.services.elastictranscoder.model.CreatePipelineRequest;
import com.amazonaws.services.elastictranscoder.model.CreatePipelineResult;
import com.amazonaws.services.elastictranscoder.model.CreatePresetRequest;
import com.amazonaws.services.elastictranscoder.model.CreatePresetResult;
import com.amazonaws.services.elastictranscoder.model.DeletePipelineRequest;
import com.amazonaws.services.elastictranscoder.model.DeletePipelineResult;
import com.amazonaws.services.elastictranscoder.model.DeletePresetRequest;
import com.amazonaws.services.elastictranscoder.model.DeletePresetResult;
import com.amazonaws.services.elastictranscoder.model.ListJobsByPipelineRequest;
import com.amazonaws.services.elastictranscoder.model.ListJobsByPipelineResult;
import com.amazonaws.services.elastictranscoder.model.ListJobsByStatusRequest;
import com.amazonaws.services.elastictranscoder.model.ListJobsByStatusResult;
import com.amazonaws.services.elastictranscoder.model.ListPipelinesRequest;
import com.amazonaws.services.elastictranscoder.model.ListPipelinesResult;
import com.amazonaws.services.elastictranscoder.model.ListPresetsRequest;
import com.amazonaws.services.elastictranscoder.model.ListPresetsResult;
import com.amazonaws.services.elastictranscoder.model.Pipeline;
import com.amazonaws.services.elastictranscoder.model.PipelineOutputConfig;
import com.amazonaws.services.elastictranscoder.model.ReadJobRequest;
import com.amazonaws.services.elastictranscoder.model.ReadJobResult;
import com.amazonaws.services.elastictranscoder.model.ReadPipelineRequest;
import com.amazonaws.services.elastictranscoder.model.ReadPipelineResult;
import com.amazonaws.services.elastictranscoder.model.ReadPresetRequest;
import com.amazonaws.services.elastictranscoder.model.ReadPresetResult;
import com.amazonaws.services.elastictranscoder.model.TestRoleRequest;
import com.amazonaws.services.elastictranscoder.model.TestRoleResult;
import com.amazonaws.services.elastictranscoder.model.UpdatePipelineNotificationsRequest;
import com.amazonaws.services.elastictranscoder.model.UpdatePipelineNotificationsResult;
import com.amazonaws.services.elastictranscoder.model.UpdatePipelineRequest;
import com.amazonaws.services.elastictranscoder.model.UpdatePipelineResult;
import com.amazonaws.services.elastictranscoder.model.UpdatePipelineStatusRequest;
import com.amazonaws.services.elastictranscoder.model.UpdatePipelineStatusResult;

public class PipelineManager {

	public PipelineManager() {
		// TODO Auto-generated constructor stub
	}

	public static Pipeline createMainPipeline(){
		AmazonElasticTranscoderClient transcoder = new AmazonElasticTranscoderClient();
		
		CreatePipelineRequest request = new CreatePipelineRequest();
		request.setName("MainPipeline");
		request.setInputBucket(BucketName.INPUT.toString());
		request.setOutputBucket(BucketName.OUTPUT.toString());
		request.setRole("arn:aws:iam::101517482224:role/Elastic_Transcoder_Default_Role");
		//request.setThumbnailConfig(new PipelineOutputConfig().withBucket(BucketName.THUMBNAILS.toString()).withStorageClass("ReducedRedundancy"));
		
		CreatePipelineResult result = transcoder.createPipeline(request);
		
		return result.getPipeline();
	}
}
