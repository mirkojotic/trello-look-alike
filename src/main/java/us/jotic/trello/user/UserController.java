package us.jotic.trello.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private CustomUserDetailsService userService;

	@RequestMapping(method = RequestMethod.POST, value = "/register")
	public User register(@RequestBody User user) {
		return userService.saveNewUser(user);
	}
}
