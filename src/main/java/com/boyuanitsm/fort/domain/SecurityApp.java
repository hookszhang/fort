package com.boyuanitsm.fort.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SecurityApp.
 */
@Entity
@Table(name = "security_app")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "securityapp")
public class SecurityApp extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "app_name", length = 50, nullable = false)
    private String appName;

    @Size(max = 20)
    @Column(name = "app_key", length = 20, updatable = false)
    private String appKey;

    @Size(max = 20)
    @Column(name = "app_secret", length = 20)
    private String appSecret;

    /**
     * Max session number. And the greatest at the same time, the number of login user.
     * Max number is 99.
     */
    @NotNull
    @Column(name = "max_sessions", length = 3, nullable = false)
    private Integer maxSessions;

    /**
     * Session max age. ms
     */
    @NotNull
    @Column(name = "session_max_age", nullable = false)
    private Long sessionMaxAge;

    @Size(max = 60)
    @Column(name = "st", length = 60)
    private String st;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public Integer getMaxSessions() {
        return maxSessions;
    }

    public void setMaxSessions(Integer maxSessions) {
        this.maxSessions = maxSessions;
    }

    public Long getSessionMaxAge() {
        return sessionMaxAge;
    }

    public void setSessionMaxAge(Long sessionMaxAge) {
        this.sessionMaxAge = sessionMaxAge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecurityApp securityApp = (SecurityApp) o;
        if(securityApp.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, securityApp.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SecurityApp{" +
            "id=" + id +
            ", appName='" + appName + '\'' +
            ", appKey='" + appKey + '\'' +
            ", appSecret='" + appSecret + '\'' +
            ", maxSessions=" + maxSessions +
            ", sessionMaxAge=" + sessionMaxAge +
            ", st='" + st + '\'' +
            '}';
    }
}
