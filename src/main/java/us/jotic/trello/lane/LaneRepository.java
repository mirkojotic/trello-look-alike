package us.jotic.trello.lane;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

//public interface LaneRepository extends PagingAndSortingRepository<Lane, String>{
public interface LaneRepository extends CrudRepository<Lane, String>{

	public List<Lane> findByProjectUuid(String projectId);
	
	public List<Lane> findByProjectUuidOrderByPositionAsc(String projectId);

	@Query("SELECT COUNT(l) FROM Lane l WHERE l.project.uuid = ?1")
	public Long countLanesByProjectUuid(String projectId);
}
