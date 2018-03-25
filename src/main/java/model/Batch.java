package model;

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
@Table(name = "partia")
public class Batch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long IDPartia;

	private int NrPartii;
	@Column(name = "Zlecenie")
	private int zlecenie;
	private short Ilosc;
	private String Opis;
	private String Grupa;
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="\"Kuweta\"")
	private Container kuweta;
	@Transient
	private boolean update = false;
	@Transient
	private boolean save = false;

	public Batch(int nrPartii, int zlecenie, short ilosc, String opis, String grupa) {
		NrPartii = nrPartii;
		this.zlecenie = zlecenie;
		Ilosc = ilosc;
		Opis = opis;
		Grupa = grupa;
	}

	public Container getKuweta() {
		return kuweta;
	}

	public void setKuweta(Container kuweta) {
		this.kuweta = kuweta;
	}
	
	public Batch() {
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	public long getIDPartia() {
		return IDPartia;
	}

	public void setIDPartia(long iDPartia) {
		IDPartia = iDPartia;
	}

	public int getNrPartii() {
		return NrPartii;
	}

	public void setNrPartii(int nrPartii) {
		NrPartii = nrPartii;
	}

	public int getZlecenie() {
		return zlecenie;
	}

	public void setZlecenie(int zlecenie) {
		this.zlecenie = zlecenie;
	}

	public short getIlosc() {
		return Ilosc;
	}

	public void setIlosc(short ilosc) {
		Ilosc = ilosc;
	}

	public String getOpis() {
		return Opis;
	}

	public void setOpis(String opis) {
		Opis = opis;
	}

	public String getGrupa() {
		return Grupa;
	}

	public void setGrupa(String grupa) {
		Grupa = grupa;
	}

	@Override
	public String toString() {
		return this.getZlecenie() + " - " + this.getNrPartii() + " (" + this.getIlosc() + ") "+this.getGrupa()+" - "+this.getNrPartii();
	}
}
