package com.example.naucna.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.naucna.model.Magazin;

@Service
public interface MagazinServiceImp {
	public Magazin saveMagazin(Magazin magazin);
	public Magazin findOneByIssn(Long issn);
	public void deleteMagazin(Magazin m);
	public List<Magazin> getAll();
	public Magazin findOneById(Long id);
}
