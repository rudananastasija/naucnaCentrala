package com.example.naucna.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.naucna.model.Text;


@Repository
public interface TextRepository extends JpaRepository<Text,Long>{
	Text findOneById(Long id);
	
}
