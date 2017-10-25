package us.jotic.trello.lane;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.jotic.trello.project.Project;

@Service
public class LaneService {

	@Autowired
	private LaneRepository laneRepository;
	
	public List<Lane> getAll(String projectUuid) {
		return (List<Lane>) laneRepository.findByProjectUuidOrderByPositionAsc(projectUuid);
	}
	
	public Lane reorderLanes(String uuid, Lane lane) {
		
		List<Lane> lanes = getAll(uuid);
		
		boolean reorderAllFollowing = false;
		
		for(Lane storedLane : lanes) {
			// when we get to the position we want to set the lane to and preasumably that lane is not the
			// the lane we got from the front-end we update its position and make 'reorderAllFollowing' flag
			// to true
			if(storedLane.getPosition() == lane.getPosition() && storedLane.getUuid() != lane.getUuid()) {
				update(lane);
				reorderAllFollowing = true;
			}
			// this block will offset all lanes (after moved lane) other lanes by one 
			if(reorderAllFollowing) {
				Integer oldPosition = storedLane.getPosition();
				storedLane.setPosition(oldPosition + 1);
				update(storedLane);
			}
			
		}
		
		return update(lane);
	}
	
	public Lane get(String uuid) {
		return laneRepository.findOne(uuid);
	}
	
	public Lane createNewLane(Project project, Lane lane) {
		lane.setProject(project);
		Integer totalLanes = count(project.getUuid()).intValue();
		// set position of lanes - left to right ordering
		lane.setPosition(totalLanes);
		return create(lane);
	}
	
	public Lane create(Lane lane) {
		return laneRepository.save(lane);
	}
	
	public Lane update(Lane lane) {
		return laneRepository.save(lane);
	}
	
	public Long count(String projectUuid) {
		return laneRepository.countLanesByProjectUuid(projectUuid);
	}
}
