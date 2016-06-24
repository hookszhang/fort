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
