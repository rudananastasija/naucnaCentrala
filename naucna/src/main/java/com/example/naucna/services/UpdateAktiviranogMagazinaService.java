package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.Magazin;
import com.example.naucna.model.NaucnaOblast;

@Service
public class UpdateAktiviranogMagazinaService implements JavaDelegate{

	@Autowired
	MagazinService magazinService;
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		List<FormSubmissionDto> magazinForm = (List<FormSubmissionDto>)execution.getVariable("magazin");
			System.out.println("dosao da sacuva aktivirani magazin");
			
			
	      Magazin magazin = new Magazin();
			 for (FormSubmissionDto formField : magazinForm) {
				
				if(formField.getFieldId().equals("ISSN")) {
					magazin = magazinService.findOneByIssn(Long.parseLong(formField.getFieldValue()));
					break;
				}
				
			  }
			 if(magazin != null) {
				 List<FormSubmissionDto> updateMagazin = (List<FormSubmissionDto>)execution.getVariable("aktivacijaMagazina");
			      for(FormSubmissionDto form : updateMagazin) {
				    	if(form.getFieldId().equals("aktivirano")) {
				    		if(form.getFieldValue().equals("true")) {
				    			magazin.setAktiviran(true);
				    		}else {
				    			magazin.setAktiviran(false);
				    		}
				    	
				    	}
				    
				    }
			      magazinService.saveMagazin(magazin);
			 
			 }


	}

}
