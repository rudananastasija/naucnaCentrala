package com.example.naucna.services;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.Magazin;
import com.example.naucna.model.NaucnaOblast;

@Service
public class CuvanjeMagazina implements JavaDelegate{
	@Autowired
	IdentityService identityService;

	@Autowired
	UserService userService;

	@Autowired
	NaucnaOblastService naucnaOblastService;

	@Autowired
	MagazinService magazinService;
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		 List<FormSubmissionDto> magazinForm = (List<FormSubmissionDto>)execution.getVariable("magazin");
	     Magazin newMagazin = new Magazin();
		 for (FormSubmissionDto formField : magazinForm) {
			if(formField.getFieldId().equals("naziv")) {
				newMagazin.setName(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("ISSN")) {
				newMagazin.setIssn(Long.parseLong(formField.getFieldValue()));
				
			}
			if(formField.getFieldId().equals("nacinPlacanja")) {
				if(formField.getFieldValue().equals("true")) {
					newMagazin.setPlacanje(true);
				}else {
					newMagazin.setPlacanje(false);
					
				}
				
			}
			
			
			if(formField.getFieldId().equals("oblasti")) {
				System.out.println("usao u oblasti");
				System.out.println(formField.getOblasti());
				List<String> oblastiId = formField.getOblasti();
				for(String s : oblastiId) {
					Long id = Long.parseLong(s);
					NaucnaOblast oblast = naucnaOblastService.findById(id);
					newMagazin.getNaucneOblastiMagazin().add(oblast);
					
				}
			}
			magazinService.saveMagazin(newMagazin);
	      }
	}

}
