package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityNav;
import com.boyuanitsm.fort.domain.SecurityResourceEntity;

import java.util.List;

/**
 * Spring Data JPA repository for the SecurityNav entity.
 */
@SuppressWarnings("unused")
public interface SecurityNavRepository extends MyJpaRepository<SecurityNav, Long> {

    List<SecurityNav> findByParentId(Long parentId);

    List<SecurityNav> findByResource(SecurityResourceEntity resource);
}
