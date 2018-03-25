package repository;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import model.Station;
import model.StationType;

@Primary
@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

	public List<Station> findByName(String name);
	
	@Query("SELECT s FROM Station s WHERE s.stationId = ?1")
	public List<Station> getByStationType(long id);
	
	List<Station> findByStationType(StationType stationtype);
		
}
