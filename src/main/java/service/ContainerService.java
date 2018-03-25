package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Container;
import repository.ContainersRepository;

@Service
public class ContainerService {

	@Autowired
	private ContainersRepository containerRepository;
	
	public List<Container> getAllContainers(){
		return containerRepository.findAll();
	}
	
	public List<Container> getAllDeletedContainers(){
		return containerRepository.findAllDeleted();
	}
	
	public List<Container> save(List<Container> list){
		 return containerRepository.save(list);
	}
	
	public void remove(List<Container> list){
		containerRepository.delete(list);
	}
	
	
}
