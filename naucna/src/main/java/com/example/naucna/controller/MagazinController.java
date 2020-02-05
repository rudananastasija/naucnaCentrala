package com.example.naucna.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.naucna.model.FormFieldsDto;
import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.Magazin;
import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.model.User;
import com.example.naucna.security.TokenUtils;
import com.example.naucna.services.MagazinServiceImp;
import com.example.naucna.services.NaucnaOblastService;
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

	
	//startovanje processa za kreiranje magazina
	@GetMapping(path = "/get", produces = "application/json")
	@PreAuthorize("hasAuthority('KREIRANJE_CASOPISA')")    
    public @ResponseBody FormFieldsDto get(@Context HttpServletRequest request) {
		
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("procesCasopisID");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		
		return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }
	
	
	   //startovanje procesa za obradu novog teksta
		@GetMapping(path = "/getFormaObradaTeksta", produces = "application/json")
		//@PreAuthorize("hasAuthority('KREIRANJE_CASOPISA')")    
	    public @ResponseBody FormFieldsDto getFormaObradaTeksta(@Context HttpServletRequest request) {
			System.out.println("dosao po magazine za izbor u obradi teksta"); 
			ProcessInstance pi = runtimeService.startProcessInstanceByKey("procesObradaId");
			Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
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
	        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			for(Task t : taskList) {
				System.out.println(" task "+t.getAssignee()+" ii " +t.getId());
			}
			
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
		System.out.println("U sacuvajIzborMagazina");
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		for(int i = 0;i<dto.size();i++) {
				if(dto.get(i).getFieldId() == "casopisi") {
					if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
						System.out.println("prazni casopisi");
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}
				}
				
		}
		try {

			runtimeService.setVariable(processInstanceId, "izabraniMagazin", dto);
			formService.submitTaskForm(taskId, map);
	       
	        return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(FormFieldValidationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
    }
	//metoda vraca formu za unos novog rada
	@GetMapping(path = "/getFormaNoviRad/{processInstanceId}", produces = "application/json")
	//@PreAuthorize("hasAuthority('POTVRDI_RECENZENT')") 
    public @ResponseBody FormFieldsDto getFormPotvrda(@PathVariable String processInstanceId) {
		System.out.println("dosao po formu za novi rad");
		Task activeTask = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
		
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
			
			
		}
		try {

			runtimeService.setVariable(processInstanceId, "rad", dto);
			formService.submitTaskForm(taskId, map);
	        
	        return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(FormFieldValidationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
    }

}
