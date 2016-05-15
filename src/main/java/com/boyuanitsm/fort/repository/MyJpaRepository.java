package com.boyuanitsm.fort.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface MyJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    Page<T> findOwnAll(Pageable var1);
}
