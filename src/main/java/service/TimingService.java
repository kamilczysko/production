package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import model.Batch;
import model.Container;
import model.Production;
import model.Station;
import model.Timing;
import repository.TimingRepository;

@Service
public class TimingService {
	//
	@Autowired
	private TimingRepository timingRepo;

	@Autowired
	private ProductionService prodService;
	
	@Autowired
	private BatchService batchService;
	
	@Autowired
	private ProductionService productionService;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
	
	@Autowired
	private JdbcTemplate template;


	public List<Timing> getAll() {
		List<Timing> timingList = (List<Timing>) timingRepo.findAll();
		for (Timing t : timingList)
			t.setProduction(prodService.getById(t.getId()));
		return null;
	}

	public List<Timing> getById(Production prod) {
		List<Timing> list = getById(prod, null);
		return list;
	}
	
	public List<Timing> getByStartDateAndStation(Station station, Date start){
		String query = "select * from timing "
				+ "inner join produkcja on Produkcja = produkcja.IDProdukcja where Stanowisko = "+station.getStationId()+" "
				+ "AND  Start >= convert(datetime, '"+dateFormat.format(start)+"',120) order by Start Asc";
		List<Timing> list = template.query(query, new RowMapper<Timing>(){

			@Override
			public Timing mapRow(ResultSet set, int arg1) throws SQLException {
				Production p = null;//makeSingleProduction(set.getLong(1));
				return makeSingleTimingRecord(set, p);
			}
		});
	
		return list;
	}
	
	public List<Timing> getByDateRange(Date start, Date end) {
		String query = "select * from timing where Start > convert(datetime, '"+dateFormat.format(start)+"',120) "
				+ "AND Koniec < convert(datetime, '"+dateFormat.format(end)+"',120)";
		List<Timing> list = template.query(query, new RowMapper<Timing>(){

			@Override
			public Timing mapRow(ResultSet set, int arg1) throws SQLException {
				Production p = productionService.getById(set.getLong(1));
				return makeSingleTimingRecord(set, p);
			}
		});
	
		return list;
	}

	
	public List<Timing> getById(Production p, Date startDate) {
				
		String query = "select * from timing where Produkcja = '"+p.getIDProdukcja()+"' order by Start ASC";
		List<Timing> list = template.query(query, new RowMapper<Timing>(){

			@Override
			public Timing mapRow(ResultSet set, int arg1) throws SQLException {
				return makeSingleTimingRecord(set, p);
			}
		});
		return list;
	}
	
	private Timing makeSingleTimingRecord(ResultSet set, Production p){
		Timing t = new Timing();
		try {
			t.setId(set.getLong(1));
			t.setProduction(p);
			t.setIlosc(set.getShort("Ilosc"));
			t.setPracownik(set.getString("Pracownik"));
			t.setKoniec(set.getTimestamp("Koniec"));
			t.setStart(set.getTimestamp("Start"));
			t.setOpis(set.getString("Opis"));
			t.setBraki(set.getShort("Braki"));
			t.setContainer((Container) set.getObject("Kuweta"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return t;
	}
	
	@Autowired
	ProductionService prod;
	
	private Production makeSingleProduction(long id){
		return prod.getById(id);
	}
	
	public List<Timing> getForStats(Production production){
		return timingRepo.findByProdId(production.getIDProdukcja());
	}
	
	public List<Map<String, Object>> getTimingsForOrder(int orderId){// timing, partia, stanowisko, [pretime, duration, posttime]
		List<Batch> ordersList = batchService.getByOrder(orderId);
				List<Production> prodList = new ArrayList<Production>();
		for(Batch b : ordersList){
			List<Production> prod = productionService.getByBatch(b);
			prodList.addAll(prod);
		}

		List<Map<String, Object>> timingList = new ArrayList<Map<String, Object>>();
		for(Production p : prodList) {
			List<Timing> timing = getById(p);
			
			for(Timing t : timing){
				Map<String, Object> tmpMap = new TreeMap<String, Object>();
				tmpMap.put("timing", t);
				tmpMap.put("batch", p.getBatch());
				tmpMap.put("station", p.getStanowisko());
				tmpMap.put("time", Arrays.asList(p.getPreTime(), p.getOperation().getDuration() ,p.getPostTime()));
				timingList.add(tmpMap);
			}
		}
		return timingList;
	}


	public Timing save(Timing t){
		return timingRepo.save(t);
	}

}
