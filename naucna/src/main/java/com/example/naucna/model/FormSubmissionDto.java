package com.example.naucna.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FormSubmissionDto implements Serializable{
	String fieldId;
	String fieldValue;
	List<String> oblasti = new ArrayList<String>(); 
	List<String> urednici = new ArrayList<String>(); 
	List<String> recenzenti = new ArrayList<String>(); 
	
	public FormSubmissionDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FormSubmissionDto(String fieldId, String fieldValue) {
		super();
		this.fieldId = fieldId;
		this.fieldValue = fieldValue;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldValue() {
		return fieldValue;
	}
	

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public List<String> getOblasti() {
		return oblasti;
	}

	public void setOblasti(ArrayList<String> oblasti) {
		this.oblasti = oblasti;
	}

	public List<String> getUrednici() {
		return urednici;
	}

	public void setUrednici(List<String> urednici) {
		this.urednici = urednici;
	}

	public List<String> getRecenzenti() {
		return recenzenti;
	}

	public void setRecenzenti(List<String> recenzenti) {
		this.recenzenti = recenzenti;
	}

	
}
