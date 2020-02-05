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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Magazin {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@Column
	private String name;
	@Column
	private Long issn;
	//ako je true onda urednik placa, korisnicima besplatno
	@Column
	private boolean placanje;
	@Column
	private boolean aktiviran;
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
	        name = "Magazin_NaucnaOblast", 
	        joinColumns = { @JoinColumn(name = "magazin_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "naucnaOblast_id") }
	  )
	private Set<NaucnaOblast> naucneOblastiMagazin = new HashSet<NaucnaOblast>();
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
	        name = "Magazin_User", 
	        joinColumns = { @JoinColumn(name = "magazin_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "user_id") }
	  )
	private Set<User> urednici = new HashSet<User>();

	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
	        name = "Magazin_User", 
	        joinColumns = { @JoinColumn(name = "magazin_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "user_id") }
	  )
	private Set<User> recenzenti = new HashSet<User>();

	@ManyToOne( fetch = FetchType.EAGER)	
	private User glavniUrednik;
	
	@OneToMany(mappedBy = "magazin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Text> tekstovi= new HashSet<Text>();
	
	public Magazin() {
		super();
	}
	public Magazin(Long id, String name, Long issn, boolean placanje) {
		super();
		this.id = id;
		this.name = name;
		this.issn = issn;
		this.placanje = placanje;
	}
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
	public Long getIssn() {
		return issn;
	}
	public void setIssn(Long issn) {
		this.issn = issn;
	}
	
	public boolean isPlacanje() {
		return placanje;
	}
	public void setPlacanje(boolean placanje) {
		this.placanje = placanje;
	}
	public Set<NaucnaOblast> getNaucneOblastiMagazin() {
		return naucneOblastiMagazin;
	}
	public void setNaucneOblastiMagazin(Set<NaucnaOblast> naucneOblastiMagazin) {
		this.naucneOblastiMagazin = naucneOblastiMagazin;
	}
	public Set<User> getUrednici() {
		return urednici;
	}
	public void setUrednici(Set<User> urednici) {
		this.urednici = urednici;
	}
	public Set<User> getRecenzenti() {
		return recenzenti;
	}
	public void setRecenzenti(Set<User> recenzenti) {
		this.recenzenti = recenzenti;
	}
	public boolean isAktiviran() {
		return aktiviran;
	}
	public void setAktiviran(boolean aktiviran) {
		this.aktiviran = aktiviran;
	}
	public User getGlavniUrednik() {
		return glavniUrednik;
	}
	public void setGlavniUrednik(User glavniUrednik) {
		this.glavniUrednik = glavniUrednik;
	}
	public Set<Text> getTekstovi() {
		return tekstovi;
	}
	public void setTekstovi(Set<Text> tekstovi) {
		this.tekstovi = tekstovi;
	}
	
	
}
