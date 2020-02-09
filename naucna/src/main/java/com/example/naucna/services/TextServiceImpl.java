package com.example.naucna.services;

import java.util.List;

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

	@Override
	public List<Text> getAll() {
		// TODO Auto-generated method stub
		return textRepository.findAll();
	}

	@Override
	public Text findById(Long id) {
		// TODO Auto-generated method stub
		return textRepository.findOneById(id);
	}


}
