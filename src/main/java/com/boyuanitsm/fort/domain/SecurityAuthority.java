package com.boyuanitsm.fort.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A SecurityAuthority.
 */
@Entity
@Table(name = "security_authority")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "securityauthority")
public class SecurityAuthority extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Size(max = 60)
    @Column(name = "st", length = 60)
    private String st;

    @ManyToOne
    private SecurityApp app;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "security_authority_resource",
               joinColumns = @JoinColumn(name="security_authorities_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="resources_id", referencedColumnName="ID"))
    private Set<SecurityResourceEntity> resources = new HashSet<>();

    @ManyToMany(mappedBy = "authorities")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SecurityRole> roles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<SecurityResourceEntity> getResources() {
        return resources;
    }

    public void setResources(Set<SecurityResourceEntity> securityResourceEntities) {
        this.resources = securityResourceEntities;
    }

    public Set<SecurityRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SecurityRole> securityRoles) {
        this.roles = securityRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecurityAuthority securityAuthority = (SecurityAuthority) o;
        if(securityAuthority.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, securityAuthority.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SecurityAuthority{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", st='" + st + "'" +
            '}';
    }
}
