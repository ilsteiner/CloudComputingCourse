package finalProject.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.amazonaws.services.elastictranscoder.model.Pipeline;

import finalProject.main.PipelineManager;

public class PipelineManagementTest {

	@Test
	public void CreatePipeline_SingleMethodCall_OnePipelineCreated() {
		assertNotNull(PipelineManager.createMainPipeline());
	}
	
	@Test
	public void CreatePipeLine_MultipleMethodCall_OnePipelineCreated() {
		PipelineManager.createMainPipeline();
		PipelineManager.createMainPipeline();
		PipelineManager.createMainPipeline();
		
		int pipelineCount = 0;
		
		for(Pipeline pipeline : PipelineManager.getPipelines()){
			if(pipeline.getName().equals(PipelineManager.name)){
				pipelineCount++;
			}
		}
		
		assertTrue(pipelineCount == 1);
	}
}
