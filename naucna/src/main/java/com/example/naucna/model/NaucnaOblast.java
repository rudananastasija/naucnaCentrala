package com.example.naucna.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class NaucnaOblast {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@Column
	private String name;
	
	public NaucnaOblast(String name) {
		super();
		this.name = name;
	}
	public NaucnaOblast() {}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToMany(mappedBy = "naucneOblasti")
	private Set<User> korisnici = new HashSet<User>();
	
	@ManyToMany(mappedBy = "naucneOblastiMagazin")
	private Set<Magazin> magazini = new HashSet<Magazin>();

	public Set<User> getKorisnici() {
		return korisnici;
	}
	public void setKorisnici(Set<User> korisnici) {
		this.korisnici = korisnici;
	}
	public Set<Magazin> getMagazini() {
		return magazini;
	}
	public void setMagazini(Set<Magazin> magazini) {
		this.magazini = magazini;
	}
	
	
}
