package controller.functions;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import model.Batch;
import model.Station;
import model.Timing;
import service.TimingService;

public class GanttLabel extends Label{

	private long zeroReference;
	private int factor;
	private int offset = 0;
	
	private String pracownik;
	private String opis;
	private Station stanowisko;
	private Batch batch;
	
	public GanttLabel(){
		this.setAlignment(Pos.CENTER);
		
		
	}
	
	public void setParams(Timing t, long zero, int factor, int offset){
		this.setPrefHeight(50.0);
		this.getStyleClass().add("labsiorek");
		this.offset = offset;
		this.factor = factor;
		this.zeroReference = zero;
		setInfo(t);
	}
	
	private void setInfo(Timing t){
		setInfos(t);
	}
	
	private void setInfos(Timing t){
		this.pracownik = t.getPracownik();
		this.opis = t.getOpis();
		this.stanowisko = t.getProduction().getStanowisko();
		this.batch = t.getProduction().getBatch();
		this.setDimensions(t);
		
//		System.out.println("label: "+batch.getNrPartii()+" \n"+this.pracownik);
		
		this.setText(batch.getZlecenie()+" - "+batch.getNrPartii()+" \n"+this.pracownik);
		Tooltip toolTip = new Tooltip();
		toolTip.setText(""+stanowisko.getName()+"("+stanowisko.getStationId()+")\n"+"Liczba sztuk: "+t.getIlosc()+"\n"+t.getProduction().getOperation().getNazwa());
		this.setTooltip(toolTip);
	}
	
	
	
	private void setDimensions(Timing t){
		
		if(t.getKoniec() == null )
			this.setStyle("-fx-background-color: "+t.getColor());
		Date start = t.getStart();
		long duration;

		if(t.getKoniec() == null || t.getKoniec().getTime() < 0){
			duration = t.getDuration();
			if(t.getKoniec() == null)
				this.getStyleClass().add("labsiorek");
			else
				this.getStyleClass().add("labsiorek-started");
			
		}else{
			duration = (t.getKoniec().getTime() - t.getStart().getTime())/1000;
			this.getStyleClass().add("labsiorek-done");
		}
		this.setPrefWidth(duration);
		this.setTranslateX(getXpos(start));		
	}
	
	private double getXpos(Date start){
		return ((start.getTime()/1000) - zeroReference+offset);
	}
	
	public void changeFactor(double factor){
		this.setPrefWidth(this.getPrefWidth()*factor);
		this.setTranslateX((this.getTranslateX()-offset)*factor+offset);
	}
	
	public void setPosY(double yPos){
		this.setTranslateY(yPos);
	}
	
	public String getWorker(){
		return this.pracownik;
	}
	

	public String getDescription() {
		return opis;
	}


	public Station getStation() {
		return stanowisko;
	}


	public Batch getBatch() {
		return batch;
	}
	
	private TimingService timingService;

	public void setTimingService(TimingService timingService) {
		this.timingService = timingService;
		
	}


}
