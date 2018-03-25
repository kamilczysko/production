package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Batch;
import repository.BatchRepository;

@Service
public class BatchService {

	@Autowired
	private BatchRepository batchRepository;
	
	public List<Batch> getAll(){
		return (List<Batch>) batchRepository.findAll();
	}
	
	public List<Batch> getByOrder(int id){
		return batchRepository.findByZlecenie(id);
	}
		
//	public void update(Batch batch){
//		batchRepository.update(batch.getIlosc(), batch.getOpis(), batch.getIDPartia());
//	}
	
	public Batch save(Batch batch){
		return batchRepository.save(batch);
	}
	
	public List<Batch> save(List<Batch> batchList){
		return batchRepository.save(batchList);
	}
	
}
