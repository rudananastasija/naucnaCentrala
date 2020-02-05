package com.example.naucna.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.naucna.model.Magazin;
import com.example.naucna.model.User;


@Repository
public interface MagazinRepository extends JpaRepository<Magazin, Long>{
	public Magazin findOneByIssn(Long issn);
	Magazin findOneById(Long id);
	
}
