package repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Batch;

@Repository
@Qualifier("batchRepository")
public interface BatchRepository extends JpaRepository<Batch, Long>{
	
	public List<Batch> findByZlecenie(int id);

	@Override
	<S extends Batch> List<S> save(Iterable<S> arg0);
	@Override
	<S extends Batch> S save(S entity);
}
