package com.boyuanitsm.fort.domain;

import com.boyuanitsm.fort.config.Constants;
import com.boyuanitsm.fort.domain.util.RandomUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;
import sun.misc.BASE64Encoder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Random;

/**
 * A SecurityLoginEvent.
 */
@Entity
@Table(name = "security_login_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "securityloginevent")
public class SecurityLoginEvent extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public SecurityLoginEvent(SecurityUser user, String ipAddress, String userAgent) {
        this.user = user;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.tokenValue = RandomUtils.generateToken(Constants.SECURITY_TOKEN_LENGTH);
    }

    private String generateTokenData() {
        byte[] newToken = new byte[30];
        Random random = new Random();
        random.nextBytes(newToken);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(newToken);
    }

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
