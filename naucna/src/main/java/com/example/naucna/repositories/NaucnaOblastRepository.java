package com.example.naucna.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.naucna.model.NaucnaOblast;


@Repository 
public interface NaucnaOblastRepository extends JpaRepository<NaucnaOblast, Long> {
	
}
