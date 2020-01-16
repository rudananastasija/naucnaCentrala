package com.example.naucna.services;

import org.springframework.stereotype.Service;

import com.example.naucna.model.Magazin;

@Service
public interface MagazinServiceImp {
	public Magazin saveMagazin(Magazin magazin);
	public Magazin findOneByIssn(Long issn);
	public void deleteMagazin(Magazin m);
}
