package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityNav;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SecurityNav entity.
 */
@SuppressWarnings("unused")
public interface SecurityNavRepository extends JpaRepository<SecurityNav,Long> {

}
