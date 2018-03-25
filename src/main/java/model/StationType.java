package model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "stationType")
public class StationType {

	public StationType() {
	}

	public StationType(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// @JsonProperty("idType")
	private long IDTypu;

	@Column(name = "Nazwa")
	private String name;
	@Column(name = "Opis")
	private String description;

	@ManyToMany(mappedBy="stationType", fetch=FetchType.LAZY, cascade={CascadeType.DETACH, CascadeType.REFRESH})
	//CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH
	private List<Station> station;

	public long getIDTypu() {
		return IDTypu;
	}

	public void setIDTypu(long iDTypu) {
		IDTypu = iDTypu;
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

	public List<Station> getStation() {
		return station;
	}

	public void setStation(List<Station> station) {
		this.station = station;
	}

	@Override
	public String toString() {
		return this.getName();
	}

}
