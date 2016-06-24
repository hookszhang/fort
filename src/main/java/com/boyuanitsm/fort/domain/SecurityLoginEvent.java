package com.boyuanitsm.fort.domain;

import com.boyuanitsm.fort.config.Constants;
import com.boyuanitsm.fort.service.util.RandomUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * A SecurityLoginEvent.
 */
@Entity
@Table(name = "security_login_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "securityloginevent")
public class SecurityLoginEvent extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "token_value")
    private String tokenValue;

    @Column(name = "token_overdue_time")
    private ZonedDateTime tokenOverdueTime;

    @Size(max = 39)
    @Column(name = "ip_address", length = 39)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @ManyToOne
    private SecurityUser user;

    public SecurityLoginEvent() {
    }

    public SecurityLoginEvent(SecurityUser user, String ipAddress, String userAgent, Long sessionMaxAge) {
        this.user = user;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        // generate token
        this.tokenValue = RandomUtil.generateToken();
        // set token overdue time, now + Constants.TOKEN_EXPIRY_DATE
        this.tokenOverdueTime = ZonedDateTime.ofInstant(new Date(System.currentTimeMillis() + sessionMaxAge).toInstant(), ZoneId.of("UTC+8"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public ZonedDateTime getTokenOverdueTime() {
        return tokenOverdueTime;
    }

    public void setTokenOverdueTime(ZonedDateTime tokenOverdueTime) {
        this.tokenOverdueTime = tokenOverdueTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public SecurityUser getUser() {
        return user;
    }

    public void setUser(SecurityUser securityUser) {
        this.user = securityUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecurityLoginEvent securityLoginEvent = (SecurityLoginEvent) o;
        if (securityLoginEvent.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, securityLoginEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SecurityLoginEvent{" +
            "id=" + id +
            ", tokenValue='" + tokenValue + "'" +
            ", tokenOverdueTime='" + tokenOverdueTime + "'" +
            ", ipAddress='" + ipAddress + "'" +
            ", userAgent='" + userAgent + "'" +
            '}';
    }
}
