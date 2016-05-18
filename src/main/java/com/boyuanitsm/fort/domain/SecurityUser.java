package com.boyuanitsm.fort.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SecurityUser.
 */
@Entity
@Table(name = "security_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "securityuser")
public class SecurityUser extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "login", length = 50, nullable = false, updatable = false)
    private String login;

    @NotNull
    @Size(max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String passwordHash;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @NotNull
    @Column(name = "activated", nullable = false)
    private Boolean activated;

    @Size(max = 60)
    @Column(name = "st", length = 60)
    private String st;

    @ManyToOne
    private SecurityApp app;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "security_user_role",
               joinColumns = @JoinColumn(name="security_users_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="roles_id", referencedColumnName="ID"))
    private Set<SecurityRole> roles = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "security_user_group",
               joinColumns = @JoinColumn(name="security_users_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="groups_id", referencedColumnName="ID"))
    private Set<SecurityGroup> groups = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public SecurityApp getApp() {
        return app;
    }

    public void setApp(SecurityApp securityApp) {
        this.app = securityApp;
    }

    public Set<SecurityRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SecurityRole> securityRoles) {
        this.roles = securityRoles;
    }

    public Set<SecurityGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<SecurityGroup> securityGroups) {
        this.groups = securityGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecurityUser securityUser = (SecurityUser) o;
        if(securityUser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, securityUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SecurityUser{" +
            "id=" + id +
            ", login='" + login + "'" +
            ", passwordHash='" + passwordHash + "'" +
            ", email='" + email + "'" +
            ", activated='" + activated + "'" +
            ", st='" + st + "'" +
            '}';
    }
}
