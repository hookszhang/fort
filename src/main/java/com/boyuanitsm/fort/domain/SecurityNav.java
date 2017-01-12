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

package com.boyuanitsm.fort.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SecurityNav.
 */
@Entity
@Table(name = "security_nav")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "securitynav")
public class SecurityNav extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "icon")
    private String icon;

    @Column(name = "description")
    private String description;

    @Size(max = 60)
    @Column(name = "st", length = 60)
    private String st;

    @Column(name = "position")
    private Double position;

    @ManyToOne
    private SecurityNav parent;

    @ManyToOne
    private SecurityResourceEntity resource;

    @ManyToOne
    private SecurityApp app;

    public SecurityNav() {
    }

    public SecurityNav(Long id, String appKey) {
        this.id = id;
        this.app = new SecurityApp();
        app.setAppKey(appKey);
    }

    public Double getPosition() {
        return position;
    }

    public void setPosition(Double position) {
        this.position = position;
    }

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public SecurityNav getParent() {
        return parent;
    }

    public void setParent(SecurityNav securityNav) {
        this.parent = securityNav;
    }

    public SecurityResourceEntity getResource() {
        return resource;
    }

    public void setResource(SecurityResourceEntity securityResourceEntity) {
        this.resource = securityResourceEntity;
    }

    public SecurityApp getApp() {
        return app;
    }

    public void setApp(SecurityApp securityApp) {
        this.app = securityApp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecurityNav securityNav = (SecurityNav) o;
        if(securityNav.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, securityNav.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SecurityNav{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", icon='" + icon + '\'' +
            ", description='" + description + '\'' +
            ", st='" + st + '\'' +
            ", position=" + position +
            ", parent=" + parent +
            ", resource=" + resource +
            ", app=" + app +
            '}';
    }
}
