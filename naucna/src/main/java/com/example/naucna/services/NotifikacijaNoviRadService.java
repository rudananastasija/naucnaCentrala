package com.example.naucna.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.Text;
import com.example.naucna.model.User;


@Service
public class NotifikacijaNoviRadService implements JavaDelegate{
	@Autowired 
	private EMailService emailService;
	@Autowired
	private UserServiceImp service;
	@Autowired
	MagazinService magazinService;
	@Autowired
	private TextService textService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		String urednikId = (String)execution.getVariable("izabraniUrednik");
		if(urednikId.equals("nema")) {
			String textId = (String)execution.getVariable("textId");
			//urednici tog casopisa za oblast koja je u radu
			Text text = textService.findById(Long.parseLong(textId));
			System.out.println("Slanje mejla glavnom uredniku");
			execution.setVariable("odabraniUrednik", text.getMagazin().getGlavniUrednik().getUsername());
	        
			emailService.sendNoviRadUrednik(text.getMagazin().getGlavniUrednik(),execution.getProcessInstanceId());
			
		}else {
			User urednik = service.findById(Long.parseLong(urednikId));
			System.out.println("Slanje mejla  uredniku");
			execution.setVariable("odabraniUrednik", urednik.getUsername());
	        
			emailService.sendNoviRadUrednik(urednik,execution.getProcessInstanceId());

		}
		
	}
}
