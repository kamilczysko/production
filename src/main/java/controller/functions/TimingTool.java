package controller.functions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import model.Production;
import model.Station;
import model.Timing;
import service.ProductionService;
import service.TimingService;

@Service
public class TimingTool {

	@Autowired
	private ProductionService productionService;
	@Autowired
	private TimingService timingService;

	private MultiValueMap<Station, Date[]> dateMap;

	public void initDateMap() {

		this.dateMap = new LinkedMultiValueMap<Station, Date[]>();
	}

	public Date saveTiming(List<Production> prod, Date processStart) {
		Date[] findTime = null;
		for (Production p : prod) {
			System.out.println("zapis: "+p.getIDProdukcja());
			Timing t = new Timing();
			t.setId(p.getIDProdukcja());
			t.setIlosc(p.getBatch().getIlosc());
			t.setProduction(p);
			t.setPracownik("");
			t.setOpis("");
			findTime = findTime(processStart, p.getDuration(), getTimingList(p.getStanowisko(), processStart),
					p.getStanowisko());
			t.setStart(findTime[0]);
			timingService.save(t);
		}
			return findTime[1];
		
	}

	public Date[] findTime(Date whenStartOperation, long operationDuration, List<Date[]> timingList, Station station) {

		if (timingList.isEmpty()) {
			Date[] time = new Date[2];
			time = getDatesRevere(whenStartOperation, operationDuration);
			timingList.add(time);
			dateMap.add(station, time);
		}
		return findMoment(station, operationDuration, whenStartOperation);

	}
	
	private Date[] findMoment(Station station, long opDuration, Date processStart) {

		List<Date[]> list = dateMap.get(station);
		ListIterator<Date[]> i = list.listIterator();
		Date[] result = null;

		for (Date[] d : list)
			System.out.println("czasy: " + d[0] + " -- " + d[1]);

		Date[] d = getDates(processStart, opDuration);
		Date[] tryBefore = d;

		if (tryBefore[1].before(list.get(0)[0])) {
													
			tryBefore = getDatesRevere(processStart, opDuration);
			dateMap.get(station).add(0, tryBefore);
			// result = tryBefore;

		}
		i = dateMap.get(station).listIterator();
		while (i.hasNext()) {
			Date[] d1 = i.next();
//			lastDate = d1[1];
			if (i.hasNext()) {
				Date[] d2 = i.next();
				if (!d2[1].after(processStart))
					continue;

				long diference = d2[0].getTime() - d1[1].getTime();
				if (opDuration < diference) {
					Date[] dates = getDates(d1[1], opDuration);
					result = dates;
					int previousIndex = i.previousIndex();
					dateMap.get(station).add(previousIndex, dates);
					break;
				} else
					i.previous();

			} else {
				
				double dif = Math.abs(d1[1].getTime() - processStart.getTime())/1000;
				if(d1[1].after(processStart) || dif <= 5){
					Date [] dat = getDates(d1[1], opDuration);
					dateMap.add(station, dat);
					result = dat;
				}else{
					Date[] dat = getDates(processStart, opDuration);
					result = dat;
					dateMap.add(station, dat);
				}
					
				break;
			}
		}

		return result;
	}

	private Date[] getDates(Date start, long duration) {
		Date[] dat = new Date[2];
		dat[0] = start;
		dat[1] = getEndDate(start, duration);
		return dat;
	}
	
	private Date[] getDatesRevere(Date end, long duration) {
		Date[] dat = new Date[2];
		dat[0] = getStartDate(end,duration);
		dat[1] = end;
		return dat;
	}

	private List<Date[]> getTimingList(Station station, Date beginDate) {
		if (!dateMap.containsKey(station)) {
			List<Timing> timings = productionService.findTimingsByStation(station, beginDate);
			List<Date[]> timingList = new ArrayList<Date[]>();
			for (Timing t : timings) {
				Date[] list = new Date[2];
				list[0] = t.getStart();
				if (t.getKoniec() != null && t.getKoniec() != new Date(0))
					list[1] = t.getKoniec();
				else
					list[1] = getEndDate(t.getStart(), t.getProduction().getDuration());

				System.out.println("czasy: " + list[0] + " -- " + list[1]);
				if (list[1].after(beginDate)) {
					timingList.add(list);
					dateMap.add(station, list);
				}
			}
			return timingList;
		} else
			return dateMap.get(station);
	}

	private Date getEndDate(Date start, long duration) {
		long czas = start.getTime() + duration * 1000;

		return new Date(czas);
	}
	
	private Date getStartDate(Date start, long duration) {
		long czas = start.getTime() - duration * 1000;

		return new Date(czas);
	}

}
