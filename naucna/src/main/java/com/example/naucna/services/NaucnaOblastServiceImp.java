package com.example.naucna.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.naucna.model.NaucnaOblast;

@Service
public interface NaucnaOblastServiceImp {
	NaucnaOblast saveNaucnaOblast(NaucnaOblast oblast);
	public List<NaucnaOblast> getAll();
	public NaucnaOblast findById(Long id);
}
