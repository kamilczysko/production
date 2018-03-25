package model;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

public class Article {

	@Id
	private int id;
	private String name;
	private String nameCont;
	private String barCode;
	private String katalogIdx;
	private String description;

	@Transient
	@ManyToOne(fetch=FetchType.LAZY)
	private List<Process> processList;
	
	
	
	public List<Process> getProcessList() {
		return processList;
	}

	public void setProcessList(List<Process> processList) {
		this.processList = processList;
	}

	public int getId() {
		return id;
	}

	public void setId(int i) {
		this.id = i;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameCont() {
		return nameCont;
	}

	public void setNameCont(String nameCont) {
		this.nameCont = nameCont;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getKatalogIdx() {
		return katalogIdx;
	}

	public void setKatalogIdx(String katalogIdx) {
		this.katalogIdx = katalogIdx;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
