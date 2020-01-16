package com.example.naucna.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.model.NaucnaOblastDto;
import com.example.naucna.services.NaucnaOblastServiceImp;

@RestController
@RequestMapping(value = "/naucneOblasti")
@CrossOrigin(origins = "http://localhost:4200")
public class NaucnaOblastController {
	@Autowired
	private NaucnaOblastServiceImp service;
	
	@RequestMapping(value="/getAll", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll(){		
		List<NaucnaOblast> naucneOblasti = service.getAll();
		List<NaucnaOblastDto> naucneOblastiDto = new ArrayList<NaucnaOblastDto>();
		
		for(NaucnaOblast no:naucneOblasti) {
			NaucnaOblastDto oblast = new NaucnaOblastDto(no.getName());
			oblast.setId(no.getId());
			naucneOblastiDto.add(oblast);
		}
		return new ResponseEntity<List<NaucnaOblastDto>>(naucneOblastiDto, HttpStatus.OK);
	}
}
