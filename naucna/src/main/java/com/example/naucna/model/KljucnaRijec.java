package com.example.naucna.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class KljucnaRijec {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@Column
	private String rijec;
	@ManyToOne( fetch = FetchType.EAGER)	
	private Text text;
	
	public KljucnaRijec() {
		super();
	}
	
	public KljucnaRijec(String rijec, Text text) {
		super();
		this.rijec = rijec;
		this.text = text;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRijec() {
		return rijec;
	}
	public void setRijec(String rijec) {
		this.rijec = rijec;
	}
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	
	

	
}
