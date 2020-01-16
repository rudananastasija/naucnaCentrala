package com.example.naucna.services;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.naucna.model.User;

@Service
public interface UserServiceImp {
	User saveUser(User user);
	User findUserByEmail(String email);
	List<User> getAll();
	User findUserByUsername(String username);
	void deleteUser(User user);
	String enkriptuj(String sifra)throws NoSuchAlgorithmException;
	User findById(Long id);
}
