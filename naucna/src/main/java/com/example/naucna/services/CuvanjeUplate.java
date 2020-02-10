package com.example.naucna.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.User;

@Service
public class CuvanjeUplate  implements JavaDelegate{
	@Autowired
	UserService userService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		User autor = new User();
		String autorUsername = (String)execution.getVariable("activator");
		
		autor = userService.findUserByUsername(autorUsername);    
		if(autor != null) {
			autor.setPlaceno(true);

			System.out.println("uplaceno je");
			userService.saveUser(autor);
			
		}
		
	}

}
