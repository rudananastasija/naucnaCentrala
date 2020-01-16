package com.example.naucna.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.repositories.NaucnaOblastRepository;

@Service
public class NaucnaOblastService implements NaucnaOblastServiceImp{
	@Autowired
	private NaucnaOblastRepository repozitorijum;

	@Override
	public NaucnaOblast saveNaucnaOblast(NaucnaOblast oblast) {
		// TODO Auto-generated method stub
		return repozitorijum.save(oblast);
	}

	@Override
	public List<NaucnaOblast> getAll() {
		// TODO Auto-generated method stub
		return repozitorijum.findAll();
	}

	@Override
	public NaucnaOblast findById(Long id) {
		// TODO Auto-generated method stub
		return repozitorijum.getOne(id);
	}

	
	
}
