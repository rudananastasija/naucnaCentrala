package com.example.naucna.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.naucna.model.Text;

@Service
public interface TextService {
	Text savetext(Text text);
	public List<Text> getAll();
	Text findById(Long id) ;
	
}
