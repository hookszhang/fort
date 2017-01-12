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

package com.boyuanitsm.fort.service;

import com.boyuanitsm.fort.bean.OnUpdateSecurityResource;
import com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceClass;
import com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption;
import com.boyuanitsm.fort.config.Constants;
import com.boyuanitsm.fort.domain.*;
import com.boyuanitsm.fort.repository.*;
import com.boyuanitsm.fort.web.rest.dto.SecurityUserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceClass.*;
import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption.DELETE;

/**
 * Security resource update service. on update security resource, send web socket stomp message.
 *
 * @author zhanghua on 5/23/16.
 */
@Service
@Transactional(readOnly = true)
public class SecurityResourceUpdateService {

    private final Logger log = LoggerFactory.getLogger(SecurityResourceUpdateService.class);

    @Inject
    private SecurityResourceEntityRepository securityResourceEntityRepository;

    @Inject
    private SecurityAuthorityRepository securityAuthorityRepository;

    @Inject
    private SecurityRoleRepository securityRoleRepository;

    @Inject
    private SimpMessageSendingOperations messagingTemplate;

    private ObjectMapper mapper;

    @Inject
    public SecurityResourceUpdateService(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        mapper = jackson2ObjectMapperBuilder.build();
    }

    /**
     * send web socket stomp message.
     *
     * @param option the option.
     * @param resourceClass the resourceClass.
     * @param data the data.
     */
    public void send(OnUpdateSecurityResourceOption option, OnUpdateSecurityResourceClass resourceClass, Object data) {
        String appKey;

        if (SECURITY_RESOURCE_ENTITY.equals(resourceClass)) {// update resource entity
            SecurityResourceEntity resourceEntity = (SecurityResourceEntity) data;
            appKey = resourceEntity.getApp().getAppKey();
            if (!DELETE.equals(option)) {
                // find eager relationships
                data = securityResourceEntityRepository.findOneWithEagerRelationships(resourceEntity.getId());
            }
        } else if (SECURITY_NAV.equals(resourceClass)) {// update nav
            SecurityNav nav = (SecurityNav) data;
            appKey = nav.getApp().getAppKey();
        } else if (SECURITY_AUTHORITY.equals(resourceClass)) {// update authority
            SecurityAuthority authority = (SecurityAuthority) data;
            appKey = authority.getApp().getAppKey();
            if (!DELETE.equals(option)) {
                // find eager relationships
                data = securityAuthorityRepository.findOneWithEagerRelationships(authority.getId());
            }
        } else if (SECURITY_GROUP.equals(resourceClass)) {// update group
            SecurityGroup group = (SecurityGroup) data;
            appKey = group.getApp().getAppKey();
        } else if (SECURITY_ROLE.equals(resourceClass)) {// update role
            SecurityRole role = (SecurityRole) data;
            appKey = role.getApp().getAppKey();
            if (!DELETE.equals(option)) {
                // find eager relationships
                data = securityRoleRepository.findOneWithEagerRelationships(role.getId());
            }
        } else if (SECURITY_USER.equals(resourceClass)) {
            SecurityUserDTO userDTO = (SecurityUserDTO) data;
            appKey = userDTO.getAppKey();
        } else {
            // warning: we don't have this resource class
            log.warn("We don't have this resource class: {}", resourceClass);
            return;
        }

        OnUpdateSecurityResource onUpdateSecurityResource = new OnUpdateSecurityResource(option, resourceClass, data);
        try {
            String json = mapper.writeValueAsString(onUpdateSecurityResource);
            messagingTemplate.convertAndSend(String.format(Constants.ON_UPDATE_SECURITY_RESOURCE_SEND, appKey), json);
        } catch (JsonProcessingException e) {
            log.warn("Send resource update message error!", e);
        }
    }
}
