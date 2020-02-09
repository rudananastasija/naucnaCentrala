package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.KljucnaRijec;
import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.model.Text;
import com.example.naucna.model.User;

@Service
public class CuvanjePodatakaKoautorService implements JavaDelegate{
	@Autowired
	IdentityService identityService;
	@Autowired
	UserService userService;
	
	@Autowired
	private TextService textService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("dosao u cuvanje koautor data");
		List<FormSubmissionDto> koautorForm = (List<FormSubmissionDto>)execution.getVariable("koautorData");

		User koautor = new User();
		for (FormSubmissionDto formField : koautorForm) {
			if(formField.getFieldId().equals("imeKoautor")) {
	
				koautor.setIme(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("GradKoautor")) {
	
				koautor.setGrad(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("DrzavaKoautor")) {
				
				koautor.setDrzava(formField.getFieldValue());
			}
	  }
		
		List<FormSubmissionDto> email = (List<FormSubmissionDto>)execution.getVariable("koautorEmail");
		for (FormSubmissionDto formField : email) {
			if(formField.getFieldId().equals("emailK")) {
				System.out.println("email u execution je "+formField.getFieldValue());
				koautor.setEmail(formField.getFieldValue());
				break;
			}
			
		  }
		User sacuvaniKoautor = userService.saveUser(koautor);
		String rad = (String)execution.getVariable("textId");
		Text text = textService.findById(Long.parseLong(rad));
		text.getKoautori().add(sacuvaniKoautor);
		textService.savetext(text);
		System.out.println("sacuvao text i koautora ");
		
		
	}

}
