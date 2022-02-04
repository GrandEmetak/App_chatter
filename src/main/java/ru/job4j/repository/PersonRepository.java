package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.entity.Person;

/**
 * <S extends T> S save(S entity);
 * <S extends T> Iterable<S> saveAll(Iterable<S> entities);
 * Optional<T> findById(ID id);
 * boolean existsById(ID id);
 * Iterable<T> findAll();
 * Iterable<T> findAllById(Iterable<ID> ids);
 * long count();
 * void deleteById(ID id);
 * void delete(T entity);
 * void deleteAllById(Iterable<? extends ID> ids);
 * void deleteAll(Iterable<? extends T> entities);
 * void deleteAll();
 */
public interface PersonRepository extends CrudRepository<Person, Integer> {

    Person findByUsername(String username);
}
