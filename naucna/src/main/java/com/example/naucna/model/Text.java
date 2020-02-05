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
public class Text {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@Column
	private String naslov;
	@Column
	private String rezime;
	
	@OneToMany(mappedBy = "text", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<KljucnaRijec> kljucneRijeci= new HashSet<KljucnaRijec>();
	
	@ManyToOne( fetch = FetchType.EAGER)	
	private Magazin magazin;
	
	@ManyToOne( fetch = FetchType.EAGER)	
	private NaucnaOblast oblast;
	
	public Text() {
		super();
	}
	public Text(String naslov, String rezime, Set<KljucnaRijec> kljucneRijeci) {
		super();
		this.naslov = naslov;
		this.rezime = rezime;
		this.kljucneRijeci = kljucneRijeci;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNaslov() {
		return naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	public String getRezime() {
		return rezime;
	}

	public void setRezime(String rezime) {
		this.rezime = rezime;
	}

	public Set<KljucnaRijec> getKljucneRijeci() {
		return kljucneRijeci;
	}

	public void setKljucneRijeci(Set<KljucnaRijec> kljucneRijeci) {
		this.kljucneRijeci = kljucneRijeci;
	}

	public Magazin getMagazin() {
		return magazin;
	}

	public void setMagazin(Magazin magazin) {
		this.magazin = magazin;
	}
	public NaucnaOblast getOblast() {
		return oblast;
	}
	public void setOblast(NaucnaOblast oblast) {
		this.oblast = oblast;
	}
	
	
	
	
	
}
