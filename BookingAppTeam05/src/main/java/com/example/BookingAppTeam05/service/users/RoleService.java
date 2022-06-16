package com.example.BookingAppTeam05.service.users;

import com.example.BookingAppTeam05.model.users.Role;
import com.example.BookingAppTeam05.model.repository.users.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findById(Long id) {
        return this.roleRepository.getOne(id);
    }

    public Role findByName(String name) {
        return this.roleRepository.findByName(name);
    }

}
