package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityLoginEvent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SecurityLoginEvent entity.
 */
@SuppressWarnings("unused")
public interface SecurityLoginEventRepository extends JpaRepository<SecurityLoginEvent,Long> {

}
