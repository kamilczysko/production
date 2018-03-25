package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import model.Batch;
import model.Operation;
import model.Production;
import model.Station;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {

	@Query(value = "select p from Production p where p.batch = ?1 and p.operation = ?2")
	public List<Production> getByBatch(Batch batch, Operation operation);

	public List<Production> getByBatch(Batch batch);
	
	@Query(value = "select p from Production p where p.Stanowisko = ?1")
	public List<Production> findByStanowisko(Station stanowisko);

	@Override
	<S extends Production> S save(S entity);
	@Override
	<S extends Production> List<S> save(Iterable<S> arg0);
}
