package com.example.springjobboard.repository.basic;

import com.example.springjobboard.model.HasId;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public interface BasicRepository<T extends HasId<ID>, ID> {
    void setClazz(@NotNull Class<T> clazz);
    Set<T> findAll();
    T findById(ID id);
    T findByIdEagerly(ID id);
    T findByIdEagerly(ID id, String[] fetchedTables);
    T findByField(String fieldName, Object value);
    T save(T entity);
    T update(ID id, T entity);
    boolean delete(T entity);
    boolean deleteById(ID id);
}
