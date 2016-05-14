package com.boyuanitsm.fort.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

public interface CustomJpaRepository<T, ID extends Serializable> {

    Page<T> findOwnAll(Pageable var1);
}
