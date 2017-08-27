package us.jotic.trello.lane;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface LaneRepository extends CrudRepository<Lane, String>{

	public List<Lane> findByProjectUuid(String projectId);
}
