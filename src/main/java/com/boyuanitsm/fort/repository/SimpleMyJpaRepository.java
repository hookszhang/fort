package com.boyuanitsm.fort.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class SimpleMyJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements MyJpaRepository<T, ID> {

    private final EntityManager entityManager;

    public SimpleMyJpaRepository(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        // Keep the EntityManager around to used from the newly introduced methods.
        this.entityManager = entityManager;
    }

    @Override
    public Page<T> findOwnAll(Pageable var1) {
        return this.findAll(var1);
    }
}
