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
import com.boyuanitsm.fort.domain.SecurityRole;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

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

    @Query("select distinct securityRole from SecurityRole securityRole left join fetch securityRole.authorities where securityRole.app.appKey =:appKey")
    List<SecurityRole> findAllByAppKeyWithEagerRelationships(@Param("appKey") String appKey);

    SecurityRole findByAppAndName(SecurityApp app, String name);
}
