package com.example.naucna.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.Text;
import com.example.naucna.model.User;
import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.KljucnaRijec;
import com.example.naucna.model.Magazin;
import com.example.naucna.model.NaucnaOblast;

@Service
public class CuvanjeRadaService implements JavaDelegate{

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
		List<FormSubmissionDto> radForm = (List<FormSubmissionDto>)execution.getVariable("rad");
		
		String autorUsername = (String)execution.getVariable("autor");
		User autor = service.findUserByUsername(autorUsername);
		
	     Text noviText= new Text();
		for (FormSubmissionDto formField : radForm) {
			System.out.println("usao u petlju");
			if(formField.getFieldId().equals("naslov")) {
	
				noviText.setNaslov(formField.getFieldValue());
				System.out.println(" naslo v je "+formField.getFieldValue());
			}
			if(formField.getFieldId().equals("apstrakt")) {
				noviText.setRezime(formField.getFieldValue());
 				System.out.println(" naslo apse "+formField.getFieldValue());
					
			}
			
			if(formField.getFieldId().equals("pdfFajl")) {
				noviText.setFajl(formField.getFieldValue());
				System.out.println(" naslo apse "+formField.getFieldValue());
					
			}
			if(formField.getFieldId().equals("kljucneRijeci")) {
				String[] nizRijeci = formField.getFieldValue().split(",");
				for(int i = 0;i<nizRijeci.length;i++) {
					KljucnaRijec kljr = new KljucnaRijec();
					kljr.setRijec(nizRijeci[i]);
					kljr.setText(noviText);
					noviText.getKljucneRijeci().add(kljr);
			
				}
				
			}
			
			if(formField.getFieldId().equals("naucnaOblast")) {
				Long idOblasti = Long.parseLong(formField.getFieldValue());
				System.out.println(" naucna oblas "+formField.getFieldValue());
				
				NaucnaOblast oblast = naucnaOblastService.findById(idOblasti);
				noviText.setOblast(oblast);
				
			}
		  }
		noviText.setAutor(autor);
		noviText.setMagazin(izabraniMagazin);
		Text sacuvanText = textService.savetext(noviText);
		execution.setVariable("textId", sacuvanText.getId().toString());
	    System.out.println("sacuvao tekst");
	}

}
