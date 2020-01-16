package com.example.naucna.model;

public class UserDto {
	protected Long id;
	private String name;
	private String uloga;
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
	public String getUloga() {
		return uloga;
	}
	public void setUloga(String uloga) {
		this.uloga = uloga;
	}
	public UserDto(Long id, String name, String uloga) {
		super();
		this.id = id;
		this.name = name;
		this.uloga = uloga;
	}
	
	
}
