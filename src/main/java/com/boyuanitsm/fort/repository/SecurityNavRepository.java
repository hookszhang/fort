package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.domain.SecurityNav;
import com.boyuanitsm.fort.domain.SecurityResourceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the SecurityNav entity.
 */
@SuppressWarnings("unused")
public interface SecurityNavRepository extends MyJpaRepository<SecurityNav, Long> {

    List<SecurityNav> findByParentId(Long parentId);

    List<SecurityNav> findByResource(SecurityResourceEntity resource);

    @Query("select max(securityNav.position) from SecurityNav securityNav where securityNav.app = :app")
    Double findMaxPosition(@Param("app") SecurityApp app);
}
