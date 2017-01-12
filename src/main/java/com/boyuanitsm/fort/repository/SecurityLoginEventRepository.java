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

import com.boyuanitsm.fort.domain.SecurityLoginEvent;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the SecurityLoginEvent entity.
 */
@SuppressWarnings("unused")
public interface SecurityLoginEventRepository extends JpaRepository<SecurityLoginEvent, Long> {

    @Query("select securityLoginEvent from SecurityLoginEvent securityLoginEvent where securityLoginEvent.tokenValue =:tokenValue and  securityLoginEvent.tokenOverdueTime > :tokenOverdueTime")
    SecurityLoginEvent findByTokenValueAndTokenOverdueTime(@Param("tokenValue") String tokenValue, @Param("tokenOverdueTime") ZonedDateTime tokenOverdueTime);

    @Query("select securityLoginEvent from SecurityLoginEvent securityLoginEvent where securityLoginEvent.user.id =:userId and  securityLoginEvent.tokenOverdueTime > :tokenOverdueTime order by securityLoginEvent.tokenOverdueTime asc")
    List<SecurityLoginEvent> findByUserIdAndTokenOverdueTime(@Param("userId") Long userId, @Param("tokenOverdueTime") ZonedDateTime tokenOverdueTime);
}
