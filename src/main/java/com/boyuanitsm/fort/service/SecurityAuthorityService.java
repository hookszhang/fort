package com.boyuanitsm.fort.service;

import com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption;
import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.domain.SecurityAuthority;
import com.boyuanitsm.fort.repository.SecurityAuthorityRepository;
import com.boyuanitsm.fort.repository.search.SecurityAuthoritySearchRepository;
import com.boyuanitsm.fort.service.util.QueryBuilderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceClass.SECURITY_AUTHORITY;
import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption.*;

/**
 * Service Implementation for managing SecurityAuthority.
 */
@Service
@Transactional
public class SecurityAuthorityService {

    private final Logger log = LoggerFactory.getLogger(SecurityAuthorityService.class);

    @Inject
    private SecurityAuthorityRepository securityAuthorityRepository;

    @Inject
    private SecurityAuthoritySearchRepository securityAuthoritySearchRepository;

    @Inject
    private SecurityResourceUpdateService updateService;

    /**
     * Save a securityAuthority.
     *
     * @param securityAuthority the entity to save
     * @return the persisted entity
     */
    public SecurityAuthority save(SecurityAuthority securityAuthority) {
        log.debug("Request to save SecurityAuthority : {}", securityAuthority);

        OnUpdateSecurityResourceOption option = securityAuthority == null ? POST : PUT;

        SecurityAuthority result = securityAuthorityRepository.save(securityAuthority);
        securityAuthoritySearchRepository.save(result);

        updateService.send(option, SECURITY_AUTHORITY, result);
        return result;
    }

    /**
     * Get all the securityAuthorities.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityAuthority> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityAuthorities");
        Page<SecurityAuthority> result = securityAuthorityRepository.findOwnAll(pageable);
        return result;
    }

    /**
     * Get one securityAuthority by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SecurityAuthority findOne(Long id) {
        log.debug("Request to get SecurityAuthority : {}", id);
        SecurityAuthority securityAuthority = securityAuthorityRepository.findOneWithEagerRelationships(id);
        return securityAuthority;
    }

    /**
     * Delete the  securityAuthority by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SecurityAuthority : {}", id);
        String appKey = securityAuthorityRepository.getOne(id).getApp().getAppKey();

        securityAuthorityRepository.delete(id);
        securityAuthoritySearchRepository.delete(id);

        updateService.send(DELETE, SECURITY_AUTHORITY, new SecurityAuthority(id, appKey));
    }

    /**
     * Search for the securityAuthority corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityAuthority> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityAuthorities for query {}", query);
        return securityAuthoritySearchRepository.search(QueryBuilderUtil.build(query), pageable);
    }

    /**
     * Find this app all authorities with eager relationships.
     *
     * @param appKey the appKey of the SecurityApp
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SecurityAuthority> findAllByAppKeyWithEagerRelationships(String appKey) {
        return securityAuthorityRepository.findAllByAppKeyWithEagerRelationships(appKey);
    }

    public SecurityAuthority findByAppAndName(SecurityApp app, String name) {
        return securityAuthorityRepository.findByAppAndName(app, name);
    }
}
