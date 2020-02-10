package com.example.naucna.model;

public class OgranicenjeDto {
	String fieldName;
	boolean required;
	//-1 ako je nema
	int minDuzina;
	int maxDuzina;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public OgranicenjeDto(String fieldName, boolean required) {
		super();
		this.fieldName = fieldName;
		this.required = required;
	}
	public int getMinDuzina() {
		return minDuzina;
	}
	public void setMinDuzina(int minDuzina) {
		this.minDuzina = minDuzina;
	}
	public int getMaxDuzina() {
		return maxDuzina;
	}
	public void setMaxDuzina(int maxDuzina) {
		this.maxDuzina = maxDuzina;
	}
	
	
}
