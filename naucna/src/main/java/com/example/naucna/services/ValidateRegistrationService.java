package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;


@Service
public class ValidateRegistrationService implements JavaDelegate{

	@Autowired
	IdentityService identityService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("U validaciji je");
	      List<FormSubmissionDto> registration = (List<FormSubmissionDto>)execution.getVariable("registration");
	      System.out.println(registration);
	      boolean flag = false;
	      for(int i = 0;i<registration.size();i++) {
				if(!registration.get(i).getFieldId().equals("titula") && !registration.get(i).getFieldId().equals("recenzent")&&!registration.get(i).getFieldId().equals("oblasti")) {
					if(registration.get(i).getFieldValue()  == null || registration.get(i).getFieldValue().isEmpty()) {
						flag = true;
					}
				}
				 if(registration.get(i).getFieldId().equals("oblasti")) {
						if(registration.get(i).getOblasti().size() == 0) {
							flag = true;
							System.out.println("Prazne naucne oblasti");
							
						}
					}
	      } 
	      
	     
		if(flag) {
			execution.setVariable("result", true);
			System.out.println("nije ok");
		}else {
			execution.setVariable("result", false);
			System.out.println("ok");
		}
	}
}
