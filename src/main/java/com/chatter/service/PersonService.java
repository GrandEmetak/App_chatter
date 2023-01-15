package com.chatter.service;

import org.springframework.stereotype.Service;
import com.chatter.entity.model.Person;
import com.chatter.entity.model.Role;
import com.chatter.repository.PersonRepository;
import com.chatter.repository.RoleRepository;

import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final RoleRepository repository;

    public PersonService(PersonRepository personRepository, RoleRepository repository) {
        this.personRepository = personRepository;
        this.repository = repository;
    }

    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    public Person save(Person person) {
        person.setRole(findRoleById(1));
        return personRepository.save(person);
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }

    public void deleteById(int id) {
        personRepository.deleteById(id);
    }

    private Role findRoleById(int id) {
        Optional<Role> role = repository.findById(1);
        var r = role.orElse(new Role());
        return r;
    }

}
