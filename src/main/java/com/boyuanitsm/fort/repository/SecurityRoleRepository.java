package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityRole;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the SecurityRole entity.
 */
@SuppressWarnings("unused")
public interface SecurityRoleRepository extends MyJpaRepository<SecurityRole,Long> {

    @Query("select distinct securityRole from SecurityRole securityRole left join fetch securityRole.authorities")
    List<SecurityRole> findAllWithEagerRelationships();

    @Query("select securityRole from SecurityRole securityRole left join fetch securityRole.authorities where securityRole.id =:id")
    SecurityRole findOneWithEagerRelationships(@Param("id") Long id);

}
