package us.jotic.trello.project;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import us.jotic.trello.project.Project;
import us.jotic.trello.project.ProjectRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectRepositoryTest {
	
	@Autowired
	private TestEntityManager em;
	
	@Autowired
	private ProjectRepository projectRepository;

	@Test
	public void createUserAndReturnWithUuidAsPK() {
		
		Project project = new Project("Super duper Project", "Some description");
		
		em.persistAndFlush(project);
		
		Project found = projectRepository.findOne(project.getUuid());
		
		assertThat(found.getUuid()).isEqualTo(project.getUuid());
	}
}
