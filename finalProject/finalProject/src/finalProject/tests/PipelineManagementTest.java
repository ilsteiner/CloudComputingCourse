package finalProject.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.amazonaws.services.elastictranscoder.model.ListPipelinesResult;
import com.amazonaws.services.elastictranscoder.model.Pipeline;

import finalProject.main.PipelineManager;

public class PipelineManagementTest {

	@Test
	public void CreatePipeline_PipelineCreated() {
		assertNotNull(PipelineManager.createMainPipeline());
	}
}
