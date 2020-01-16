package com.example.naucna.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.Magazin;
import com.example.naucna.repositories.MagazinRepository;


@Service
public class MagazinService implements MagazinServiceImp{
	@Autowired
	private MagazinRepository repozitorijum;

	@Override
	public Magazin saveMagazin(Magazin magazin) {
		// TODO Auto-generated method stub
		return repozitorijum.save(magazin);
	}

	@Override
	public Magazin findOneByIssn(Long issn) {
		// TODO Auto-generated method stub
		return repozitorijum.findOneByIssn(issn);
	}

	@Override
	public void deleteMagazin(Magazin m) {
		// TODO Auto-generated method stub
		 repozitorijum.delete(m);
	}

}
