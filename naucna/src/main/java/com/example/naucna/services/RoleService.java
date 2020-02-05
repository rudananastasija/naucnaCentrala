package com.example.naucna.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.naucna.model.Role;
import com.example.naucna.repositories.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findOneById(Integer id){
        return roleRepository.findOneById(id);
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    public Role save(Role role){
        return roleRepository.save(role);
    }

    public void remove(Integer id){
        roleRepository.deleteById(id);
    }

    public Role findOneByName(String name){
        return roleRepository.findOneByName(name);
    }

}
