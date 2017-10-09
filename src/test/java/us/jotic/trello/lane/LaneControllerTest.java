package us.jotic.trello.lane;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.jotic.trello.project.Project;
import us.jotic.trello.project.ProjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LaneControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ProjectService projectService;
	
	@Test
	@WithMockUser
	public void createLaneBelongingToOneProject() throws Exception {
		// create a project
		Project project = new Project("Super duper Project", "Some description");
		Project p = projectService.addProject(project);
	
		// create a lane
		MvcResult result = mockMvc.perform(post("/api/projects/" + p.getUuid() + "/lanes")
					.contentType("application/json")
					.content("{\"name\":\"Testing\",\"description\":\"Testing, testing\"}"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		Lane laneFromJson = new ObjectMapper().readValue(jsonResponse, Lane.class);
	
		// assert returned lane object has everything it has to have
		assert(laneFromJson.getName().equals("Testing"));
		assert(laneFromJson.getDescription().equals("Testing, testing"));
		// assert relationship with project was established
		assert(laneFromJson.getProject().getUuid().equals(p.getUuid()));
	}

	@Test
	@WithMockUser
	public void assertProjectHasLanesAssociatedWithIt() throws Exception {
		// create a project
		Project project = new Project("Super duper Project", "Some description");
		Project p = projectService.addProject(project);
	
		// create a lane
		MvcResult resultLane = mockMvc.perform(post("/api/projects/" + p.getUuid() + "/lanes")
					.contentType("application/json")
					.content("{\"name\":\"Testing\",\"description\":\"Satisfying length constraint\"}"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		String laneResponse = resultLane.getResponse().getContentAsString();
		Lane lane = new ObjectMapper().readValue(laneResponse, Lane.class);
		
		MvcResult result = mockMvc.perform(get("/api/projects/" + p.getUuid()))
									.andExpect(status().is2xxSuccessful())
									.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		Project projectFromJson = new ObjectMapper().readValue(jsonResponse, Project.class);
	
		assert(projectFromJson.getLanes().size() == 1);
		assert(projectFromJson.getLanes().get(0).getUuid().equals(lane.getUuid()));
	}
	
	@Test
	@WithMockUser
	public void assertPostFailsWithValidationMessages() throws Exception {
		
		Project project = new Project("Super duper Project", "Some description");
		Project p = projectService.addProject(project);
		
		// create a lane
		MvcResult result = mockMvc.perform(post("/api/projects/" + p.getUuid() + "/lanes")
					.contentType("application/json")
					.content("{\"name\":\"Test\",\"description\":\"Test\"}"))
				.andExpect(status().is4xxClientError())
				.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		
		assert(jsonResponse.contains("{\"field\":\"name\",\"message\":\"size must be between 5 and 20\"}"));
		assert(jsonResponse.contains("{\"field\":\"description\",\"message\":\"size must be between 10 and 500\"}"));
		
	}
	
	@Test
	@WithMockUser
	public void assertPostFailsWithValidationMessagesIfFieldsAreMissing() throws Exception {
		
		Project project = new Project("Super duper Project", "Some description");
		Project p = projectService.addProject(project);
		
		// create a lane
		MvcResult result = mockMvc.perform(post("/api/projects/" + p.getUuid() + "/lanes")
					.contentType("application/json")
					.content("{\"name\":\"Test\"}"))
				.andExpect(status().is4xxClientError())
				.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		
		assert(jsonResponse.contains("{\"field\":\"name\",\"message\":\"size must be between 5 and 20\"}"));
		assert(jsonResponse.contains("{\"field\":\"description\",\"message\":\"may not be null\"}"));
		
	}
}
