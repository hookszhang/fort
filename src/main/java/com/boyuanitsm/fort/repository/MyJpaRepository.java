package com.boyuanitsm.fort.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface MyJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * Return own create data list.
     *
     * @param var1
     * @return
     */
    Page<T> findOwnAll(Pageable var1);

    /**
     * Return this app resource list.
     *
     * @param var1
     * @param appId the id of the app.
     * @return
     */
    Page<T> findAllByAppId(Pageable var1, Long appId);
}
