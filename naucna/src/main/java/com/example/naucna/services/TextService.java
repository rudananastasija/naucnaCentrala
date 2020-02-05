package com.example.naucna.services;

import org.springframework.stereotype.Service;

import com.example.naucna.model.Text;

@Service
public interface TextService {
	Text savetext(Text text);
	
}
