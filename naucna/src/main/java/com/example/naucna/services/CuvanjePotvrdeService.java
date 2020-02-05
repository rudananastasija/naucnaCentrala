package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.Role;

@Service
public class CuvanjePotvrdeService implements JavaDelegate{

	
	@Autowired
	IdentityService identityService;
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;

	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("stigao u execute od cuvanje potrvrde da je korisnik recenzent");
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
					   Role oldRole = roleService.findOneByName("ROLE_USER");
					      
					   	Role role = roleService.findOneByName("ROLE_RECENZENT");
					   	
					    korisnik.getRoles().remove(oldRole); 
					   	korisnik.getRoles().add(role);
				    	korisnik.setRecenzent(true);

						   System.out.println("sacuvao recenzenta!");
				   }else {
				    	  korisnik.setRecenzent(false);
				   }
				   userService.saveUser(korisnik);
					
			
			}	
	   
	      				
	}

}
