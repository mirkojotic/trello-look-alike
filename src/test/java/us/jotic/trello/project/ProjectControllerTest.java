package us.jotic.trello.project;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ProjectService projectService;
	
	@Test
	@WithMockUser
	public void testShouldAddProject() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/api/projects")
					.contentType("application/json")
					.content("{\"name\":\"Testing\",\"description\":\"My supper cool fancy project\"}"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		assert(jsonResponse.contains("\"name\":\"Testing\",\"description\":\"My supper cool fancy project\""));
	}
	
	@Test
	@WithMockUser
	public void testFailWithValidationMessages() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/api/projects")
					.contentType("application/json")
					.content("{\"name\":\"Test\",\"description\":\"Testing\"}"))
				.andExpect(status().is4xxClientError())
				.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		
		assert(jsonResponse.contains("{\"field\":\"name\",\"message\":\"size must be between 5 and 20\"}"));
		assert(jsonResponse.contains("{\"field\":\"description\",\"message\":\"size must be between 10 and 500\"}"));
	}
	
	@Test
	@WithMockUser
	public void testFailWithValidationMessagesIfRequiredFieldsAreNotPresent() throws Exception {
		
		MvcResult result = mockMvc.perform(post("/api/projects")
					.contentType("application/json")
					.content("{}"))
				.andExpect(status().is4xxClientError())
				.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		
		assert(jsonResponse.contains("{\"field\":\"name\",\"message\":\"may not be null\"}"));
		assert(jsonResponse.contains("{\"field\":\"description\",\"message\":\"may not be null\"}"));
	}
	
	@Test
	@WithMockUser
	public void testShouldGetProject() throws Exception {
		Project project = new Project("Super duper Project", "Some description");
		Project p = projectService.addProject(project);
		
		MvcResult result = mockMvc.perform(get("/api/projects/" + p.getUuid()))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		String jsonResponse = result.getResponse().getContentAsString();
		Project projectFromJson = new ObjectMapper().readValue(jsonResponse, Project.class);
	
		assert(projectFromJson.getUuid().equals(p.getUuid()));
		assert(projectFromJson.getName().equals("Super duper Project"));
		assert(projectFromJson.getDescription().equals("Some description"));
	}
	
	@Test
	@WithMockUser
	public void testShouldUpdateProject() throws Exception {
		Project project = new Project("Testing", "Test description");
		Project p = projectService.addProject(project);
		
		String body = "{\"uuid\":\"" + p.getUuid() + "\",\"name\":\"Updated testing\",\"description\":\"Updated Description\"}";
		
		MvcResult result = mockMvc.perform(put("/api/projects/" + p.getUuid()).contentType("application/json").content(body))
									.andExpect(status().is2xxSuccessful())
									.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		Project projectFromJson = new ObjectMapper().readValue(jsonResponse, Project.class);
		
		// assert JSON response is of updated model
		assert(projectFromJson.getName().equals("Updated testing"));
		assert(projectFromJson.getDescription().equals("Updated Description"));
		
		Project found = projectService.getProject(p.getUuid());
		
		//assert model was actually changed
		assert(found.getName().equals("Updated testing"));
		assert(found.getDescription().equals("Updated Description"));
	}
}
