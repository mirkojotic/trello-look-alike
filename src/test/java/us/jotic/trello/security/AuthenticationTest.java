package us.jotic.trello.security;


import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testShouldReturn401_IfCredentialsBad() throws Exception {
		mockMvc.perform(post("/login")
					.contentType("application/x-www-form-urlencoded")
					.accept(MediaType.APPLICATION_JSON)
					.param("username", "Mirko")
					.param("password", "Password"))
				.andExpect(status().is4xxClientError())
				.andExpect(status().reason(containsString("Bad credentials")));
	}
	
	@Test
	public void testShouldReturn200_IfCredentialsRight() throws Exception {
		mockMvc.perform(post("/login")
					.contentType("application/x-www-form-urlencoded")
					.param("username", "root")
					.param("password", "root"))
				.andExpect(status().is2xxSuccessful());
	}
	
}
