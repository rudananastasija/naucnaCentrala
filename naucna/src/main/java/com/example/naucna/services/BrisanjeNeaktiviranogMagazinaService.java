package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.Magazin;
import com.example.naucna.model.NaucnaOblast;
@Service
public class BrisanjeNeaktiviranogMagazinaService implements JavaDelegate{
	@Autowired
	IdentityService identityService;

		

	@Autowired
	MagazinService magazinService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("usao u execute brisanje magazina");
		
		 List<FormSubmissionDto> magazinForm = (List<FormSubmissionDto>)execution.getVariable("magazin");
	      Magazin magazin = new Magazin();
		 for (FormSubmissionDto formField : magazinForm) {
			
			if(formField.getFieldId().equals("ISSN")) {
				magazin = magazinService.findOneByIssn(Long.parseLong(formField.getFieldValue()));
				break;
			}
			
		  }
		 if(magazin != null) {
			 magazinService.deleteMagazin(magazin);
		 }
	}

}
