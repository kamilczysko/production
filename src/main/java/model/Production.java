package model;

import java.text.SimpleDateFormat;
import java.time.Duration;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "produkcja")
public class Production {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long IDProdukcja;

	// (cascade = { CascadeType.ALL, CascadeType.MERGE}, fetch=FetchType.EAGER)
	@OneToOne(cascade = { CascadeType.MERGE/*, CascadeType.REMOVE*/ }, fetch=FetchType.EAGER)
	@JoinColumn(name = "Partia")
	private Batch batch;
	@OneToOne(cascade = { CascadeType.MERGE /*, CascadeType.REFRESH*/ }, fetch=FetchType.EAGER)
	@JoinColumn(name = "Operacja")
	private Operation operation;
	@OneToOne(cascade = {CascadeType.MERGE/*, CascadeType.REFRESH*/}, fetch=FetchType.EAGER)
	@JoinColumn(name = "\"Stanowisko\"")
	private Station Stanowisko;

	private String PreTime;
	private String PostTime;
	@Column(name = "Opis")
	private String description;

	@Transient
	private List<Timing> timing;
	@Transient
	private String group;
	@Transient
	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	public String getGroup(){
		return batch.getGrupa();
	}
	
	public long getDuration(){//zwraca czas trwanai operacji w sekundach
		int amount = batch.getIlosc();
		long pretime = getInSeconds(operation.getPreTime());
		long postTime = getInSeconds(operation.getPostTime());
		long duration = getInSeconds(operation.getDuration());
		long wholeTime = (pretime + postTime + (amount * duration));// w sekundach
		System.out.println(this+" -duration --- "+wholeTime+" preTime"+operation.getPreTime()+" post"+operation.getPostTime()+" dur"+operation.getDuration());
		return wholeTime;
	}
	
	private long getInSeconds(Date input){//czas poddany w formacie HH:mm:ss konwertowany jest na sekuny
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String time = dateFormat.format(input);
		String[] vals = time.split(":");
		Duration duration = Duration.parse(String.format("PT%sH%sM%sS", vals[0],vals[1],vals[2]));

		return duration.getSeconds();
	}

	public List<Timing> getTiming() {
		return timing;
	}

	public void setTiming(List<Timing> timing) {
		this.timing = timing;
	}

	public long getIDProdukcja() {
		return IDProdukcja;
	}

	public void setIDProdukcja(long iDProdukcja) {
		IDProdukcja = iDProdukcja;
	}

	public String getPreTime() {
		return PreTime;
	}

	public void setPreTime(String preTime) {
		PreTime = preTime;
	}

	public String getPostTime() {
		return PostTime;
	}

	public void setPostTime(String postTime) {
		PostTime = postTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public Station getStanowisko() {
		return Stanowisko;
	}

	public void setStanowisko(Station stanowisko) {
		Stanowisko = stanowisko;
	}
	
	@Override
	public String toString() {
		return "produkcja--- operacja:"+this.getOperation()+", stanowisko:"+this.getStanowisko()+", partia: "+this.getBatch()+"||||\n";
	}

}
