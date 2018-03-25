package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Batch;
import model.Operation;
import model.Production;
import model.Station;
import model.Timing;
import repository.ProductionRepository;

@Service
public class ProductionService {

	@Autowired
	private ProductionRepository productionRepository;
	
	@Autowired
	private TimingService timingService;
	
	public List<Production> getAll(){
		List<Production> all = (List<Production>) productionRepository.findAll();
		return all;
	}
	
	public Production getById(long id){
		return productionRepository.findOne(id);
	}
	
	public List<Production> getByBatch(Batch batch, Operation operation){
		return productionRepository.getByBatch(batch, operation);
	}
	
	public List<Production> getByBatch(Batch batch){
		return productionRepository.getByBatch(batch);
	}
	
	public List<Timing> getTimingsForOrder(Batch batch){
		List<Timing> timings = new ArrayList<Timing> ();
		List<Production> byBatch = getByBatch(batch);
		for(Production actualProduction : byBatch){
			List<Timing> timingList = timingService.getById(actualProduction);
			timings.addAll(timingList);
		}
	
		return timings;
	}
	
	public List<Production> findByStation(Station station){
		return productionRepository.findByStanowisko(station);
	}
	
	public List<Timing> findTimingsByStation(Station station){
		
		List <Timing> timingList = new ArrayList<Timing>();
		List<Production> listOfProduction = findByStation(station);
		for(Production p : listOfProduction){
			List<Timing> timings = timingService.getById(p);
			for(Timing t : timings){
				t.setProduction(p);
				timingList.add(t);
			}
		}
		return timingList;
	}
	
	public List<Timing> findTimingsByStation(Station station, Date startDate){
//		List <Timing> timingList = new ArrayList<Timing>();
//		List<Production> stations = findByStation(station);
//		for(Production p : stations){
//			List<Timing> timings = timingService.getById(p, startDate);
//			for(Timing t : timings){
//				t.setProduction(p);
//				timingList.add(t);
//			}
//		}
		List <Timing> timingList = timingService.getByStartDateAndStation(station, startDate);
		return timingList;
	}
	
	public Production save(Production entity){
		return productionRepository.saveAndFlush(entity);
	}	
	
	public List<Production> save(List<Production> entity){
		return productionRepository.save(entity);
	}
	
	public void update(){
		
	}
	
	
}
