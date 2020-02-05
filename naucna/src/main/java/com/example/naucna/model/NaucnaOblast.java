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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@OneToMany(mappedBy = "oblast", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Text> tekstovi= new HashSet<Text>();
	

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
	public Set<Text> getTekstovi() {
		return tekstovi;
	}
	public void setTekstovi(Set<Text> tekstovi) {
		this.tekstovi = tekstovi;
	}
	
	
}
