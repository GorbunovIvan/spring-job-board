package com.example.springjobboard.repository;

import com.example.springjobboard.model.EntityWithId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BasicRepositoryImpl<T extends EntityWithId<ID>, ID> implements BasicRepository<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> clazz;
    private final String className;

    public BasicRepositoryImpl(@NotNull Class<T> clazz) {
        this.clazz = clazz;
        this.className = this.clazz.getTypeName();
    }

    @Override
    public Set<T> findAll() {
        return entityManager.createQuery("FROM " + className, clazz)
                .getResultStream()
                .collect(Collectors.toSet());
    }

    @Override
    public T findById(ID id) {
        return entityManager.find(clazz, id);
    }

    @Override
    public T save(T entity) {
        return doInSessionAndReturn(mng -> mng.merge(entity));
    }

    @Override
    public T update(ID id, T entity) {
        return doInSessionAndReturn(mng -> {
            var entityInContext = entityManager.find(clazz, id);
            if (entityInContext != null) {
                entity.setId(id);
                return mng.merge(entity);
            } else {
                throw new EntityNotFoundException(String.format("Entity '%s' with id '%s' is not found", className, id));
            }
        });
    }

    @Override
    public boolean delete(T entity) {
        return deleteById(entity.getId());
    }

    @Override
    public boolean deleteById(ID id) {

        var removedEntity = doInSessionAndReturn(mng -> {
            T entityInContext = entityManager.find(clazz, id);
            if (entityInContext != null) {
                mng.remove(entityInContext);
                return entityInContext;
            } else {
                return null;
            }
        });

        return removedEntity != null;
    }

    private void doInSession(Consumer<EntityManager> consumer) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            consumer.accept(entityManager);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    private T doInSessionAndReturn(Function<EntityManager, T> function) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            T result = function.apply(entityManager);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }
}
