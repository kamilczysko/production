package controller.functions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import model.Batch;
import model.Operation;
import model.Production;
import model.Station;
import service.BatchService;
import service.ProductionService;
import service.StationService;

@Service
public class SaveProduction {
	
	private List<Operation> firstOperations;
	
	@Autowired
	private StationService stationService;
	@Autowired
	private ProductionService productionService;
	@Autowired
	private TimingTool timingTool;
	
	private SimpleDateFormat format = new SimpleDateFormat();
	
	private List<Production> prodListToSave = new ArrayList<Production>();
	private List<Production> lastProduction = new ArrayList<Production>();
	
public void saveProduction(List<Operation> fistOperations, Date start){
		
		this.firstOperations = firstOperations;
		
		prodListToSave = new ArrayList<Production>();
		lastProduction = new ArrayList<Production>();
		
		Map<String, Operation> opMap = makeSortedMap(fistOperations);
		List<Production> savedProdList = null;
		char group = 'a';
		
		timingTool.initDateMap();
		Date timeForOperation = start;
		Date timeForLastOperation = start;
		
		while(opMap.containsKey(group+"")){
			timeForOperation = start;
			Operation operation = opMap.get(group+"");
			prodListToSave = new ArrayList<Production>();
			MultiValueMap<Operation, Production> makeProductionList = makeProductionList(operation);
			Set<Operation> keySet = makeProductionList.keySet();
			for(Operation k : keySet) {
				List<Production> saveProdInBase = saveProdInBase(makeProductionList.get(k));
				timeForOperation = timingTool.saveTiming(saveProdInBase, timeForOperation);
				if(timeForLastOperation.before(timeForOperation))
					timeForLastOperation = timeForOperation;
			}
				
			group += 1;
		}
		
		timingTool.saveTiming(saveProdInBase(lastProduction), timeForLastOperation);
		
		System.out.println(savedProdList);
	}
	
	//sortowanie partii zrobic
	
	private List<Production> saveProdInBase(List<Production> production){//zapis produkcji i timingu jednoczesnie
		List<Production> prodList = new ArrayList<Production>();
		for(Production prod : production)
			prodList.add(productionService.save(prod));
		
		return prodList;
	}
	
	private Map<String, Operation> makeSortedMap(List<Operation> firstOperations){
		Map<String, Operation> opMap = new TreeMap<String, Operation>();
		for(Operation first : firstOperations)
			switch(first.getGroup()){//do 4 grup obsluga
			case "a":
				opMap.put("a", first);
				break;
			case "b":
				opMap.put("b", first);
				break;
			case "c":
				opMap.put("c", first);
				break;
			case "d":
				opMap.put("d", first);
				break;
			}
		return opMap;
	}
	@Autowired
	private BatchService batchService;
	
	private MultiValueMap<Operation, Production> prodMap = new LinkedMultiValueMap<Operation, Production>();
		
	private MultiValueMap<Operation, Production> makeProductionList(Operation o){

		Operation tmpOp = o;
		
		prodMap = new LinkedMultiValueMap<Operation, Production>();
				
		while(tmpOp.getKolejna() != null){
			tmpOp = getDataToGenerateProduction(tmpOp);
		}
		
		if(tmpOp.getKolejna() == null){
			System.out.println("dodano");
			boolean test = false;
			for(Production p : lastProduction)//czy nie ma tego gówna juz - ostaniej operacji
				if(tmpOp == p.getOperation())
					test = true;
			if(!test)
				getDataToGenerateProduction(tmpOp, lastProduction);
		}
		
		System.out.println("Ostatinoa: "+lastProduction);
		return prodMap;
	}
	
	private Operation getDataToGenerateProduction(Operation op){
		MultiValueMap<Long, Batch> map = op.getMap();
		Set<Long> keySet = map.keySet();
		for(long key : keySet){
			Station station = stationService.getById(key);
			
			List<Batch> list = map.get(key);
			sortList(list);
			
			for(Batch b : list)
				prodMap.add(op, makeProduction(b,station,op));
			
		}
		return op.getKolejna();
	}
	
	private Operation getDataToGenerateProduction(Operation op, List<Production> prodListToSave){
		MultiValueMap<Long, Batch> map = op.getMap();
		Set<Long> keySet = map.keySet();
		for(long key : keySet){
			Station station = stationService.getById(key);
			
			List<Batch> list = map.get(key);
			sortList(list);
			
			for(Batch b : list)
				prodListToSave.add(makeProduction(b, station, op));
			
		}
		return op.getKolejna();
	}
	
	private Production makeProduction(Batch b, Station station, Operation op){
		Production prod = new Production();
		batchService.save(b);
		prod.setBatch(b);
		prod.setDescription("");
		prod.setOperation(op);
		prod.setStanowisko(station);
		prod.setPostTime(format.format(op.getPostTime()));
		prod.setPreTime(format.format(op.getPreTime()));
		System.out.println("prod: "+prod);
		return prod;
	}
	
	private void sortList(List<Batch> list){
		Collections.sort(list, new Comparator<Batch>() {

			@Override
			public int compare(Batch o1, Batch o2) {
				return (o1.getNrPartii() > o2.getNrPartii()) ? o1.getNrPartii() : o2.getNrPartii();
			}
			
		});
	}
	
}
