package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityGroup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SecurityGroup entity.
 */
@SuppressWarnings("unused")
public interface SecurityGroupRepository extends JpaRepository<SecurityGroup,Long> {

}
