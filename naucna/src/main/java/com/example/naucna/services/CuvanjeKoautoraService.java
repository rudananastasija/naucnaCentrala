package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.KljucnaRijec;
import com.example.naucna.model.Magazin;
import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.model.Text;
import com.example.naucna.model.User;

@Service
public class CuvanjeKoautoraService implements JavaDelegate{
	@Autowired
	MagazinService magazinService;
	@Autowired
	private TextService textService;
	@Autowired
	NaucnaOblastService naucnaOblastService;
	@Autowired
	private UserServiceImp service;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		List<FormSubmissionDto> email = (List<FormSubmissionDto>)execution.getVariable("koautorEmail");
		User koautor = null;
		String rad = (String)execution.getVariable("textId");
		Text text = textService.findById(Long.parseLong(rad));
		
	    Text noviText= new Text();
		for (FormSubmissionDto formField : email) {
			if(formField.getFieldId().equals("emailK")) {
				koautor = service.findUserByUsername(formField.getFieldValue());
				break;
			}
			
		  }
		if(koautor != null) {
			text.getKoautori().add(koautor);
		}
		textService.savetext(text);
		System.out.println("update texta dodavanje koautora");
		
	}
}
