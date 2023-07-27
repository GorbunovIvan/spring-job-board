package com.example.springjobboard.repository;

import com.example.springjobboard.model.EntityWithId;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BasicRepository<T extends EntityWithId<ID>, ID> {
    Set<T> findAll();
    T findById(ID id);
    T save(T entity);
    T update(ID id, T entity);
    boolean delete(T entity);
    boolean deleteById(ID id);
}
