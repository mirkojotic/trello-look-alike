package us.jotic.trello.project;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {
	
    public static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
	
	@Autowired
	private ProjectService projectService;

	@RequestMapping(method = RequestMethod.GET, value = "/projects")
	public List<Project> getAllProjects() {
		return projectService.getAllProjects();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/projects/{uuid}")
	public Project getProject(@PathVariable String uuid) {
		return projectService.getProject(uuid);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/projects")
	public Project addProject(@RequestBody Project project) {
		logger.info("Creating project {}", project);
		return projectService.addProject(project);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/projects/{uuid}")
	public Project updateProject(@PathVariable String uuid, @RequestBody Project project) {
		logger.info("Update project {}", project);
		return projectService.updateProject(project);
	}
}
