package repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import model.Operation;
import model.Process;

@Repository
public interface OperationRepository extends CrudRepository<Operation, Long>{
	
	
	public List<Operation> findByProces(Process p);
}
