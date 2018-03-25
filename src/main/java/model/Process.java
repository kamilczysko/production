package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "proces")
public class Process {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDProces")
	private long IDProces;

	@Column(name = "\"Artykul\"")
	private int article;
	@Column(name = "\"Nazwa\"")
	private String name;
	@Column(name = "\"Opis\"")
	private String description;
	@Column(name = "\"Partia\"")
	private short batch;

	public long getIDProces() {
		return IDProces;
	}

	public void setIDProces(long iDProces) {
		IDProces = iDProces;
	}

	public int getArticle() {
		return article;
	}

	public void setArticle(int article) {
		this.article = article;
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

	public short getBatch() {
		return batch;
	}

	public void setBatch(short batch) {
		this.batch = batch;
	}
	
	@Override
	public String toString() {
		return this.getName()+" - "+this.getIDProces()+" - "+this.getArticle() + " - "+this.getBatch();
	}

}
