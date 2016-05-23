package com.boyuanitsm.fort.service;

import com.alibaba.fastjson.JSON;
import com.boyuanitsm.fort.bean.OnUpdateSecurityResource;
import com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceClass;
import com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption;
import com.boyuanitsm.fort.config.Constants;
import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.domain.SecurityResourceEntity;
import com.boyuanitsm.fort.repository.SecurityResourceEntityRepository;
import com.boyuanitsm.fort.repository.search.SecurityResourceEntitySearchRepository;
import com.fasterxml.jackson.core.JsonEncoding;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SecurityResourceEntity.
 */
@Service
@Transactional
public class SecurityResourceEntityService {

    private final Logger log = LoggerFactory.getLogger(SecurityResourceEntityService.class);

    @Inject
    private SecurityResourceEntityRepository securityResourceEntityRepository;

    @Inject
    private SecurityResourceEntitySearchRepository securityResourceEntitySearchRepository;

    @Inject
    private SimpMessageSendingOperations messagingTemplate;

    /**
     * Save a securityResourceEntity.
     *
     * @param securityResourceEntity the entity to save
     * @return the persisted entity
     */
    public SecurityResourceEntity save(SecurityResourceEntity securityResourceEntity) {
        log.debug("Request to save SecurityResourceEntity : {}", securityResourceEntity);

        OnUpdateSecurityResourceOption option;
        if (securityResourceEntity.getId() == null) {
            option = OnUpdateSecurityResourceOption.POST;
        } else {
            option = OnUpdateSecurityResourceOption.PUT;
        }

        SecurityResourceEntity result = securityResourceEntityRepository.save(securityResourceEntity);
        securityResourceEntitySearchRepository.save(result);

        OnUpdateSecurityResource onUpdateSecurityResource = new OnUpdateSecurityResource(option, OnUpdateSecurityResourceClass.SECURITY_RESOURCE_ENTITY, result);
        messagingTemplate.convertAndSend(String.format(Constants.ON_UPDATE_SECURITY_RESOURCE_SEND, result.getApp().getAppKey()), JSON.toJSONString(onUpdateSecurityResource));
        return result;
    }

    /**
     *  Get all the securityResourceEntities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityResourceEntity> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityResourceEntities");
        Page<SecurityResourceEntity> result = securityResourceEntityRepository.findOwnAll(pageable);
        return result;
    }

    /**
     *  Get one securityResourceEntity by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SecurityResourceEntity findOne(Long id) {
        log.debug("Request to get SecurityResourceEntity : {}", id);
        SecurityResourceEntity securityResourceEntity = securityResourceEntityRepository.findOne(id);
        return securityResourceEntity;
    }

    /**
     *  Delete the  securityResourceEntity by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SecurityResourceEntity : {}", id);
        securityResourceEntityRepository.delete(id);
        securityResourceEntitySearchRepository.delete(id);
    }

    /**
     * Search for the securityResourceEntity corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityResourceEntity> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityResourceEntities for query {}", query);
        return securityResourceEntitySearchRepository.search(queryStringQuery(query), pageable);
    }

    @Transactional(readOnly = true)
    public SecurityResourceEntity findByAppAndUrl(SecurityApp app, String url) {
        return securityResourceEntityRepository.findByAppAndUrl(app, url);
    }

    /**
     * Find this app all resource entity with eager relationships.
     *
     * @param appKey the appKey of the SecurityApp
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SecurityResourceEntity> findAllByAppKeyWithEagerRelationships(String appKey) {
        return securityResourceEntityRepository.findAllByAppKeyWithEagerRelationships(appKey);
    }
}
