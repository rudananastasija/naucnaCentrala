package com.example.naucna.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.User;
import com.example.naucna.repositories.UserRepository;

@Service
public class UserService implements UserServiceImp{

	@Autowired
	private UserRepository repozitorijum;

	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		return repozitorijum.save(user);
	}

	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return repozitorijum.findOneByEmail(email);
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return repozitorijum.findAll();
	}
	

	@Override
	public User findUserByUsername(String username) {
		// TODO Auto-generated method stub
		return repozitorijum.findOneByUsername(username);
	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		repozitorijum.delete(user);
	}

	@Override
	public String enkriptuj(String sifra) throws NoSuchAlgorithmException{
			MessageDigest md = MessageDigest.getInstance("SHA-256"); 
			  
			 byte[] messageDigest = md.digest(sifra.getBytes()); 
	         
	         StringBuilder sb = new StringBuilder();
	         for (byte b : messageDigest) {
	             sb.append(String.format("%02x", b));
	         }
	        String returnValue=sb.toString();
	        System.out.println(" enkriptovano je "+returnValue);
	    	
	        return returnValue;
		
	}

	@Override
	public User findById(Long id) {
		// TODO Auto-generated method stub
		return repozitorijum.findOneById(id);
	}

}
