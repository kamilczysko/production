package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import model.Station;
import model.StationType;
import repository.StationTypeRepository;

@Service
public class StationTypeService {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private StationTypeRepository stationTypeRepository;
	
	public List<StationType> getStationTypes() {
		return (List<StationType>) stationTypeRepository.findAll();
	}

	public StationType getById(long id) {
		return stationTypeRepository.findOne(id);
	}

	public List<Station> getStationsFromType(long id) {
		return stationTypeRepository.findOne(id).getStation();
	}
	
	public StationType saveType(StationType newType){
		return stationTypeRepository.save(newType);
	}
	
	public void removeFromStationMark(long typeId){
		String query = "delete from oznaczenieTypuStanowiska where TypStanowiska = "+typeId;
		jdbc.update(query);
	}
	
	public boolean deleteType(List<StationType> types) {
		try {
			stationTypeRepository.delete(types);

		} catch (Exception e) {
			System.out.println("Nie można usunac typu.");
			return false;
		}
		return true;
	}

}
