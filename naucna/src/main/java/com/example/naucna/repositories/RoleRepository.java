package com.example.naucna.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.naucna.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findOneById(Integer id);
    Role findOneByName(String name);
}
