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

import com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption;
import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.domain.SecurityNav;
import com.boyuanitsm.fort.domain.SecurityResourceEntity;
import com.boyuanitsm.fort.repository.SecurityNavRepository;
import com.boyuanitsm.fort.repository.search.SecurityNavSearchRepository;
import com.boyuanitsm.fort.service.util.QueryBuilderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.util.List;

import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceClass.SECURITY_NAV;
import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption.*;

/**
 * Service Implementation for managing SecurityNav.
 */
@Service
@Transactional
public class SecurityNavService {

    private final Logger log = LoggerFactory.getLogger(SecurityNavService.class);

    @Inject
    private SecurityNavRepository securityNavRepository;

    @Inject
    private SecurityNavSearchRepository securityNavSearchRepository;

    @Inject
    private SecurityResourceUpdateService updateService;

    @Inject
    private SecurityAppService securityAppService;

    /**
     * Save a securityNav.
     *
     * @param securityNav the entity to save
     * @return the persisted entity
     */
    public SecurityNav save(SecurityNav securityNav) {
        log.debug("Request to save SecurityNav : {}", securityNav);

        OnUpdateSecurityResourceOption option = securityNav.getId() == null ? POST : PUT;

        SecurityApp app = securityAppService.findCurrentSecurityApp();
        // set app
        if (app != null) {
            securityNav.setApp(app);
        }

        SecurityNav result = securityNavRepository.save(securityNav);
        securityNavSearchRepository.save(result);

        updateService.send(option, SECURITY_NAV, result);
        return result;
    }

    /**
     *  Get all the securityNavs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityNav> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityNavs");
        Page<SecurityNav> result = securityNavRepository.findOwnAll(pageable);
        return result;
    }

    /**
     *  Get one securityNav by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SecurityNav findOne(Long id) {
        log.debug("Request to get SecurityNav : {}", id);
        SecurityNav securityNav = securityNavRepository.findOne(id);
        return securityNav;
    }

    public List<SecurityNav> findByResource(SecurityResourceEntity resource) {
        return securityNavRepository.findByResource(resource);
    }

    /**
     *  Delete the  securityNav by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SecurityNav : {}", id);

        String appKey = securityNavRepository.getOne(id).getApp().getAppKey();

        securityNavRepository.delete(id);
        securityNavSearchRepository.delete(id);

        updateService.send(DELETE, SECURITY_NAV, new SecurityNav(id, appKey));
    }

    /**
     * Search for the securityNav corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityNav> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityNavs for query {}", query);
        return securityNavSearchRepository.search(QueryBuilderUtil.build(query), pageable);
    }

    public List<SecurityNav> findByParentId(Long parentId) {
        return securityNavRepository.findByParentId(parentId);
    }
}
