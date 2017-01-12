/*
 * Copyright 2016-2017 Shanghai Boyuan IT Services Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    @Query("select distinct securityResourceEntity from SecurityResourceEntity securityResourceEntity left join fetch securityResourceEntity.authorities where securityResourceEntity.id =:id")
    SecurityResourceEntity findOneWithEagerRelationships(@Param("id") Long id);

    SecurityResourceEntity findByAppAndUrl(SecurityApp app, String url);
}
