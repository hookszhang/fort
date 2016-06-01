package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.domain.SecurityGroup;

/**
 * Spring Data JPA repository for the SecurityGroup entity.
 */
@SuppressWarnings("unused")
public interface SecurityGroupRepository extends MyJpaRepository<SecurityGroup,Long> {

    SecurityGroup findByAppAndName(SecurityApp app, String name);

}
