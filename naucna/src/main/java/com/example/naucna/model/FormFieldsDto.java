package com.example.naucna.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.FormFieldValidationConstraint;
public class FormFieldsDto {
	String taskId;
	List<FormField> formFields;
	String processInstanceId;	
	List<FormFieldValidationConstraint> constraints;
	List<OgranicenjeDto> ogranicenja;
	public FormFieldsDto(String taskId, String processInstanceId, List<FormField> formFields) {
		super();
		this.taskId = taskId;
		this.formFields = formFields;
		this.processInstanceId = processInstanceId;
	}

	public FormFieldsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<FormField> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<FormField> formFields) {
		this.formFields = formFields;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public List<FormFieldValidationConstraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<FormFieldValidationConstraint> constraints) {
		this.constraints = constraints;
	}

	public FormFieldsDto(String taskId, String processInstanceId, List<FormField> formFields,
			List<FormFieldValidationConstraint> constraints) {
		super();
		this.taskId = taskId;
		this.formFields = formFields;
		this.processInstanceId = processInstanceId;
		this.constraints = constraints;
	}

	public List<OgranicenjeDto> getOgranicenja() {
		return ogranicenja;
	}

	public void setOgranicenja(List<OgranicenjeDto> ogranicenja) {
		this.ogranicenja = ogranicenja;
	}

	public FormFieldsDto(String taskId, List<FormField> formFields, String processInstanceId,
			List<FormFieldValidationConstraint> constraints, List<OgranicenjeDto> ogranicenja) {
		super();
		this.taskId = taskId;
		this.formFields = formFields;
		this.processInstanceId = processInstanceId;
		this.constraints = constraints;
		this.ogranicenja = ogranicenja;
	}

	

	
	
}
