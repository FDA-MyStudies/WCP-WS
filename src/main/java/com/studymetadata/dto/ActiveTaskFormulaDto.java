package com.studymetadata.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="activetask_formula")
public class ActiveTaskFormulaDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="activetask_formula_id")
	private Integer activetaskFormulaId;
	
	@Column(name="value")
	private String value;

	public Integer getActivetaskFormulaId() {
		return activetaskFormulaId;
	}

	public void setActivetaskFormulaId(Integer activetaskFormulaId) {
		this.activetaskFormulaId = activetaskFormulaId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
