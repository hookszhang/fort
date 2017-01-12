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

package com.boyuanitsm.fort.web.rest.dto;

import com.boyuanitsm.fort.domain.SecurityGroup;
import com.boyuanitsm.fort.domain.SecurityLoginEvent;
import com.boyuanitsm.fort.domain.SecurityRole;
import com.boyuanitsm.fort.domain.SecurityUser;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO representing a security user, with his authorities, roles.
 *
 * @author zhanghua on 5/24/16.
 */
public class SecurityUserDTO {

    private Long id;

    private String login;

    private String passwordHash;

    private String email;

    private Set<SecurityRole> roles = new HashSet<>();

    private Set<SecurityGroup> groups = new HashSet<>();

    private String ipAddress;

    private String userAgent;

    private String token;

    private Long tokenOverdueTime;

    private String appKey;

    public SecurityUserDTO() {
    }

    public SecurityUserDTO(SecurityUser user, SecurityLoginEvent event) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.passwordHash = "[protect]";
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.groups = user.getGroups();
        this.ipAddress = event.getIpAddress();
        this.userAgent = event.getUserAgent();
        this.token = event.getTokenValue();
        this.tokenOverdueTime = Date.from(event.getTokenOverdueTime().toInstant()).getTime();
        this.appKey = user.getApp().getAppKey();
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<SecurityRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }

    public Set<SecurityGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<SecurityGroup> groups) {
        this.groups = groups;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Long getTokenOverdueTime() {
        return tokenOverdueTime;
    }

    public void setTokenOverdueTime(Long tokenOverdueTime) {
        this.tokenOverdueTime = tokenOverdueTime;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
