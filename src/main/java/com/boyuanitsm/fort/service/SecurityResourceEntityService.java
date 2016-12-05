package com.boyuanitsm.fort.service;

import com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption;
import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.domain.SecurityResourceEntity;
import com.boyuanitsm.fort.repository.SecurityResourceEntityRepository;
import com.boyuanitsm.fort.repository.search.SecurityResourceEntitySearchRepository;
import com.boyuanitsm.fort.service.util.QueryBuilderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceClass.SECURITY_RESOURCE_ENTITY;
import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption.*;

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
    private SecurityResourceUpdateService updateService;

    /**
     * Save a securityResourceEntity.
     *
     * @param securityResourceEntity the entity to save
     * @return the persisted entity
     */
    public SecurityResourceEntity save(SecurityResourceEntity securityResourceEntity) {
        log.debug("Request to save SecurityResourceEntity : {}", securityResourceEntity);

        OnUpdateSecurityResourceOption option = securityResourceEntity.getId() == null ? POST: PUT;

        SecurityResourceEntity result = securityResourceEntityRepository.save(securityResourceEntity);
        securityResourceEntitySearchRepository.save(result);

        updateService.send(option, SECURITY_RESOURCE_ENTITY, result);
        return result;
    }

    /**
     *  Get all the securityResourceEntities.
     *
     *  @param pageable the pagination information
     *  @param appId the app of the id
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityResourceEntity> findAll(Pageable pageable, Long appId) {
        log.debug("Request to get all SecurityResourceEntities");

        if (appId != null) {
            return securityResourceEntityRepository.findAllByAppId(pageable, appId);
        } else {
            return securityResourceEntityRepository.findOwnAll(pageable);
        }
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

    @Transactional(readOnly = true)
    public SecurityResourceEntity findOneWithEagerRelationships(Long id) {
        log.debug("Request to get SecurityResourceEntity : {}", id);
        SecurityResourceEntity securityResourceEntity = securityResourceEntityRepository.findOneWithEagerRelationships(id);
        return securityResourceEntity;
    }

    /**
     *  Delete the  securityResourceEntity by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SecurityResourceEntity : {}", id);
        String appKey = securityResourceEntityRepository.getOne(id).getApp().getAppKey();

        securityResourceEntityRepository.delete(id);
        securityResourceEntitySearchRepository.delete(id);

        updateService.send(DELETE, SECURITY_RESOURCE_ENTITY, new SecurityResourceEntity(id, appKey));
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
        return securityResourceEntitySearchRepository.search(QueryBuilderUtil.build(query), pageable);
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
