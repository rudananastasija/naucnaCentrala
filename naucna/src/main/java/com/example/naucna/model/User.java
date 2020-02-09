package com.example.naucna.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements UserDetails{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "ime", nullable = true)
	private String ime;

	@Column(name = "prezime")
	private String prezime;
	
	@Column(name = "email")
	private String email;
	
	@Column(name="grad")
	private String grad;
	
	@Column(name="drzava")
	private String drzava;
	
	@Column(name="titula")
	private String titula;
	
	@Column(name="recenzent")
	private boolean recenzent;
	
	@Column(name = "username")
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
	
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
	        name = "User_Text", 
	        joinColumns = { @JoinColumn(name = "user_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "text_id") }
	  )
	private Set<Text> radoviKoautor = new HashSet<Text>();
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
	        name = "User_Text", 
	        joinColumns = { @JoinColumn(name = "user_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "text_id") }
	  )
	private Set<Text> radoviRecenzent = new HashSet<Text>();

	@ManyToMany(mappedBy = "urednici")
	private Set<Magazin> magaziniUrednik = new HashSet<Magazin>();

	@ManyToMany(mappedBy = "recenzenti")
	private Set<Magazin> magaziniRecenzent = new HashSet<Magazin>();
	
	@OneToMany(mappedBy = "glavniUrednik", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Magazin> magaziniGlavniUrednik = new HashSet<Magazin>();

	@OneToMany(mappedBy = "autor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Text> radoviAutor = new HashSet<Text>();

	
	@ManyToMany(cascade =  {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	    @JoinTable(name = "user_roles",
	            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
	            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	protected List<Role> roles = new ArrayList<>();

	   
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		  // uvek ima samo jednu rolu - uzmi privilegije i vrati
        if(!this.roles.isEmpty()){
            Role r = roles.iterator().next();
            List<Privilege> privileges = new ArrayList<Privilege>();
            for(Privilege p : r.getPrivileges()){
                privileges.add(p);
            }
            return privileges;
        }
        return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.lozinka;
	}

	@Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Set<Text> getRadoviKoautor() {
		return radoviKoautor;
	}

	public void setRadoviKoautor(Set<Text> radoviKoautor) {
		this.radoviKoautor = radoviKoautor;
	}

	public Set<Text> getRadoviAutor() {
		return radoviAutor;
	}

	public void setRadoviAutor(Set<Text> radoviAutor) {
		this.radoviAutor = radoviAutor;
	}

	public Set<Text> getRadoviRecenzent() {
		return radoviRecenzent;
	}

	public void setRadoviRecenzent(Set<Text> radoviRecenzent) {
		this.radoviRecenzent = radoviRecenzent;
	}
	

	
}
