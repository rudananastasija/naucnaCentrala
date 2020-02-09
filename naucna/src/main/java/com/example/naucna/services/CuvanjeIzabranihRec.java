package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.Magazin;
import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.model.Text;
import com.example.naucna.model.User;

@Service
public class CuvanjeIzabranihRec  implements JavaDelegate{
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
		// TODO Auto-generated method stub
		
		 List<FormSubmissionDto> izabraniRecenzentiForm = (List<FormSubmissionDto>)execution.getVariable("izabraniRecenzentiForm");
		 boolean postojeRec = (boolean)execution.getVariable("imaRec");
		 String textId = (String)execution.getVariable("textId");
		 Text text = textService.findById(Long.parseLong(textId));
			
		    
		 for (FormSubmissionDto formField : izabraniRecenzentiForm) {
			
			if(formField.getFieldId().equals("oblasti")) {
				System.out.println("usao da sacuva recenzente");
				
				List<String> oblastiId = formField.getOblasti();
				for(String s : oblastiId) {
					Long id = Long.parseLong(s);
					User recenzent = service.findById(id);
					text.getRecenzenti().add(recenzent);
				}
			}
	      }
		 textService.savetext(text);
		 System.out.println("uspjesno je sacuvao text nakon dodavanja recenzenata");
	}

}
