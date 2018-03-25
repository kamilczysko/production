package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Process;
import repository.ProcessRepository;

@Service
public class ProcessService {

	@Autowired
	private ProcessRepository processRepository;
	
	public List<Process> getAll(){
		return (List<Process>) processRepository.findAll();
	}
	
	public Process getById(long id){
		return processRepository.findOne(id);
	}
	
	public List<Process> getByArt(int art){
		return processRepository.findByArticle(art);
	}
	
	public List<Process> getByName(String name){
		return processRepository.findByName(name);
	}
	
	public List<Process> getProcessForOrder(long order){
		return processRepository.findOperationForOrder(order);
	}
	
	public Process saveProcess(Process process){
		return processRepository.save(process);
	}
	
}
