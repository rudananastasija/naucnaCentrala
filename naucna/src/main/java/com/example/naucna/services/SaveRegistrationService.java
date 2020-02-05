package com.example.naucna.services;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.model.Role;


@Service
public class SaveRegistrationService implements JavaDelegate{

	@Autowired
	IdentityService identityService;

	@Autowired
	UserService userService;

	@Autowired
	NaucnaOblastService naucnaOblastService;
	 
	@Autowired
	RoleService roleService;

	   

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("usao u execute saveregistrationService");
		
		 List<FormSubmissionDto> registration = (List<FormSubmissionDto>)execution.getVariable("registration");
	      User user = identityService.newUser("korisnickoIme");
	      com.example.naucna.model.User korisnik = new com.example.naucna.model.User();
	      for (FormSubmissionDto formField : registration) {
			if(formField.getFieldId().equals("korisnickoIme")) {
				user.setId(formField.getFieldValue());
				korisnik.setUsername(formField.getFieldValue());
				
			}
			if(formField.getFieldId().equals("lozinka")) {
				String salt = BCrypt.gensalt();
	            String enkriptnovanaSifra = BCrypt.hashpw(formField.getFieldValue(), salt);   
				System.out.println("enkriptovana sifra " + enkriptnovanaSifra);
	            user.setPassword(enkriptnovanaSifra);
				
				korisnik.setLozinka(enkriptnovanaSifra);
			}
			if(formField.getFieldId().equals("ime")) {
				user.setFirstName(formField.getFieldValue());
				korisnik.setIme(formField.getFieldValue());
				
			}
			if(formField.getFieldId().equals("prezime")) {
				user.setLastName(formField.getFieldValue());
				korisnik.setPrezime(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("email")) {
				user.setEmail(formField.getFieldValue());
				korisnik.setEmail(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("grad")) {
				korisnik.setGrad(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("drzava")) {
				korisnik.setDrzava(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("titula")) {
				korisnik.setTitula(formField.getFieldValue());
			}
			if(formField.getFieldId().equals("recenzent")) {
				System.out.println("recenzent "+formField.getFieldValue());
				
				if(formField.getFieldValue().equals("true")) {
					korisnik.setRecenzent(true);
					System.out.println(" recenzent ");
				}else {
					korisnik.setRecenzent(false);
					System.out.println(" recenzent false ");
				
				}
			}
			
			if(formField.getFieldId().equals("oblasti")) {
				System.out.println("usao u oblasti");
				System.out.println(formField.getOblasti());
				List<String> oblastiId = formField.getOblasti();
				for(String s : oblastiId) {
					Long id = Long.parseLong(s);
					NaucnaOblast oblast = naucnaOblastService.findById(id);
					korisnik.getNaucneOblasti().add(oblast);
					
				}
			}
	      }
	      Role role = roleService.findOneByName("ROLE_USER");
	      korisnik.getRoles().add(role);
	      korisnik.setVerifikovan(false);
	      identityService.saveUser(user);
	      userService.saveUser(korisnik);
	}
	
	
	
}
