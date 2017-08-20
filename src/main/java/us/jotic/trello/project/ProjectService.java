package us.jotic.trello.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	public List<Project> getAllProjects() {
		return (List<Project>) projectRepository.findAll();
	}

	public Project getProject(String uuid) {
		return projectRepository.findOne(uuid);
	}

	public Project addProject(Project project) {
		return projectRepository.save(project);
	}

	public Project updateProject(Project project) {
		return projectRepository.save(project);
	}
}
