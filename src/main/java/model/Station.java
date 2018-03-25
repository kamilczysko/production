package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "station")
public class Station {

	public Station() {
	}

	public Station(String name, String description, Date preTime, Date postTime, List<StationType> stationType) {
		this.name = name;
		this.description = description;
		this.preTime = preTime;
		this.postTime = postTime;
		this.stationType = stationType;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "\"IDStanowisko\"")
	private long stationId;
	@Column(name = "\"Nazwa\"")
	private String name;
	@Column(name = "\"Opis\"")
	private String description;
	@Column(name = "\"PreTime\"")
	private Date preTime;
	@Column(name = "\"PostTime\"")
	private Date postTime;

	@ManyToMany(cascade={CascadeType.REFRESH, CascadeType.DETACH}, fetch=FetchType.LAZY)
	@JoinTable(name = "oznaczenieTypuStanowiska",
	joinColumns = @JoinColumn(name = "Stanowisko"),
	inverseJoinColumns = @JoinColumn(name = "TypStanowiska"))
	private List<StationType> stationType;


	@Transient
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPreTime() {
		return preTime;
	}

	public void setPreTime(Date preTime) {
		this.preTime = preTime;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public List<StationType> getStationType() {
		return stationType;
	}

	public void setStationType(List<StationType> stationType) {
		this.stationType = stationType;
	}

	@Transient
	private String possibleDate = new String();
	
	
	public String getPossibleDate() {
		return possibleDate;
	}

	
	public void setPossibleDate(Date possibleDate) {
		if(possibleDate == null || possibleDate.before(new Date(System.currentTimeMillis())) )
			this.possibleDate = "teraz";
		else
			this.possibleDate = this.dateFormat.format(possibleDate);
	}

	@Override
	public String toString() {
		if(possibleDate != null)
			return getName()+", dostepne: "+getPossibleDate();
		else
			return getName();
	}
}
