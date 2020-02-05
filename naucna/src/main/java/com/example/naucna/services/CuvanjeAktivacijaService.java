package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;

@Service
public class CuvanjeAktivacijaService implements JavaDelegate{
	@Autowired
	UserService userService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("stigao u execute od cuvanje aktivacije naloga");
		 boolean potvrdaFlag = (boolean)execution.getVariable("aktivacija");
	      com.example.naucna.model.User korisnik = new com.example.naucna.model.User();
	      List<FormSubmissionDto> registration = (List<FormSubmissionDto>)execution.getVariable("registration");
	      for(FormSubmissionDto form : registration) {
		    	if(form.getFieldId().equals("email")) {
		    		korisnik = userService.findUserByEmail(form.getFieldValue());
		    	}
		    
		    }
			if(korisnik != null) {
			
				   if(potvrdaFlag) {
					   korisnik.setVerifikovan(true);
					   userService.saveUser(korisnik);
						        
				      }
				   System.out.println("Izmjenio korisnika");
				}	
	   
	      				
	
	}

}
