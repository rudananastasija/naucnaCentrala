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
import javax.persistence.JoinTable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "ime", nullable = false)
	private String ime;

	@Column(name = "prezime", nullable = false)
	private String prezime;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name="grad", nullable= false)
	private String grad;
	
	@Column(name="drzava", nullable= false)
	private String drzava;
	
	@Column(name="titula", nullable= true)
	private String titula;
	
	@Column(name="recenzent", nullable= true)
	private boolean recenzent;
	
	@Column(name = "username", nullable = false)
	private String username;

	@Column(name="lozinka")
	private String lozinka;
	
	@Column(name="uloga")
	private String uloga;
	
	@Column(name="verifikovan")
	private boolean verifikovan;
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
	        name = "User_NaucnaOblast", 
	        joinColumns = { @JoinColumn(name = "user_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "naucnaOblast_id") }
	  )
	private Set<NaucnaOblast> naucneOblasti = new HashSet<NaucnaOblast>();

	@ManyToMany(mappedBy = "urednici")
	private Set<Magazin> magaziniUrednik = new HashSet<Magazin>();

	@ManyToMany(mappedBy = "recenzenti")
	private Set<Magazin> magaziniRecenzent = new HashSet<Magazin>();
	
	@OneToMany(mappedBy = "glavniUrednik", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Magazin> magaziniGlavniUrednik = new HashSet<Magazin>();


	public User() {
		super();
	}
	
	public User(String ime, String prezime, String email, String grad, String drzava, String titula,
			boolean recenzent, String username, String lozinka, Set<NaucnaOblast> naucneOblasti) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.grad = grad;
		this.drzava = drzava;
		this.titula = titula;
		this.recenzent = recenzent;
		this.username = username;
		this.lozinka = lozinka;
		this.naucneOblasti = naucneOblasti;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getDrzava() {
		return drzava;
	}

	public void setDrzava(String drzava) {
		this.drzava = drzava;
	}

	public String getTitula() {
		return titula;
	}

	public void setTitula(String titula) {
		this.titula = titula;
	}

	public boolean isRecenzent() {
		return recenzent;
	}

	public void setRecenzent(boolean recenzent) {
		this.recenzent = recenzent;
	}
	

	public boolean isVerifikovan() {
		return verifikovan;
	}

	public void setVerifikovan(boolean verifikovan) {
		this.verifikovan = verifikovan;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public Set<NaucnaOblast> getNaucneOblasti() {
		return naucneOblasti;
	}

	public void setNaucneOblasti(Set<NaucnaOblast> naucneOblasti) {
		this.naucneOblasti = naucneOblasti;
	}

	public String getUloga() {
		return uloga;
	}

	public void setUloga(String uloga) {
		this.uloga = uloga;
	}

	public User(Long id, String ime, String prezime, String email, String grad, String drzava, String titula,
			boolean recenzent, String username, String lozinka, String uloga, boolean verifikovan) {
		super();
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.grad = grad;
		this.drzava = drzava;
		this.titula = titula;
		this.recenzent = recenzent;
		this.username = username;
		this.lozinka = lozinka;
		this.uloga = uloga;
		this.verifikovan = verifikovan;
	}

	public Set<Magazin> getMagaziniUrednik() {
		return magaziniUrednik;
	}

	public void setMagaziniUrednik(Set<Magazin> magaziniUrednik) {
		this.magaziniUrednik = magaziniUrednik;
	}

	public Set<Magazin> getMagaziniRecenzent() {
		return magaziniRecenzent;
	}

	public void setMagaziniRecenzent(Set<Magazin> magaziniRecenzent) {
		this.magaziniRecenzent = magaziniRecenzent;
	}

	public Set<Magazin> getMagaziniGlavniUrednik() {
		return magaziniGlavniUrednik;
	}

	public void setMagaziniGlavniUrednik(Set<Magazin> magaziniGlavniUrednik) {
		this.magaziniGlavniUrednik = magaziniGlavniUrednik;
	}
	

	
}
