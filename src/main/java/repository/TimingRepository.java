package repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import model.Timing;

@Repository
public interface TimingRepository extends JpaRepository<Timing, Long>{
	
	@Query(value = "select t from Timing t where t.id = :idProd ORDER by t.Start ASC")
	public List<Timing> findByProdId(@Param("idProd")long production);
	
	@Query(value = "select t from Timing t where t.id = :idProd and t.Start > :startDate ORDER by t.Start ASC")
	public List<Timing> findById(@Param("idProd")long production, @Param("startDate")Date date);
	
	@Query(value = "SELECT * FROM Timing WHERE (Start > ?1 AND Koniec < ?2) ORDER BY Start ASC", nativeQuery = true)
	public List<Timing> findByDateRange(Date startDate, Date endDate);
		
	@Override
	<S extends Timing> S save(S entity);
	
}
