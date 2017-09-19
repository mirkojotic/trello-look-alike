package us.jotic.trello.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void shouldDemonstrateHowToCreateANewUser() throws Exception {
		// create a project
		String pwd = "secret";
		User user = new User();
		user.setUsername("mirko_jotic");
		user.setPassword(passwordEncoder.encode(pwd));
		User savedUser = userRepository.save(user);
		assert(passwordEncoder.matches(pwd, savedUser.getPassword()));
	}
	
	@Test
	public void shouldCreateANewUserTroughAPI() throws Exception {
		MvcResult result = mockMvc.perform(post("/register")
					.contentType("application/json")
					.content("{\"username\":\"mirko\",\"password\":\"secret\"}"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		User user = new ObjectMapper().readValue(jsonResponse, User.class);
		assert(user.getUsername().equals("mirko"));
	}
	
	@Test
	public void createNewUserAndTryToLogIn() throws Exception {
		String un = "somePerson";
		String pw = "somePersonsPwd";
		MvcResult result = mockMvc.perform(post("/register")
					.contentType("application/json")
					.content("{\"username\":\""+ un +"\",\"password\":\""+ pw +"\"}"))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		User user = new ObjectMapper().readValue(jsonResponse, User.class);
		
		// perform actual login
		mockMvc.perform(post("/login")
					.contentType("application/x-www-form-urlencoded")
					.accept(MediaType.APPLICATION_JSON)
					.param("username", un)
					.param("password", pw))
					.andExpect(status().is2xxSuccessful());
		
	}
/*	
	@Test
	public void shouldFail_createTwoUsersWithSameUsername() throws Exception {
		String un = "repeat";
		String pw = "secret";
		mockMvc.perform(post("/register")
					.contentType("application/json")
					.content("{\"username\":\""+ un +"\",\"password\":\""+ pw +"\"}"))
				.andExpect(status().is2xxSuccessful());
		MvcResult result = mockMvc.perform(post("/register")
					.contentType("application/json")
					.content("{\"username\":\""+ un +"\",\"password\":\""+ pw +"\"}"))
				.andExpect(status().is4xxClientError())
				.andReturn();
		System.out.println("*******************************");
		System.out.println(result.getResponse().getContentAsString());
		
	}
*/
}

