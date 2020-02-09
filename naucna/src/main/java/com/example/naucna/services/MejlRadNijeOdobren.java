package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.Magazin;
import com.example.naucna.model.Text;


@Service
public class MejlRadNijeOdobren implements JavaDelegate{
	@Autowired
	IdentityService identityService;
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	 TextService textService;
	@Autowired 
	private EMailService emailService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		String rad = (String)execution.getVariable("textId");
		Text text = textService.findById(Long.parseLong(rad));
		
		System.out.println("urednik mejl "+text.getAutor().getEmail());
		emailService.sendNotificaitionTemaNeodobrena(text,execution.getProcessInstanceId());

	}
}
