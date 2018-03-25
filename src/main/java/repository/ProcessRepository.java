package repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import model.Process;

@Repository
public interface ProcessRepository extends CrudRepository<Process, Long>{
	
	public List<Process> findByArticle(int Art);
	public List<Process> findByName(String name);
	
	@Query(value = "select distinct p.IDProces, p.Artykul, p.Nazwa, p.Opis, p.Partia from proces p "
			+ "join operacja on IDProces = Proces "
			+ "join produkcja on Operacja = IDOperacja "
			+ "join partia on IDPartia = produkcja.Partia "
			+ "join timing on Produkcja = IDProdukcja "
			+ "where Zlecenie = ?1 AND Koniec is null" , nativeQuery=true)
	public List<Process> findOperationForOrder(long order);
	
}
