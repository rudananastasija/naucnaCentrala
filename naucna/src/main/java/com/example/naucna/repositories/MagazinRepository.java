package com.example.naucna.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.naucna.model.Magazin;


@Repository
public interface MagazinRepository extends JpaRepository<Magazin, Long>{
	public Magazin findOneByIssn(Long issn);
}
