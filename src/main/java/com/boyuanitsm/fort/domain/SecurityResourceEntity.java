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

import com.boyuanitsm.fort.domain.enumeration.ResourceEntityType;

/**
 * A SecurityResourceEntity.
 */
@Entity
@Table(name = "security_resource_entity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "securityresourceentity")
public class SecurityResourceEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public SecurityResourceEntity() {
    }

    public SecurityResourceEntity(Long id) {
        this.id = id;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type")
    private ResourceEntityType resourceType;

    @Size(max = 60)
    @Column(name = "st", length = 60)
    private String st;

    @ManyToOne
    private SecurityApp app;

    @ManyToMany(mappedBy = "resources")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SecurityAuthority> authorities = new HashSet<>();

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResourceEntityType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceEntityType resourceType) {
        this.resourceType = resourceType;
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

    public Set<SecurityAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<SecurityAuthority> securityAuthorities) {
        this.authorities = securityAuthorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecurityResourceEntity securityResourceEntity = (SecurityResourceEntity) o;
        if(securityResourceEntity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, securityResourceEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SecurityResourceEntity{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", url='" + url + "'" +
            ", description='" + description + "'" +
            ", resourceType='" + resourceType + "'" +
            ", st='" + st + "'" +
            '}';
    }
}
