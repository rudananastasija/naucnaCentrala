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
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.naucna.model.User;
import com.example.naucna.services.MagazinServiceImp;
import com.example.naucna.services.UserServiceImp;


@RestController
@RequestMapping(value = "/magazin")
@CrossOrigin(origins = "http://localhost:4200")

public class MagazinController {

	@Autowired
	private MagazinServiceImp service;
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

	
//startovanje processa za kreiranje magazina
	@GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody FormFieldsDto get(@Context HttpServletRequest request) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("procesCasopisID");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		User korisnik = (User)request.getSession().getAttribute("ulogovan");		
		if(korisnik != null) {
			korisnik.setUloga("glavniUrednik");
			userService.saveUser(korisnik);
		}
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }
	
	@PostMapping(path = "/dodajMagazin/{taskId}", produces = "application/json"	)
    public @ResponseBody ResponseEntity dodajMagazin(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		System.out.println("U potvrdi magazin je");
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
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
	
}
