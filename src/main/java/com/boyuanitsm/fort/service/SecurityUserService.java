package com.boyuanitsm.fort.service;

import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.domain.SecurityUser;
import com.boyuanitsm.fort.repository.SecurityAppRepository;
import com.boyuanitsm.fort.repository.SecurityUserRepository;
import com.boyuanitsm.fort.repository.search.SecurityUserSearchRepository;
import com.boyuanitsm.fort.security.AuthoritiesConstants;
import com.boyuanitsm.fort.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SecurityUser.
 */
@Service
@Transactional
public class SecurityUserService {

    private final Logger log = LoggerFactory.getLogger(SecurityUserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private SecurityUserRepository securityUserRepository;

    @Inject
    private SecurityUserSearchRepository securityUserSearchRepository;

    @Inject
    private SecurityAppRepository securityAppRepository;

    /**
     * Save a securityUser.
     *
     * @param securityUser the entity to save
     * @return the persisted entity
     */
    public SecurityUser save(SecurityUser securityUser) {
        log.debug("Request to save SecurityUser : {}", securityUser);

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SECURITY_APP)) {
            // set app
            String appKey = SecurityUtils.getCurrentUserLogin();
            SecurityApp app = securityAppRepository.findByAppKey(appKey);
            securityUser.setApp(app);
        }

        // encode password
        String passwordHash = securityUser.getPasswordHash();
        securityUser.setPasswordHash(passwordEncoder.encode(passwordHash));

        SecurityUser result = securityUserRepository.save(securityUser);
        securityUserSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the securityUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityUser> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityUsers");
        Page<SecurityUser> result = securityUserRepository.findOwnAll(pageable);
        return result;
    }

    /**
     *  Get one securityUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SecurityUser findOne(Long id) {
        log.debug("Request to get SecurityUser : {}", id);
        SecurityUser securityUser = securityUserRepository.findOneWithEagerRelationships(id);
        return securityUser;
    }

    /**
     *  Delete the  securityUser by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SecurityUser : {}", id);
        securityUserRepository.delete(id);
        securityUserSearchRepository.delete(id);
    }

    /**
     * Search for the securityUser corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityUser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityUsers for query {}", query);
        return securityUserSearchRepository.search(queryStringQuery(query), pageable);
    }

    /**
     * Is authorization access app server
     *
     * @param login security user login
     * @param password security user password
     * @return if authorization success return SecurityUser else return null
     */
    @Transactional(readOnly = true)
    public SecurityUser authorization(String login, String password) {
        // get current logged appKey
        SecurityApp app = securityAppRepository.findByAppKey(SecurityUtils.getCurrentUserLogin());
        // get user
        SecurityUser user = securityUserRepository.findByLoginAndApp(login, app);
        if (user == null) {
            return null;
        }

        if (passwordEncoder.matches(password, user.getPasswordHash())){
            user = securityUserRepository.findOneWithEagerRelationships(user.getId());
            return user;
        }

        return null;
    }
}
