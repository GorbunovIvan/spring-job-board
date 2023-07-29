package com.example.springjobboard.repository;

import com.example.springjobboard.model.EntityWithId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Getter
@Slf4j
@Scope("prototype")
public class BasicRepositoryImpl<T extends EntityWithId<ID>, ID> implements BasicRepository<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> clazz;
    private String className;

    public void setClazz(@NotNull Class<T> clazz) {
        setClazzForChildren(clazz);
    }

    protected void setClazzForChildren(@NotNull Class<T> clazz) {
        this.clazz = clazz;
        this.className = this.clazz.getSimpleName();
    }

    @Override
    @Transactional
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
    @Transactional
    public T findByIdEagerly(ID id) {

        var fields = Arrays.stream(getClazz().getDeclaredFields())
                .filter(f -> Collection.class.isAssignableFrom(f.getType()))
                .filter(f -> f.isAnnotationPresent(OneToMany.class)
                            || f.isAnnotationPresent(ManyToMany.class))
                .map(Field::getName)
                .toArray(String[]::new);

        return findByIdEagerly(id, fields);
    }

    @Override
    @Transactional
    public T findByIdEagerly(ID id, String... fetchedTables) {

        StringBuilder tablesInQuery = new StringBuilder();
        for (var fetchedTable : fetchedTables) {
            tablesInQuery.append("LEFT JOIN FETCH obj.").append(fetchedTable).append(" ");
        }

        return entityManager.createQuery("FROM " + getClassName() + " obj " +
                        tablesInQuery +
                        "WHERE obj.id = :id", clazz)
                .setParameter("id", id)
                .getResultStream()
                .findAny()
                .orElse(null);
    }

    @Override
    @Transactional
    public T findByField(String fieldName, Object value) {
        return entityManager.createQuery("FROM " + getClassName() + " " +
                        "WHERE " + fieldName + " = :fieldName", clazz)
                .setParameter("fieldName", value)
                .getResultStream()
                .findAny()
                .orElse(null);
    }

    @Override
    @Transactional
    public T save(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    @Transactional
    public T update(ID id, T entity) {
        var entityInContext = entityManager.find(clazz, id);
        if (entityInContext != null) {
            entity.setId(id);
            return entityManager.merge(entity);
        } else {
            throw new EntityNotFoundException(String.format("Entity '%s' with id '%s' is not found", className, id));
        }
    }

    @Override
    @Transactional
    public boolean delete(T entity) {
        return deleteById(entity.getId());
    }

    @Override
    @Transactional
    public boolean deleteById(ID id) {
        T entityInContext = entityManager.find(clazz, id);
        if (entityInContext != null) {
            entityManager.remove(entityInContext);
            return true;
        } else {
            return false;
        }
    }

    private void doInSession(Consumer<EntityManager> consumer) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            consumer.accept(entityManager);
            transaction.commit();
        } catch (Exception e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }
}
