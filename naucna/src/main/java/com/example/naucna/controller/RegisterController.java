package com.example.naucna.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.FormFieldValidationConstraint;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.naucna.model.FormFieldsDto;
import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.model.OgranicenjeDto;
import com.example.naucna.model.TaskDto;
import com.example.naucna.model.User;
import com.example.naucna.services.UserService;
import com.sun.research.ws.wadl.HTTPMethods;



@Controller
@RequestMapping("/register")
@CrossOrigin(origins = "http://localhost:4200")
public class RegisterController {
	@Autowired
	IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	FormService formService;
	
	@GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody FormFieldsDto get() {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("RegistracijaId");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		task.setAssignee("korisnik");
		taskService.saveTask(task);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		List<FormFieldValidationConstraint> constraints = new ArrayList<FormFieldValidationConstraint>();
		
		List<OgranicenjeDto> ogranicenja = new ArrayList<OgranicenjeDto>();
		
		for(FormField ff : properties) {
			
			OgranicenjeDto ogranicenje = new OgranicenjeDto(ff.getId(),false);
			
			for(int i = 0;i<ff.getValidationConstraints().size();i++) {
				System.out.println("u petlji");
				
				ogranicenje.setRequired(true);
				constraints.add(ff.getValidationConstraints().get(i));
			}
			
			ogranicenja.add(ogranicenje);	
			}
		return new FormFieldsDto(task.getId(), properties, pi.getId(),constraints,ogranicenja);
    }
	
	@PostMapping(path = "/potvrdiUsera/{taskId}", produces = "application/json"	)
    public @ResponseBody ResponseEntity potvrdiUsera(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		System.out.println("U potvrdiUser je");
		List<FormSubmissionDto> returnValue = new ArrayList<FormSubmissionDto>();
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();

		for(int i = 0;i<dto.size();i++) {
			if(!dto.get(i).getFieldId().equals("titula") && !dto.get(i).getFieldId().equals("recenzent")&&!dto.get(i).getFieldId().equals("oblasti")) {
				if(dto.get(i).getFieldValue()  == null || dto.get(i).getFieldValue().isEmpty()) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
			
			User korisnik = null;
			
			if(dto.get(i).getFieldId().equals("email")) {
				korisnik = userService.findUserByEmail(dto.get(i).getFieldValue());
				if(korisnik != null) {
					returnValue.add(new FormSubmissionDto("email", "Email nije jedinstven"));
				}else {
					returnValue.add(new FormSubmissionDto("email", "ok"));
					
				}
			}
			
			if(dto.get(i).getFieldId().equals("korisnickoIme")) {
				korisnik = userService.findUserByUsername(dto.get(i).getFieldValue());
				if(korisnik != null) {
					returnValue.add(new FormSubmissionDto("username", "Korisnicko ime nije jedinstveno"));
				}else {
					returnValue.add(new FormSubmissionDto("username", "ok"));
					
				}
				
			}
			
			if(dto.get(i).getFieldId().equals("oblasti")) {
				if(dto.get(i).getOblasti().size() == 0) {
					returnValue.add(new FormSubmissionDto("oblasti", "morate izabrati najmanje jednu oblast!"));
					return new ResponseEntity<List<FormSubmissionDto>>(returnValue, HttpStatus.OK);
						  
				}else {
					returnValue.add(new FormSubmissionDto("oblasti", "ok"));
					
				}
			}
			if(korisnik != null) {
			     return new ResponseEntity<List<FormSubmissionDto>>(returnValue, HttpStatus.OK);
			       
			}
		}
		try {

			runtimeService.setVariable(processInstanceId, "registration", dto);
			formService.submitTaskForm(taskId, map);
	        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			for(Task t : taskList) {
				System.out.println(" task "+t.getAssignee()+" ii " +t.getId());
			}
	        return new ResponseEntity<>(returnValue,HttpStatus.OK);
			
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
	
	@GetMapping(path = "/getFormPotvrda/{task}", produces = "application/json")
	@PreAuthorize("hasAuthority('POTVRDI_RECENZENT')") 
    public @ResponseBody FormFieldsDto getFormPotvrda(@PathVariable String task) {
		System.out.println("dosao po formu za potvrdu recenzenta");
	
		Task pronadjenTask = taskService.createTaskQuery().taskId(task).singleResult();
		String processId = pronadjenTask.getProcessInstanceId();
		
		pronadjenTask.setAssignee("demo");
		taskService.saveTask(pronadjenTask);
		TaskFormData tfd = formService.getTaskFormData(task);
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		
        return new FormFieldsDto(task, processId, properties);
    }
	
	@PostMapping(path = "/potvrdi/{taskId}", produces = "application/json"	)
    public @ResponseBody ResponseEntity potvrdiRecenzenta(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		System.out.println("U potvrdiRecenzenta je");
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		try {

			runtimeService.setVariable(processInstanceId, "potvrda", dto);
			formService.submitTaskForm(taskId, map);
	        
	        return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(FormFieldValidationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
    }
	
	@GetMapping(path = "/getFormAktivacija/{processInstanceId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getFormAktivacija(@PathVariable String processInstanceId) {
		System.out.println("dosao po formu za aktivaciju");
	
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		
		/*List<TaskDto> tasksDto = new ArrayList<TaskDto>();
		for(int i =0;i<tasks.size();i++) {
			TaskDto newTaskDto = new TaskDto();
			newTaskDto.setTaskId(tasks.get(i).getId());
			newTaskDto.setName(tasks.get(i).getName());
			newTaskDto.setAssignee(tasks.get(i).getAssignee());
			tasksDto.add(newTaskDto);
		}
		Task activeTask = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
		
		activeTask.setAssignee("korisnik");
		taskService.saveTask(activeTask);
		
		*/
		TaskFormData tfd = formService.getTaskFormData(tasks.get(0).getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		
        return new FormFieldsDto(tasks.get(0).getId(), processInstanceId, properties);
    }
	
	@PostMapping(path = "/sacuvajAktivaciju/{taskId}", produces = "application/json"	)
    public @ResponseBody ResponseEntity sacuvajAktivaciju(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		System.out.println("U potvrdiRecenzenta je");
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		try {

			runtimeService.setVariable(processInstanceId, "aktivacija", dto);
			formService.submitTaskForm(taskId, map);
	        
	        return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(FormFieldValidationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
    }
	
	
	@GetMapping(path = "/getFormaCasopis", produces = "application/json")
    public @ResponseBody FormFieldsDto getFormaCasopis() {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("procesCasopisID");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		System.out.println("usao u fu za formu casopis");
		return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }

	@GetMapping(path = "/getUredniciForm/{processInstanceId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getUredniciForm(@PathVariable String processInstanceId) {
		System.out.println("dosao po formu za urednike i recenzente");
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		List<TaskDto> tasksDto = new ArrayList<TaskDto>();
		for(int i =0;i<tasks.size();i++) {
			TaskDto newTaskDto = new TaskDto();
			newTaskDto.setTaskId(tasks.get(i).getId());
			newTaskDto.setName(tasks.get(i).getName());
			newTaskDto.setAssignee(tasks.get(i).getAssignee());
			tasksDto.add(newTaskDto);
		}
		Task activeTask = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
		
		
		TaskFormData tfd = formService.getTaskFormData(tasks.get(0).getId());
		List<FormField> properties = tfd.getFormFields();
	
		
        return new FormFieldsDto(tasks.get(0).getId(), processInstanceId, properties);
   
    }
	
	
	@PostMapping(path = "/updateMagazin/{taskId}", produces = "application/json"	)
    public @ResponseBody ResponseEntity updateMagazin(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		System.out.println("U updateMagazin je");
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		for(int i = 0;i<dto.size();i++) {
			System.out.println(" usao u petlju ");
			if(dto.get(i).getFieldId().equals("recenzenti")) {
				if(dto.get(i).getRecenzenti().size() <=1) {
					System.out.println("nedoovljan broj recenzenata");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					
				}
			}
			
			if(dto.get(i).getFieldId().equals("urednici")) {
				if(dto.get(i).getRecenzenti().size() >2) {
					System.out.println("broj urednika ne valja");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					
				}
			}
		}
		try {

			runtimeService.setVariable(processInstanceId, "updateMagazin", dto);
			formService.submitTaskForm(taskId, map);
	        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
	        System.out.println("sacuvao variable urednik");
		
	        for(Task t : taskList) {
	        	System.out.println(" task "+t.getAssignee()+" ii " +t.getId());
			}
	        return new ResponseEntity<>(HttpStatus.OK);
			
		}catch(FormFieldValidationException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
    }
	
}
