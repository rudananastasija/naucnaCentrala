package com.example.naucna.services;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.Magazin;
import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.model.Role;
import com.example.naucna.model.Text;
import com.example.naucna.model.User;
import java.util.Random;
import java.util.Set; 


@Service
public class SistemBiraUrednikaService implements JavaDelegate{
	@Autowired
	private UserServiceImp service;
	@Autowired
	private TextService textService;
	@Autowired
	NaucnaOblastService naucnaOblastService;

	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		String textId = (String)execution.getVariable("textId");
		//urednici tog casopisa za oblast koja je u radu
		Text text = textService.findById(Long.parseLong(textId));
		
		Magazin casopis = text.getMagazin();
		
		Set<User> urednici = casopis.getUrednici();
		List<User> lista = new ArrayList<User>();
		for(User u :urednici) {
			boolean flag = false;
				
				for(NaucnaOblast no :u.getNaucneOblasti()) {
					if(no.getName().equals(text.getOblast().getName())) {
						flag = true;
						break;
					}
					
				}
			if(flag) {
				lista.add(u);
			}
		}
		if(lista.size() == 0) {
			System.out.println("nema urednika za naucnu oblast");
	        execution.setVariable("izabraniUrednik", "nema");
		}else {
			int size = lista.size();
			Random rand = new Random(); 
	        int rand_int1 = rand.nextInt(size); 
	        User izabraniUrednik = lista.get(rand_int1);
	        System.out.println("uzabran je urednik "+izabraniUrednik.getUsername());
	        execution.setVariable("izabraniUrednik", izabraniUrednik.getId().toString());
	        
		}
		
	}

}
