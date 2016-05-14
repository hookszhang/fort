package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityResourceEntity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SecurityResourceEntity entity.
 */
@SuppressWarnings("unused")
public interface SecurityResourceEntityRepository extends JpaRepository<SecurityResourceEntity,Long> {

}
