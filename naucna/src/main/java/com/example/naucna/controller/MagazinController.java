package com.example.naucna.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.impl.form.type.StringFormType;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.naucna.model.FormFieldsDto;
import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.KljucnaRijec;
import com.example.naucna.model.Magazin;
import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.model.Role;
import com.example.naucna.model.Text;
import com.example.naucna.model.User;
import com.example.naucna.security.TokenUtils;
import com.example.naucna.services.EMailService;
import com.example.naucna.services.MagazinService;
import com.example.naucna.services.MagazinServiceImp;
import com.example.naucna.services.NaucnaOblastService;
import com.example.naucna.services.TextService;
import com.example.naucna.services.UserServiceImp;
import com.example.naucna.utils.Utils;



@RestController
@RequestMapping(value = "/magazin")
@CrossOrigin(origins = "http://localhost:4200")

public class MagazinController {

	@Autowired
	private MagazinServiceImp service;
	@Autowired
	private NaucnaOblastService serviceNaucnaOblast;

	@Autowired
	IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	

	@Autowired
	TaskService taskService;
	
	@Autowired
	FormService formService;
	@Autowired
	private UserServiceImp userService;
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	MagazinService magazinService;
	@Autowired
	TextService textService;

	
	//startovanje processa za kreiranje magazina
	@GetMapping(path = "/get", produces = "application/json")
	@PreAuthorize("hasAuthority('KREIRANJE_CASOPISA')")    
    public @ResponseBody FormFieldsDto get(@Context HttpServletRequest request) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("procesCasopisID");
	    
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		//task.setAssignee(username);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		
		return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }
		
	
	   //startovanje procesa za obradu novog teksta
		 @GetMapping(path = "/getFormaObradaTeksta", produces = "application/json")
		public @ResponseBody FormFieldsDto getFormaObradaTeksta(@Context HttpServletRequest request) {
			System.out.println("dosao po magazine za izbor u obradi teksta"); 
			String username = Utils.getUsernameFromRequest(request, tokenUtils);
			ProcessInstance pi = runtimeService.startProcessInstanceByKey("procesObradaId");
			runtimeService.setVariable(pi.getProcessInstanceId(),"activator", username);
			System.out.println("dosao da startuje proces obrada text "+username);
			
			Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
			task.setAssignee(username);
			taskService.saveTask(task);
			
			TaskFormData tfd = formService.getTaskFormData(task.getId());
			
			List<FormField> properties = tfd.getFormFields();
			List<Magazin> casopisi = service.getAll();
			
			for(FormField field : properties){
	            if(field.getId().equals("casopisi")){
	                EnumFormType enumType = (EnumFormType) field.getType();
	                enumType.getValues().clear();
	                for(Magazin magazin: casopisi){
	                    enumType.getValues().put(magazin.getId().toString(), magazin.getName());
	                }
	                break;
	            }
	        }
			return new FormFieldsDto(task.getId(), pi.getId(), properties);
	    }
		 
		 
		
		 
	@PostMapping(path = "/dodajMagazin/{taskId}", produces = "application/json"	)
	public @ResponseBody ResponseEntity dodajMagazin(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId, HttpServletRequest request) {
		System.out.println("U potvrdi magazin je");
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String username = Utils.getUsernameFromRequest(request, tokenUtils);
        runtimeService.setVariable(processInstanceId, "glavniUrednik", username);

		for(int i = 0;i<dto.size();i++) {
				if(dto.get(i).getFieldId() == "naziv") {
					if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
						System.out.println("prazan naziv");
						
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}
				}
				
				if(dto.get(i).getFieldId() == "ISSN") {
					if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
						System.out.println("prazan issn");
						
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}else {
						Magazin magazinCheck = service.findOneByIssn(Long.parseLong(dto.get(i).getFieldValue()));
						
						if(magazinCheck != null) {
							return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
							
						}
					}
				}
				
			
			if(dto.get(i).getFieldId().equals("oblasti")) {
				if(dto.get(i).getOblasti().size() == 0) {
					System.out.println("prazno obasti");
					
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					
				}
			}
		}
		try {

			runtimeService.setVariable(processInstanceId, "magazin", dto);
			formService.submitTaskForm(taskId, map);
	        
			
	        return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(FormFieldValidationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
    }
	
	private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmissionDto temp : list){
			map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}
	
	@PostMapping(path = "/sacuvajAktivacijuMagazina/{taskId}", produces = "application/json"	)
    public @ResponseBody ResponseEntity sacuvajAktivacijuMagazina(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		System.out.println("U sacuvaj aktivaciju magazina je");
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		try {

			runtimeService.setVariable(processInstanceId, "aktivacijaMagazina", dto);
			formService.submitTaskForm(taskId, map);
	        return new ResponseEntity<>(HttpStatus.OK);
		}catch(FormFieldValidationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
    }
	
	@PostMapping(path = "/sacuvajIzborMagazina/{taskId}", produces = "application/json"	)
	public @ResponseBody ResponseEntity sacuvajIzborMagazina(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		System.out.println("U sacuvajIzborMagazina"+taskId);
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		System.out.println(" proces instance id"+processInstanceId);
		
		Magazin izabraniMagazin = new Magazin();
		  
		for(int i = 0;i<dto.size();i++) {
				if(dto.get(i).getFieldId().equals("casopisi")) {
					if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
						System.out.println("prazni casopisi");
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}else {
						Long idMagazina = Long.parseLong(dto.get(i).getFieldValue());
						System.out.println("id magazina "+idMagazina);
						izabraniMagazin = service.findOneById(idMagazina);
						break;
						
					}
				}
				
		}
		
		User autor = new User();
		String autorUsername = (String)runtimeService.getVariable(processInstanceId, "activator");
		System.out.println("autorsko username"+autorUsername);
		autor = userService.findUserByUsername(autorUsername);    
		
		boolean podproces = false;
		if(izabraniMagazin == null) {
			System.out.println("null je izabrani magazin");
		}
		 if(izabraniMagazin.isPlacanje()) {
			   
			  if(!autor.isPlaceno()) {
				 podproces = true;	
			  }
			  
		  }
		try {

			runtimeService.setVariable(processInstanceId, "izabraniMagazin", dto);
			formService.submitTaskForm(taskId, map);
	       
	        return new ResponseEntity<>(podproces,HttpStatus.OK);
			
		}catch(FormFieldValidationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
    }
	//metoda vraca formu za unos novog rada
	@GetMapping(path = "/getFormaNoviRad/{task}", produces = "application/json")
	//@PreAuthorize("hasAuthority('POTVRDI_RECENZENT')") 
    public @ResponseBody FormFieldsDto getFormPotvrda(@PathVariable String task) {
		System.out.println("dosao po formu za novi rad");
		Task activeTask = taskService.createTaskQuery().taskId(task).singleResult();
		String processInstanceId = activeTask.getProcessInstanceId();
	
		//Task activeTask = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
		
		TaskFormData tfd = formService.getTaskFormData(activeTask.getId());
		List<FormField> properties = tfd.getFormFields();
		List<NaucnaOblast> oblasti = serviceNaucnaOblast.getAll();
	
		for(FormField field : properties){
            if(field.getId().equals("naucnaOblast")){
                EnumFormType enumType = (EnumFormType) field.getType();
           
                enumType.getValues().clear();
                for(NaucnaOblast oblast: oblasti){
                    enumType.getValues().put(oblast.getId().toString(), oblast.getName());
                }
                break;
            }
        }
        return new FormFieldsDto(activeTask.getId(), processInstanceId, properties);
    }
	@RequestMapping(value="/sacuvajFile/", method= RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON)
	public @ResponseBody ResponseEntity sacuvajFile(@RequestParam("File") MultipartFile fileReq){
		String ret="";
		System.out.println("dosao u funkciju");
		try{
			ret=saveUpload(fileReq);
			System.out.println("usao u try");
		}catch(IOException e){
			e.printStackTrace();
		}
		//ret-apsolutna putanja fajla
		return new ResponseEntity<>(ret, HttpStatus.OK);
	}
	public String saveUpload(MultipartFile file) throws IOException{
		String pack = "folder/";
		byte[] bytes = file.getBytes();
		Path path = Paths.get(pack+file.getOriginalFilename());
		System.out.println(path.toAbsolutePath());
		Files.write(path,bytes);
		System.out.println("ispisao");

		return path.toAbsolutePath().toString();
	}
	@PostMapping(path = "/dodajRad/{taskId}", produces = "application/json"	)
	public @ResponseBody ResponseEntity dodajRad(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId, HttpServletRequest request) {
		System.out.println("U dodaj rad je");
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		String username = Utils.getUsernameFromRequest(request, tokenUtils);

		runtimeService.setVariable(processInstanceId, "autor", username);
		
		for(int i = 0;i<dto.size();i++) {
				if(dto.get(i).getFieldId() == "naziv") {
					if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
						System.out.println("prazan naziv");
						
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}
				}
				
				if(dto.get(i).getFieldId() == "kljucneRijeci") {
					if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
						System.out.println("prazan kljucne rijeci");
						
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}				}
				
				if(dto.get(i).getFieldId() == "apstrakt") {
					if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
						System.out.println("prazan apstrakt");
						
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}				}
					
				if(dto.get(i).getFieldId() == "naucnaOblast") {
					if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
						System.out.println("prazan naucnaOblast");
						
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}				}
			
				if(dto.get(i).getFieldId() == "pdfFajl") {
					if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
						System.out.println("prazan naucnaOblast");

						runtimeService.setVariable(processInstanceId, "pdfFajl", dto.get(i).getFieldValue());
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}				
				}
			
		}
		try {

			runtimeService.setVariable(processInstanceId, "rad", dto);
			
			formService.submitTaskForm(taskId, map);
	        
			
			
			List<FormSubmissionDto> magazin = (List<FormSubmissionDto>)runtimeService.getVariable(processInstanceId,"izabraniMagazin");
			Magazin izabraniMagazin = new Magazin();
			  for (FormSubmissionDto formField : magazin) {
				if(formField.getFieldId().equals("casopisi")) {
					Long idMagazina = Long.parseLong(formField.getFieldValue());
					System.out.println("id magazina je "+idMagazina);
					izabraniMagazin = magazinService.findOneById(idMagazina);
					break;
				}	      
			  }
			
			User urednik = izabraniMagazin.getGlavniUrednik();
			
			List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			for(Task t : taskList) {
				System.out.println(" task "+t.getAssignee()+" ii " +t.getId());
				if(t.getName().equals("Pregled teme rada") || t.getId().equals("Task_0ly1nks")) {
					System.out.println("usao ovdje da postavi assignee");
					t.setAssignee(urednik.getUsername());

					System.out.println("usao ovdje da postavi assignee"+t.getAssignee());
					taskService.saveTask(t);
					break;
				}
			}
	        return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(FormFieldValidationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
    }

	//metoda vraca formu za pregled rada pd strane urednika
		@GetMapping(path = "/getPregledRadaForm/{task}", produces = "application/json")
		//@PreAuthorize("hasAuthority('POTVRDI_RECENZENT')") 
	    public @ResponseBody FormFieldsDto getPregledRadaForm(@PathVariable String task) {
			System.out.println("usao u fu");
			System.out.println("dosao po formu za pregled rada"+task);

			Task pronadjenTask = taskService.createTaskQuery().taskId(task).singleResult();
			String processInstanceId = pronadjenTask.getProcessInstanceId();
		
			TaskFormData tfd = formService.getTaskFormData(pronadjenTask.getId());
			List<FormField> properties = tfd.getFormFields();
			
	        return new FormFieldsDto(pronadjenTask.getId(), processInstanceId, properties);
	    }
		
		//metoda vraca formu za izbor recenzenata
				@GetMapping(path = "/getFormaRecenzenti/{task}", produces = "application/json")
				//@PreAuthorize("hasAuthority('POTVRDI_RECENZENT')") 
			    public @ResponseBody FormFieldsDto getFormaRecenzenti(@PathVariable String task) {
					System.out.println("usao u fu getFormaRecenzenti");

					Task pronadjenTask = taskService.createTaskQuery().taskId(task).singleResult();
					String processInstanceId = pronadjenTask.getProcessInstanceId();
				
					TaskFormData tfd = formService.getTaskFormData(pronadjenTask.getId());
					
					List<FormField> properties = tfd.getFormFields();
					List<FormField> returnProperies = new ArrayList<FormField>();
					String idText = (String)runtimeService.getVariable(processInstanceId,"textId");
					Text text = textService.findById(Long.parseLong(idText));
					
					Set<User> recenzenti = text.getMagazin().getRecenzenti();
					
					List<User> lista = new ArrayList<User>();
					
					for(User u :recenzenti) {
						for(Role rola:u.getRoles()) {
							if(rola.getName().equals("ROLE_RECENZENT")) {
						
								boolean flag = false;
								System.out.println("recenzent je "+u.getUsername());
						
								for(NaucnaOblast no :u.getNaucneOblasti()) {
								
									if(no.getName().equals(text.getOblast().getName())) {
										flag = true;
										break;
									}
								
								}
									if(flag) {
											lista.add(u);
										}
									break;
							}
						}
					}
					//ovdje treba obraditi slucaj da nema dovoljno rec
					if(lista.size() > 1) {
						for(FormField field : properties){
				            if(field.getId().equals("rec")){
				                EnumFormType enumType = (EnumFormType) field.getType();
				                enumType.getValues().clear();
				                for(User u: lista){
				                    enumType.getValues().put(u.getId().toString(), u.getUsername());
				                }
				                break;
				            }
				        }
					}else {
						
						for(FormField field : properties){
				            if(field.getId().equals("rokRecenziranje")){
				                returnProperies.add(field);
				            }
				        }
				        return new FormFieldsDto(pronadjenTask.getId(), processInstanceId, returnProperies);

					}
			        return new FormFieldsDto(pronadjenTask.getId(), processInstanceId, properties);
   }	
		


		@PostMapping(path = "/pregledTeme/{taskId}", produces = "application/json"	)
		public @ResponseBody ResponseEntity pregledTeme(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId, HttpServletRequest request) {
			System.out.println("U pregledTeme je");
			
			HashMap<String, Object> map = this.mapListToDto(dto);
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String assignee = task.getAssignee();
			String processInstanceId = task.getProcessInstanceId();
			boolean flagOdobrena = false;
			String naslovRada = "";
			
			for(int i = 0;i<dto.size();i++) {
					if(dto.get(i).getFieldId().equals("odobrenaId")) {
						if(dto.get(i).getFieldValue().equals("true")) {
							flagOdobrena = true;
						}
						
					}
				}
					
			
			
			try {

				runtimeService.setVariable(processInstanceId, "radOdobrenFlag", flagOdobrena);

				formService.submitTaskForm(taskId, map);
		   
				List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
				System.out.println("ispisuem ttaskove");
				for(Task t : taskList) {
					System.out.println(" task "+t.getAssignee()+" ii " +t.getName());
					if(t.getName().equals("Pregled sadrzaja rada") || t.getId().equals("Task_1b8wqog")) {
						t.setAssignee(assignee);
						taskService.saveTask(t);
						break;
					}
				}
				
				List<Task> taskList2 = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
				System.out.println("ispisuem ttaskove");
				for(Task t : taskList2) {
					System.out.println(" task "+t.getAssignee()+" ii " +t.getName());
				
				}
			    return new ResponseEntity<>(flagOdobrena,HttpStatus.OK);
				
			}catch(FormFieldValidationException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				
			}
		
		}
		
		@GetMapping(path = "/getFormKoautoriFlag/{task}", produces = "application/json")
		public @ResponseBody FormFieldsDto getFormKoautoriFlag(@PathVariable String task) {
			System.out.println("dosao u get form koautori flag");
			Task activeTask = taskService.createTaskQuery().taskId(task).singleResult();
			String processInstanceId = activeTask.getProcessInstanceId();
		
			//Task activeTask = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
			TaskFormData tfd = formService.getTaskFormData(activeTask.getId());
			List<FormField> properties = tfd.getFormFields();
			
		
			return new FormFieldsDto(activeTask.getId(), processInstanceId, properties);
	    }
		
		
		@PostMapping(path = "/sacuvajKoautorEmail/{taskId}", produces = "application/json"	)
		public @ResponseBody ResponseEntity sacuvajKoautorEmail(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId, HttpServletRequest request) {
			
			HashMap<String, Object> map = this.mapListToDto(dto);
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			boolean postoji = false;
			User korisnik = null;
			
			for(int i = 0;i<dto.size();i++) {
					if(dto.get(i).getFieldId().equals("emailK")) {
						korisnik = userService.findUserByEmail(dto.get(i).getFieldValue());
						break;
						
					}
				}		
			
			try {
				if(korisnik == null) {
					System.out.println("korisnik je null");
					postoji = false;
					runtimeService.setVariable(processInstanceId, "KoautorUBazi", false);

				}else {

					System.out.println("korisnik  nije  null");
					postoji = true;
					runtimeService.setVariable(processInstanceId, "KoautorUBazi", true);

				}
				runtimeService.setVariable(processInstanceId, "koautorEmail", dto);

				formService.submitTaskForm(taskId, map);
		       
		        return new ResponseEntity<>(postoji,HttpStatus.OK);
				
			}catch(FormFieldValidationException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				
			}
		
		}
		
		
		
		@PostMapping(path = "/sacuvajKoautoriFlag/{taskId}", produces = "application/json"	)
		public @ResponseBody ResponseEntity sacuvajKoautoriFlag(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId, HttpServletRequest request) {
			
			HashMap<String, Object> map = this.mapListToDto(dto);
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			boolean flagOdobrena = false;
			for(int i = 0;i<dto.size();i++) {
					if(dto.get(i).getFieldId().equals("UnosKoautora")) {
						if(dto.get(i).getFieldValue().equals("true")) {
							flagOdobrena = true;
						}
					}
				}
					
			
			try {

				runtimeService.setVariable(processInstanceId, "UnosKoautora", flagOdobrena);

				formService.submitTaskForm(taskId, map);
				
				List<FormSubmissionDto> magazin = (List<FormSubmissionDto>)runtimeService.getVariable(processInstanceId,"izabraniMagazin");
				Magazin izabraniMagazin = new Magazin();
				  for (FormSubmissionDto formField : magazin) {
					if(formField.getFieldId().equals("casopisi")) {
						Long idMagazina = Long.parseLong(formField.getFieldValue());
						System.out.println("id magazina je "+idMagazina);
						izabraniMagazin = magazinService.findOneById(idMagazina);
						break;
					}	      
				  }
				
				User urednik = izabraniMagazin.getGlavniUrednik();
				
				List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
				for(Task t : taskList) {
					System.out.println(" task "+t.getAssignee()+" ii " +t.getId());
					if(t.getName().equals("Pregled teme rada") || t.getId().equals("Task_0ly1nks")) {
						System.out.println("usao ovdje da postavi assignee");
						t.setAssignee(urednik.getUsername());

						System.out.println("usao ovdje da postavi assignee"+t.getAssignee());
						taskService.saveTask(t);
						break;
					}
				}
				System.out.println("vrijednost flaga je "+flagOdobrena);
		        return new ResponseEntity<>(flagOdobrena,HttpStatus.OK);
				
			}catch(FormFieldValidationException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				
			}
		
		}
		
		
		@PostMapping(path = "/sacuvajKoautorPodaci/{taskId}", produces = "application/json"	)
		public @ResponseBody ResponseEntity sacuvajKoautorPodaci(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId, HttpServletRequest request) {
			System.out.println("U sacuvajKoautorPodaci je");
			HashMap<String, Object> map = this.mapListToDto(dto);
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			
			
			for(int i = 0;i<dto.size();i++) {
					if(dto.get(i).getFieldId().equals("imeKoautor")) {
						if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
							System.out.println("prazan naziv");
							
							return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
						}
					}
					
					if(dto.get(i).getFieldId().equals("GradKoautor")) {
						if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
							System.out.println("prazan kljucne rijeci");
							
							return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
						}				}
					
					if(dto.get(i).getFieldId().equals( "DrzavaKoautor")) {
						if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
							System.out.println("prazan apstrakt");
							
							return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
						}				}
						
				
			}
			try {

				runtimeService.setVariable(processInstanceId, "koautorData", dto);
				formService.submitTaskForm(taskId, map);
		        
				
				
		        return new ResponseEntity<>(HttpStatus.OK);
				
			}catch(FormFieldValidationException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				
			}
	    }
		
		@PostMapping(path = "/sacuvajPregledSadrzaja/{taskId}", produces = "application/json"	)
		public @ResponseBody ResponseEntity sacuvajPregledSadrzaja(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId, HttpServletRequest request) {
			
			HashMap<String, Object> map = this.mapListToDto(dto);
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			boolean flagOdobrena = false;
			for(int i = 0;i<dto.size();i++) {
					if(dto.get(i).getFieldId().equals("formatiran")) {
						if(dto.get(i).getFieldValue().equals("true")) {
							flagOdobrena = true;
						}
					}
				}
					
			
			try {

				runtimeService.setVariable(processInstanceId, "pregledSadrzajaForma", dto);

				runtimeService.setVariable(processInstanceId, "dobroFormatiran", flagOdobrena);
				formService.submitTaskForm(taskId, map);
				
				String idText = (String)runtimeService.getVariable(processInstanceId,"textId");
				Text text = textService.findById(Long.parseLong(idText));
				
				return new ResponseEntity<>(flagOdobrena,HttpStatus.OK);
				
			}catch(FormFieldValidationException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				
			}
		
		}
		
		@PostMapping(path = "/izvrsiPlacanje/{taskId}", produces = "application/json"	)
		public @ResponseBody ResponseEntity izvrsiPlacanje(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId, HttpServletRequest request) {
			
			HashMap<String, Object> map = this.mapListToDto(dto);
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			boolean uplaceno = false;
			for(int i = 0;i<dto.size();i++) {
					if(dto.get(i).getFieldId().equals("uplaceno")) {
						if(dto.get(i).getFieldValue().equals("true")) {
							uplaceno = true;
						}
					}
				}
					
			
			try {

				runtimeService.setVariable(processInstanceId, "uplaceno", uplaceno);

				formService.submitTaskForm(taskId, map);
				
				return new ResponseEntity<>(uplaceno,HttpStatus.OK);
				
			}catch(FormFieldValidationException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				
			}
		
		}
		
		
		@PostMapping(path = "/updateRad/{taskId}", produces = "application/json"	)
		public @ResponseBody ResponseEntity updateRad(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId, HttpServletRequest request) {
			System.out.println("U updateRad je");
			HashMap<String, Object> map = this.mapListToDto(dto);
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			
			String username = Utils.getUsernameFromRequest(request, tokenUtils);
			runtimeService.setVariable(processInstanceId, "autor", username);
			
			for(int i = 0;i<dto.size();i++) {
				
				if(dto.get(i).getFieldId() == "pdfIspravke") {
						if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
							System.out.println("prazan pdf url");
							
							return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
						}	
					}
				
			}
			try {

				runtimeService.setVariable(processInstanceId, "ispravljenRad", dto);
				formService.submitTaskForm(taskId, map);
				
				List<FormSubmissionDto> magazin = (List<FormSubmissionDto>)runtimeService.getVariable(processInstanceId,"izabraniMagazin");
				Magazin izabraniMagazin = new Magazin();
				  for (FormSubmissionDto formField : magazin) {
					if(formField.getFieldId().equals("casopisi")) {
						Long idMagazina = Long.parseLong(formField.getFieldValue());
						System.out.println("id magazina je "+idMagazina);
						izabraniMagazin = magazinService.findOneById(idMagazina);
						break;
					}	      
				  }
				
				User urednik = izabraniMagazin.getGlavniUrednik();
				
				List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
				for(Task t : taskList) {
					System.out.println(" task "+t.getAssignee()+" ii " +t.getId());
					if(t.getName().equals("Pregled teme rada") || t.getId().equals("Task_0ly1nks")) {
						System.out.println("usao ovdje da postavi assignee");
						t.setAssignee(urednik.getUsername());

						System.out.println("usao ovdje da postavi assignee"+t.getAssignee());
						taskService.saveTask(t);
						break;
					}
				}
		        return new ResponseEntity<>(HttpStatus.OK);
				
			}catch(FormFieldValidationException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				
			}
	    }
		
		@PostMapping(path = "/sacuvajRecenzente/{taskId}", produces = "application/json"	)
		public @ResponseBody ResponseEntity sacuvajRecenzente(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId, HttpServletRequest request) {
			
			HashMap<String, Object> map = this.mapListToDto(dto);
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId = task.getProcessInstanceId();
			boolean imaRec = false;
			for(int i = 0;i<dto.size();i++) {
					if(dto.get(i).getFieldId().equals("rec")) {
						System.out.println("Postoje recenzenti za datu naucnu oblast");
						imaRec = true;
						//provjera koliko ima recenzenata
						/*
						 * if(dto.get(i).getFieldId().equals("oblasti")) {
					if(dto.get(i).getOblasti().size() <= 1) {
						System.out.println("nedovoljno recenzenata");
					
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					
					}
				}
						*/
					}
					if(dto.get(i).getFieldId().equals("rokRecenziranje")) {
						System.out.println("Rok za ispravku");
						
						runtimeService.setVariable(processInstanceId, "rokRecenziranje", dto.get(i).getFieldValue());
						
					}
				}
					
			
			try {

				runtimeService.setVariable(processInstanceId, "izabraniRecenzentiForm", dto);
				//ovo mi mozda i nece trebati
				runtimeService.setVariable(processInstanceId, "imaRec", imaRec);
				
				formService.submitTaskForm(taskId, map);
				
				
				return new ResponseEntity<>(HttpStatus.OK);
				
			}catch(FormFieldValidationException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				
			}
		
		}

}
