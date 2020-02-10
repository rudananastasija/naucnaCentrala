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
public class Text {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@Column
	private String naslov;
	@Column
	private String rezime;
	
	@Column
	private String fajl;
	
	@Column
	private boolean uplaceno;
	
	@OneToMany(mappedBy = "text", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<KljucnaRijec> kljucneRijeci= new HashSet<KljucnaRijec>();
	
	@ManyToOne( fetch = FetchType.EAGER)	
	private Magazin magazin;
	
	@ManyToOne( fetch = FetchType.EAGER)	
	private NaucnaOblast oblast;
	
	@ManyToOne( fetch = FetchType.EAGER)	
	private User autor;
	
	@ManyToMany(mappedBy = "radoviKoautor")
	private Set<User> koautori = new HashSet<User>();
	
	@ManyToMany(mappedBy = "radoviRecenzent")
	private Set<User> recenzenti = new HashSet<User>();


	
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
	public User getAutor() {
		return autor;
	}
	public void setAutor(User autor) {
		this.autor = autor;
	}
	public Set<User> getKoautori() {
		return koautori;
	}
	public void setKoautori(Set<User> koautori) {
		this.koautori = koautori;
	}
	public String getFajl() {
		return fajl;
	}
	public void setFajl(String fajl) {
		this.fajl = fajl;
	}
	public Set<User> getRecenzenti() {
		return recenzenti;
	}
	public void setRecenzenti(Set<User> recenzenti) {
		this.recenzenti = recenzenti;
	}
	public boolean isUplaceno() {
		return uplaceno;
	}
	public void setUplaceno(boolean uplaceno) {
		this.uplaceno = uplaceno;
	}
	
	
	
	
	
}
