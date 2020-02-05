package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.Magazin;

@Service
public class SlanjeMejlaUrednikuService implements JavaDelegate{

	@Autowired 
	private EMailService emailService;
	@Autowired
	private UserServiceImp service;
	@Autowired
	MagazinService magazinService;
	@Override
	public void execute(DelegateExecution execution) throws Exception {
				
				com.example.naucna.model.User urednik = new com.example.naucna.model.User();
				List<FormSubmissionDto> magazin = (List<FormSubmissionDto>)execution.getVariable("izabraniMagazin");
				Magazin izabraniMagazin = new Magazin();
				  for (FormSubmissionDto formField : magazin) {
					if(formField.getFieldId().equals("casopisi")) {
						Long idMagazina = Long.parseLong(formField.getFieldValue());
						System.out.println("id magazina je "+idMagazina);
						izabraniMagazin = magazinService.findOneById(idMagazina);
						break;
					}	      
				  }
				
				urednik = izabraniMagazin.getGlavniUrednik();
				System.out.println("urednik mejl "+urednik.getEmail());
				emailService.sendNotificaitionUrednik(urednik,execution.getProcessInstanceId());

		
	}

}
