package us.jotic.trello.lane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import us.jotic.trello.project.Project;
import us.jotic.trello.project.ProjectService;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/projects/{projectUuid}/lanes")
public class LaneController {

    public static final Logger logger = LoggerFactory.getLogger(LaneController.class);
    
	@Autowired
	private LaneService laneService;
	
	@Autowired
	private ProjectService projectService;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Lane> getAllLanes(@PathVariable String projectUuid) {
		return laneService.getAll(projectUuid);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Lane saveLane(@PathVariable String projectUuid, @Valid @RequestBody Lane lane) {
		Project project = projectService.getProject(projectUuid);
		lane.setProject(project);
		return laneService.create(lane);
	}
}
