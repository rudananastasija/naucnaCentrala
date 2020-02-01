package com.example.naucna.model;

public class OgranicenjeDto {
	String fieldName;
	boolean required;
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
	
	
}
