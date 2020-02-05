package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.Magazin;
import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.model.Role;
import com.example.naucna.model.Text;
@Service

public class ObradaTexta1Service implements JavaDelegate{
	@Autowired
	private MagazinServiceImp service;

	@Autowired
	private TextService textService;
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("usao u execute obrada texta 1 service");
			
		  List<FormSubmissionDto> magazin = (List<FormSubmissionDto>)execution.getVariable("izabraniMagazin");
		  Magazin izabraniMagazin = new Magazin();
		  for (FormSubmissionDto formField : magazin) {
			if(formField.getFieldId().equals("casopisi")) {
				Long idMagazina = Long.parseLong(formField.getFieldValue());
				
				izabraniMagazin = service.findOneById(idMagazina);
				break;
			}	      
		  }
		  if(izabraniMagazin.isPlacanje()) {
			  execution.setVariable("access", true);
		  }else {
			  execution.setVariable("access", false);
				
		  }
		  //newText.setMagazin(izabraniMagazin);
		 // execution.setVariable(processInstanceId, "magazin", dto);
			
	}

}
