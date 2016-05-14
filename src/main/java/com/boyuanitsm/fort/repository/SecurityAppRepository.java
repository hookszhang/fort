package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityApp;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SecurityApp entity.
 */
@SuppressWarnings("unused")
public interface SecurityAppRepository extends JpaRepository<SecurityApp,Long> {

}
