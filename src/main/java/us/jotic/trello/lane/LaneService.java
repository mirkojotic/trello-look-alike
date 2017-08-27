package us.jotic.trello.lane;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaneService {

	@Autowired
	private LaneRepository laneRepository;
	
	public List<Lane> getAll(String projectUuid) {
		return (List<Lane>) laneRepository.findByProjectUuid(projectUuid);
	}
	
	public Lane get(String uuid) {
		return laneRepository.findOne(uuid);
	}
	
	public Lane create(Lane lane) {
		return laneRepository.save(lane);
	}
	
	public Lane update(Lane lane) {
		return laneRepository.save(lane);
	}
}
