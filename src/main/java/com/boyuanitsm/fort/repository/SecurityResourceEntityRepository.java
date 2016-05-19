package com.boyuanitsm.fort.repository;

import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.domain.SecurityResourceEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the SecurityResourceEntity entity.
 */
@SuppressWarnings("unused")
public interface SecurityResourceEntityRepository extends MyJpaRepository<SecurityResourceEntity,Long> {

    @Query("select distinct securityResourceEntity from SecurityResourceEntity securityResourceEntity left join fetch securityResourceEntity.authorities where securityResourceEntity.app.appKey =:appKey")
    List<SecurityResourceEntity> findAllByAppKeyWithEagerRelationships(@Param("appKey") String appKey);

    SecurityResourceEntity findByAppAndUrl(SecurityApp app, String url);
}
