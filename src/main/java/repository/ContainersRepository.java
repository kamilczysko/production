package repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import model.Container;

@Repository
@Qualifier("containersRepository")
public interface ContainersRepository extends JpaRepository<Container, Integer>{
	
	@Query(value = "select c from Container c where c.dataKasacji is null")
	List<Container> findAll();
	
	@Query(value = "select c from Container c where c.dataKasacji is not null")
	List<Container> findAllDeleted();
	
	@Override <S extends Container> List<S> save(Iterable<S> entities);
	
	@Override <S extends Container> S save(S entity);
}
