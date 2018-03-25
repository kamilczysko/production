package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import model.Station;
import model.StationType;
import repository.StationRepository;

@Service
public class StationService {

	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private JdbcTemplate jdbc;

	public List<Station> getAll() {
		return (List<Station>) stationRepository.findAll();
	}

	public Station getById(long id) {
		return stationRepository.findOne(id);
	}
	
	public List<Station> getByName(String name){
		return (List<Station>) stationRepository.findByName(name);
	}
	
	public List<Station> getByType(StationType stationType){
		return stationRepository.findByStationType(stationType);
	}
	
	public Station saveStation(Station station){
		return stationRepository.save(station);
	}
	
	public void removeFromStationMark(long stationId){
		String query = "delete from oznaczenieTypuStanowiska where Stanowisko = "+stationId;
		jdbc.update(query);
	}
	
	public boolean deleteStation(Station station){
		try {
			stationRepository.delete(station);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
