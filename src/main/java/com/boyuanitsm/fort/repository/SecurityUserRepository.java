package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the SecurityUser entity.
 */
@SuppressWarnings("unused")
public interface SecurityUserRepository extends MyJpaRepository<SecurityUser,Long> {

    @Query("select distinct securityUser from SecurityUser securityUser left join fetch securityUser.roles left join fetch securityUser.groups")
    List<SecurityUser> findAllWithEagerRelationships();

    @Query("select securityUser from SecurityUser securityUser left join fetch securityUser.roles left join fetch securityUser.groups where securityUser.id =:id")
    SecurityUser findOneWithEagerRelationships(@Param("id") Long id);

}
