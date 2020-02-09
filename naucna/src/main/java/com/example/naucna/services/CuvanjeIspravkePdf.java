package com.example.naucna.services;

import java.util.List;

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

public class CuvanjeIspravkePdf implements JavaDelegate{
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
		List<FormSubmissionDto> radForm = (List<FormSubmissionDto>)execution.getVariable("ispravljenRad");
		String textId = (String)execution.getVariable("textId");
		Text text = textService.findById(Long.parseLong(textId));
		System.out.println("dosao da updatuje u handleru text");
	   for (FormSubmissionDto formField : radForm) {
			if(formField.getFieldId().equals("pdfIspravke")) {
				System.out.println("novi fajl je"+formField.getFieldValue());

				text.setFajl(formField.getFieldValue());
			
			}
			
		  }
		Text sacuvanText = textService.savetext(text);
		execution.setVariable("pdfFajl", sacuvanText.getFajl());
	    System.out.println("sacuvao tekst");

		
	}
}
