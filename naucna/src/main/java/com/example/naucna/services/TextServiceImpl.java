package com.example.naucna.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.Text;
import com.example.naucna.repositories.TextRepository;

@Service
public class TextServiceImpl implements TextService{
	@Autowired
	 TextRepository textRepository;

	@Override
	public Text savetext(Text text) {
		// TODO Auto-generated method stub
		System.out.println(" sacuvao je tekst");
		return textRepository.save(text);
	}


}
