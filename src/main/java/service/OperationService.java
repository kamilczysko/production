package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Operation;
import model.Process;
import repository.OperationRepository;

@Service
public class OperationService {

	@Autowired
	private OperationRepository operationRepository;
	
	public List<Operation> getAll(){
		return (List<Operation>) operationRepository.findAll();
	}
	
	public Operation getById(long id){
		return operationRepository.findOne(id);
	}
	
	public List<Operation> getOperationForProcess(Process p){
		return operationRepository.findByProces(p);
	}
	
	public List<Operation> save(List<Operation>operations){
		return (List<Operation>) operationRepository.save(operations);
	}
	
	public Operation save(Operation operation){
		return operationRepository.save(operation);
	}
	
}
