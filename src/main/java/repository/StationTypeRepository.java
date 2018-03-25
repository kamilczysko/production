package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.StationType;

@Repository
public interface StationTypeRepository extends JpaRepository<StationType, Long>{

	@Override
	<S extends StationType> S save(S entity);	
}
