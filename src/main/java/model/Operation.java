package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javafx.beans.property.SimpleBooleanProperty;

@Entity
@Table(name = "operacja")
public class Operation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long IDOperacja;

	private String Nazwa;

	@OneToOne(cascade = {CascadeType.MERGE}, fetch=FetchType.EAGER)
	@JoinColumn(name = "\"Proces\"")
	private Process proces;

	@ManyToOne(cascade = CascadeType.MERGE, fetch=FetchType.EAGER)
	@JoinColumn(name = "\"Kolejna\"")
	// @JsonIgnore
	private Operation Kolejna;
	@OneToOne(cascade = CascadeType.MERGE, fetch=FetchType.EAGER)
	@JoinColumn(name = "StationType")
	private StationType StationType;
	private Date PreTime;
	@Column(nullable = false)
	private Date PostTime;
	@Column(nullable = false)
	private Date Duration;
	private String Opis;
	
	@Transient
	private boolean first = false;
	@Transient
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	@Transient
	private MultiValueMap<Long, Batch> map = new LinkedMultiValueMap<Long, Batch>();
	@Transient
	private SimpleBooleanProperty readyToSave= new SimpleBooleanProperty(false);
	
	
	public Operation(){
		setNazwa("Nowa operacja");
		try {
			setPreTime(format.parse("00:00:10"));
			setPostTime(format.parse("00:00:10"));
			setDuration(format.parse("00:00:10"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	
	public SimpleBooleanProperty getReady() {
		return readyToSave;
	}



	public void setReady(boolean ready) {
		this.readyToSave.set(ready);
	}



	public MultiValueMap<Long, Batch> getMap() {
		return map;
	}



	public void setMap(MultiValueMap<Long, Batch> map) {
		this.map = map;
	}



	public Operation(String name){
		setNazwa(name);
		try {
			setPreTime(format.parse("00:00:10"));
			setPostTime(format.parse("00:00:10"));
			setDuration(format.parse("00:00:10"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	@Transient
	private String group;

	
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public long getIDOperacja() {
		return IDOperacja;
	}

	public void setIDOperacja(long iDOperacja) {
		IDOperacja = iDOperacja;
	}

	public String getNazwa() {
		return Nazwa;
	}

	public void setNazwa(String nazwa) {
		Nazwa = nazwa;
	}

	public Process getProces() {
		return proces;
	}

	public void setProces(Process proces) {
		this.proces = proces;
	}

	public Operation getKolejna() {
		return Kolejna;
	}

	public void setKolejna(Operation kolejna) {
		Kolejna = kolejna;
	}

	public StationType getStationType() {
		return StationType;
	}

	public void setStationType(StationType stationType) {
		StationType = stationType;
	}

	public Date getPreTime() {
		return PreTime;
	}

	public void setPreTime(Date preTime) {
		PreTime = preTime;
	}

	public Date getPostTime() {
		return PostTime;
	}

	public void setPostTime(Date postTime) {
		PostTime = postTime;
	}

	public Date getDuration() {
		return Duration;
	}

	public void setDuration(Date duration) {
		Duration = duration;
	}

	public String getOpis() {
		return Opis;
	}

	public void setOpis(String opis) {
		Opis = opis;
	}
	
	@Override
	public String toString() {
		return this.getNazwa();
	}

}
