package com.boyuanitsm.fort.service;

import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.repository.SecurityAppRepository;
import com.boyuanitsm.fort.repository.search.SecurityAppSearchRepository;
import com.boyuanitsm.fort.security.AuthoritiesConstants;
import com.boyuanitsm.fort.security.SecurityUtils;
import com.boyuanitsm.fort.service.util.QueryBuilderUtil;
import com.boyuanitsm.fort.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Service Implementation for managing SecurityApp.
 */
@Service
@Transactional
public class SecurityAppService {

    private final Logger log = LoggerFactory.getLogger(SecurityAppService.class);

    @Inject
    private SecurityAppRepository securityAppRepository;

    @Inject
    private SecurityAppSearchRepository securityAppSearchRepository;

    @Inject
    private UserService userService;

    /**
     * Save a securityApp.
     *
     * @param securityApp the entity to save
     * @return the persisted entity
     */
    public SecurityApp save(SecurityApp securityApp) {
        log.debug("Request to save SecurityApp : {}", securityApp);

        if (securityApp.getId() == null) {
            // generate app key & app secret
            String appKey = RandomUtil.generateAppKey();
            String appSecret = RandomUtil.generateAppSecret();
            // set security app
            securityApp.setAppKey(appKey);
            securityApp.setAppSecret(appSecret);
            log.debug("Generate app key & app secret finished SecurityApp : {}", securityApp);
            // create new user
            userService.createSecurityAppUser(appKey, appSecret);
        }

        SecurityApp result = securityAppRepository.save(securityApp);
        securityAppSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the securityApps.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityApp> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityApps");
        Page<SecurityApp> result = securityAppRepository.findOwnAll(pageable);
        return result;
    }

    /**
     * Get one securityApp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SecurityApp findOne(Long id) {
        log.debug("Request to get SecurityApp : {}", id);
        SecurityApp securityApp = securityAppRepository.findOne(id);
        return securityApp;
    }

    /**
     * Delete the  securityApp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        SecurityApp app = securityAppRepository.findOne(id);

        if (app != null) {
            // delete user
            userService.deleteUserInformation(app.getAppKey());
        }

        log.debug("Request to delete SecurityApp : {}", id);
        securityAppRepository.delete(id);
        securityAppSearchRepository.delete(id);
    }

    /**
     * Search for the securityApp corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityApp> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityApps for query {}", query);
        return securityAppSearchRepository.search(QueryBuilderUtil.build(query), pageable);
    }

    public SecurityApp findByAppKey(String appKey) {
        return securityAppRepository.findByAppKey(appKey);
    }

    /**
     * Find current logged app. if current logged not app. return null.
     *
     * @return the current logged app.
     */
    @Transactional(readOnly = true)
    public SecurityApp findCurrentSecurityApp() {
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SECURITY_APP)) {
            return null;
        }

        String appKey = SecurityUtils.getCurrentUserLogin();
        return securityAppRepository.findByAppKey(appKey);
    }
}
