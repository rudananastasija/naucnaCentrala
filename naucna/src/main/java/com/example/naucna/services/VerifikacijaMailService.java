package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
@Service
public class VerifikacijaMailService implements JavaDelegate{

	@Autowired 
	private EMailService emailService;
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		com.example.naucna.model.User korisnik = new com.example.naucna.model.User();
		 List<FormSubmissionDto> registration = (List<FormSubmissionDto>)execution.getVariable("registration");
		    for(FormSubmissionDto form : registration) {
		    	if(form.getFieldId().equals("email")) {
		    		korisnik.setEmail(form.getFieldValue());
		    	}
		    	if(form.getFieldId().equals("ime")) {
		    		korisnik.setIme(form.getFieldValue());
				 
		    	}
		    	if(form.getFieldId().equals("korisnickoIme")) {
		    		korisnik.setUsername(form.getFieldValue());
		    	}
		    }
		emailService.sendNotificaitionAsync(korisnik,execution.getProcessInstanceId());
		
	}

}
