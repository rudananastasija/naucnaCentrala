package com.example.naucna.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.naucna.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	User findOneByEmail(String email);
	User findOneByUsername(String username);
	User findOneById(Long id);
	}
