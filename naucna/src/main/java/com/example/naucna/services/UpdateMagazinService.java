package com.example.naucna.services;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.Magazin;
import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.model.User;

@Service
public class UpdateMagazinService implements JavaDelegate{

	@Autowired
	IdentityService identityService;
	@Autowired
	MagazinService magazinService;
	@Autowired
	UserService userService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("usao u execute update magazina");
		 List<FormSubmissionDto> magazinForm = (List<FormSubmissionDto>)execution.getVariable("magazin");
	     
		 Magazin magazin = new Magazin();
	     
	     for (FormSubmissionDto formField : magazinForm) {
			
				if(formField.getFieldId().equals("ISSN")) {
					magazin = magazinService.findOneByIssn(Long.parseLong(formField.getFieldValue()));
					break;
				}
			}
	     System.out.println("");
	     

		 List<FormSubmissionDto> updateForm = (List<FormSubmissionDto>)execution.getVariable("updateMagazin");
		 if(updateForm == null) {
	    	 System.out.println("updateForm je null");
	     }
		 if(magazin != null) {
			  for (FormSubmissionDto formField : updateForm) {

					if(formField.getFieldId().equals("urednici")) {
						System.out.println("usao u urednike");
						System.out.println(formField.getUrednici().size());
						List<String> uredniciId = formField.getUrednici();
						for(String s : uredniciId) {
							Long id = Long.parseLong(s);
							User user =  userService.findById(id);
							magazin.getUrednici().add(user);
						}
					}
					
					if(formField.getFieldId().equals("recenzenti")) {
						System.out.println("usao u recenzente");
						System.out.println(formField.getRecenzenti().size());
						List<String> recId = formField.getRecenzenti();
						for(String s : recId) {
							Long id = Long.parseLong(s);
							User user =  userService.findById(id);
							magazin.getRecenzenti().add(user);
						}
					}
			  }
		}
		  

	      magazinService.saveMagazin(magazin);

			System.out.println("sacuvao magazin");		
	}

}
