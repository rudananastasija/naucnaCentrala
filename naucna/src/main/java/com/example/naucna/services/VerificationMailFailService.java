package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.naucna.model.FormSubmissionDto;

public class VerificationMailFailService implements JavaDelegate{

	@Autowired
	UserService userService;

	@Autowired
	IdentityService identityService;


	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		com.example.naucna.model.User korisnik = new com.example.naucna.model.User();
		 List<FormSubmissionDto> registration = (List<FormSubmissionDto>)execution.getVariable("registration");
		for(FormSubmissionDto form : registration) {
	    	if(form.getFieldId().equals("email")) {
	    		korisnik = userService.findUserByEmail(form.getFieldValue());
	    	}
	    
	    }
		if(korisnik != null) {
			userService.deleteUser(korisnik);
			identityService.deleteUser(korisnik.getId().toString());
			System.out.println("Obrisao korisnika");
		
		}
		
	}

}
