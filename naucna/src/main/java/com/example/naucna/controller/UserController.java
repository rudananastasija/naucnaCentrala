package com.example.naucna.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.naucna.model.NaucnaOblast;
import com.example.naucna.model.NaucnaOblastDto;
import com.example.naucna.model.Role;
import com.example.naucna.model.TaskDto;
import com.example.naucna.model.User;
import com.example.naucna.model.UserDto;
import com.example.naucna.services.UserServiceImp;



@RestController
@RequestMapping(value = "/korisnici")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	@Autowired
	private UserServiceImp service;

	@Autowired
	TaskService taskService;
	
	@RequestMapping(value="/aktivirajRegistraciju/{email}",
			method = RequestMethod.GET)
		public String aktivacijaNaloga(@PathVariable String email){
			List<User> useri = service.getAll();
			User user = service.findUserByUsername(email);
			
			user.setVerifikovan(true);	
			service.saveUser(user);
		return "Verifikovali ste mail, mozete posetiti sajt.";
		}

	@RequestMapping(value="/logovanje/{username}/password/{password}",method = RequestMethod.POST)
    public ResponseEntity<User> loginUser(@PathVariable String username,@PathVariable String password,@RequestBody User user, @Context HttpServletRequest request){
	   System.out.println("U loginUseru je");
       String encryptedPass = "";
       System.out.println("username j e "+username );

       System.out.println("lozinka j e "+password );

       User loginUser = service.findUserByUsername(username);
       if(loginUser == null){
    	    System.out.println("Ne postoji username");
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
       }
	     if(!loginUser.isVerifikovan()) {
	    	 System.out.println("Nije verifikovan");
				
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
	    
	     }
	   	try {
			encryptedPass = service.enkriptuj(password);
			System.out.println("u try");

	   	} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
		}
		
		if(!loginUser.getLozinka().equals(encryptedPass)) {
			//moraju se poklapati unesena lozinka i lozinka od korisnika sa unetim mailom 

			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}
		
		request.getSession().setAttribute("ulogovan", loginUser);

        System.out.println(username + " , " + password);
		return new ResponseEntity<User>(loginUser,HttpStatus.OK);
    }
	
	@RequestMapping(value="/logout",
			method = RequestMethod.GET)
	public User odjava(@Context HttpServletRequest request){
			
		User korisnik = (User)request.getSession().getAttribute("ulogovan");		
		
		System.out.println("Usao u funkciju logout");
		request.getSession().invalidate();
		if(korisnik == null) {
			return null;
		}
		
		return korisnik;
	
	}
	
	@GetMapping(path = "/vratiTaskove/{username}")
    public List<TaskDto> getTasks(@PathVariable String username) {
		System.out.println("dosao po taskove");
		System.out.println("mail "+username);
		User korisnik = null;
		List<User> useri = service.getAll();
		if(useri.size() == 0) {
			System.out.println("velicina niza je 0");
		}
		
		for(int i = 0;i<useri.size();i++)
		{
			System.out.println("u petlji");
			if(useri.get(i).getUsername() != null && useri.get(i).getUsername() != "") {
			if(useri.get(i).getUsername().equals(username)) {
				korisnik = useri.get(i);
				System.out.println("nasao usera");
				
				break;
			}
			
			}
		}
		List<Task> tasks = new ArrayList<Task>();
		
		if(korisnik != null) {
			System.out.println("nije korisnik null");
			boolean flag = false;
			for(com.example.naucna.model.Role role: korisnik.getRoles()){
                if(role.getName().equals("ROLE_ADMIN")){
                	flag = true;
                    break;
                    
                }
            }
            
			if(flag) {
				tasks.addAll(taskService.createTaskQuery().processDefinitionKey("RegistracijaId").taskAssignee("demo").list());
				System.out.println("nije korisnik null 2");
				tasks.addAll(taskService.createTaskQuery().processDefinitionKey("procesCasopisID").taskAssignee("demo").list());
				tasks.addAll(taskService.createTaskQuery().processDefinitionKey("procesObradaId").taskAssignee("demo").list());
				
			}else {
				System.out.println("ovdje dosao");
				tasks.addAll(taskService.createTaskQuery().processDefinitionKey("RegistracijaId").taskAssignee(username).list());
				tasks.addAll(taskService.createTaskQuery().processDefinitionKey("procesCasopisID").taskAssignee(username).list());
				tasks.addAll(taskService.createTaskQuery().processDefinitionKey("procesObradaId").taskAssignee(username).list());
				
			}
		}
		ArrayList<TaskDto> taskoviDto=  new ArrayList<TaskDto>();
		
		for(Task t:tasks) {
			System.out.println("Ubacivanje taskova"+t.getAssignee());
			taskoviDto.add(new TaskDto(t.getId(),t.getName(),t.getAssignee()));
		}
		System.out.println("broj taskova je"+taskoviDto.size());
        return taskoviDto;
    }
	
	@RequestMapping(value="/getUrednici", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getUrednici(){		
		List<User> useri = service.getAll();
		List<UserDto> urednici = new ArrayList<UserDto>();
		
		for(User u:useri) {
			 if(u.getUsername() != "" && u.getUsername() != null) {
		            for(Role role: u.getRoles()){
		                
		                if(role.getName().equals("ROLE_UREDNIK")){
		            		UserDto newUser = new UserDto(u.getId(),u.getIme(),u.getUloga());
		    				
		    				urednici.add(newUser);
		    				break;
		                }         
		             }	
			 }
		}
		return new ResponseEntity<List<UserDto>>(urednici, HttpStatus.OK);
	}
	@RequestMapping(value="/getRecenzenti", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getRecenzenti(){		
		List<User> useri = service.getAll();
		List<UserDto> rec = new ArrayList<UserDto>();
		for(User u:useri) {
			
			if(u.getUsername() != "" && u.getUsername() != null) {
	            for(Role role: u.getRoles()){
	                
	                if(role.getName().equals("ROLE_RECENZENT")){
	            		UserDto newUser = new UserDto(u.getId(),u.getIme(),u.getUloga());
	    				
	            		rec.add(newUser);
	            		break;
	                }         
	             }	
		 }
		
		}
		return new ResponseEntity<List<UserDto>>(rec, HttpStatus.OK);
	}
	
	
}
