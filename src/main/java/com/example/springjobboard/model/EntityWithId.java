package com.example.springjobboard.model;

public interface EntityWithId<T> {

    T getId();

    void setId(T id);

    default boolean isNew() {
        return getId() == null;
    }
}
