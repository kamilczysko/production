package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "timing")
public class Timing {

	// @Id
	// @ManyToOne // (cascade=CascadeType.ALL)
	// @JoinColumn(name = "Produkcja")
	// private Production produkcja;

	@Id
	@Column(name = "Produkcja")
	private long id;

	private String Pracownik;
	private Date Start;
	private Date Koniec;
	private short Ilosc;
	private short Braki;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"Kuweta\"")
	private Container kuweta;

	@Transient
	private Production production;

	@Transient
	private String color;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	// @Column(insertable = false, updatable = false)
	private String Opis;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Production getProduction() {
		return production;
	}

	public void setProduction(Production production) {
		this.production = production;
		if (production != null) {
			setId(production.getIDProdukcja());
			setIlosc(production.getBatch().getIlosc());
		}
	}

	public String getPracownik() {
		return Pracownik;
	}

	public void setPracownik(String pracownik) {
		Pracownik = pracownik;
	}

	public Date getStart() {
		return Start;
	}

	public void setStart(Date start) {
		Start = start;
	}

	public Date getKoniec() {
		return Koniec;
	}

	public void setKoniec(Date koniec) {
		Koniec = koniec;
	}

	public short getIlosc() {
		return Ilosc;
	}

	public void setIlosc(short ilosc) {
		Ilosc = ilosc;
	}

	public short getBraki() {
		return Braki;
	}

	public void setBraki(short braki) {
		Braki = braki;
	}

	public String getOpis() {
		return Opis;
	}

	public void setOpis(String opis) {
		Opis = opis;
	}

	@Override
	public String toString() {
		return this.getId() + " -- timing\n";
	}

	public Container getContainer() {
		return kuweta;
	}

	public void setContainer(Container container) {
		this.kuweta = container;
	}

	@Transient
	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public long getDuration() {// zwraca czas trwanai operacji w sekundach
		long wholeTime = -1;
		try {
			int amount = this.getIlosc();
			long pretime;
			pretime = getInSeconds(dateFormat.parse(this.getProduction().getPreTime()));
			long postTime = getInSeconds(dateFormat.parse(this.getProduction().getPostTime()));
			long duration = getInSeconds(this.getProduction().getOperation().getDuration());
			wholeTime = (pretime + postTime + (amount * duration));// w
																	// sekundach
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return wholeTime;
	}

	private long getInSeconds(Date input) {// czas poddany w formacie HH:mm:ss
											// konwertowany jest na sekuny
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String time = dateFormat.format(input);
		String[] vals = time.split(":");
		Duration duration = Duration.parse(String.format("PT%sH%sM%sS", vals[0], vals[1], vals[2]));

		return duration.getSeconds();
	}

}
