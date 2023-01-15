package com.chatter.service;

import org.springframework.stereotype.Service;
import com.chatter.entity.Role;
import com.chatter.repository.RoleRepository;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Iterable<Role> findAll() {
        return repository.findAll();
    }

    public Optional<Role> findById(int id) {
        return repository.findById(id);
    }

    public Role save(Role role) {
        return repository.save(role);
    }

    public void delete(Role role) {
        repository.delete(role);
    }
}
