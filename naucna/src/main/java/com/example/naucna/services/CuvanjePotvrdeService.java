package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.naucna.model.FormSubmissionDto;

@Service
public class CuvanjePotvrdeService implements JavaDelegate{

	
	@Autowired
	IdentityService identityService;
	@Autowired
	UserService userService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("stigao u execute od cuvanje potrvrde");
		 boolean potvrdaFlag = (boolean)execution.getVariable("potvrda");
	      com.example.naucna.model.User korisnik = new com.example.naucna.model.User();
	      List<FormSubmissionDto> registration = (List<FormSubmissionDto>)execution.getVariable("registration");
	      for(FormSubmissionDto form : registration) {
		    	if(form.getFieldId().equals("email")) {
		    		korisnik = userService.findUserByEmail(form.getFieldValue());
		    	}
		    
		    }
			if(korisnik != null) {
			
				   if(potvrdaFlag) {
				    	  korisnik.setUloga("REC");
				    	  korisnik.setRecenzent(true);
				   }else {
				    	  korisnik.setUloga("KORISNIK");
				    	  korisnik.setRecenzent(false);
				      }
				   userService.saveUser(korisnik);
					
				   System.out.println("Izmjenio korisnika");
			
			}	
	   
	      				
	}

}
