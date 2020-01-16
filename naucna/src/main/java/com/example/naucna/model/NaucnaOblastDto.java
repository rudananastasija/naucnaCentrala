package com.example.naucna.model;

public class NaucnaOblastDto {
	protected Long id;
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public NaucnaOblastDto(String name) {
		super();
		this.name = name;
	}
	
	
}
