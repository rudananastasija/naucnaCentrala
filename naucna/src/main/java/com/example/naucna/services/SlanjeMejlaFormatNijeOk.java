package com.example.naucna.services;
import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.Magazin;
import com.example.naucna.security.TokenUtils;
@Service
public class SlanjeMejlaFormatNijeOk implements JavaDelegate{
	@Autowired 
	private EMailService emailService;
	@Autowired
	private UserServiceImp service;
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		com.example.naucna.model.User korisnik = new com.example.naucna.model.User();
		String autorUsername = (String)execution.getVariable("autor");
		korisnik = service.findUserByUsername(autorUsername);
		emailService.sendNotificaitionAutorNijeDobarSadrzaj(korisnik,execution.getProcessInstanceId());
		
		
	}

}
