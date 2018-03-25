package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.scene.paint.Color;

@Entity
@Table(name="kuwety")
public class Container {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private Date dataUtworzenia;
	private Date dataKasacji;
	private short oznaczenie;
	private double r;
	private double g;
	private double b;
	
	@Transient 
	private Color color;
	

	public Container() {
		this.setColor(Color.RED);
		this.setDataUtworzenia(new Date((System.currentTimeMillis())));
	}
	
	public Color getColor() {
		Color c = new Color(this.getR(),this.getG(), this.getB(), 1.0);
		return c;
	}
	public void setColor(Color color) {
		this.color = color;		
		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDataUtworzenia() {
		return dataUtworzenia;
	}
	public void setDataUtworzenia(Date dataUtworzenia) {
		this.dataUtworzenia = dataUtworzenia;
	}
	public Date getDataKasacji() {
		return dataKasacji;
	}
	public void setDataKasacji(Date dataKasacji) {
		this.dataKasacji = dataKasacji;
	}
	public short getOznaczenie() {
		return oznaczenie;
	}
	public void setOznaczenie(short oznaczenie) {
		this.oznaczenie = oznaczenie;
	}
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	public double getG() {
		return g;
	}
	public void setG(double g) {
		this.g = g;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	
	@Override
	public String toString() {
		return this.getR()+" - "+this.getG()+" - "+this.getB()+ " - "+this.getOznaczenie();
	}
	
}
