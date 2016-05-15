package com.boyuanitsm.fort.service;

import com.boyuanitsm.fort.config.Constants;
import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.repository.SecurityAppRepository;
import com.boyuanitsm.fort.repository.search.SecurityAppSearchRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
            securityApp.setAppKey(RandomStringUtils.randomAlphanumeric(Constants.GENERATE_APP_KEY_LENGTH));
            securityApp.setAppSecret(RandomStringUtils.randomAlphanumeric(Constants.GENERATE_APP_KEY_LENGTH));
            log.debug("Generate app key & app secret finished SecurityApp : {}", securityApp);
        }

        SecurityApp result = securityAppRepository.save(securityApp);
        securityAppSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the securityApps.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityApp> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityApps");
        Page<SecurityApp> result = securityAppRepository.findOwnAll(pageable);
        return result;
    }

    /**
     *  Get one securityApp by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SecurityApp findOne(Long id) {
        log.debug("Request to get SecurityApp : {}", id);
        SecurityApp securityApp = securityAppRepository.findOne(id);
        return securityApp;
    }

    /**
     *  Delete the  securityApp by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SecurityApp : {}", id);
        securityAppRepository.delete(id);
        securityAppSearchRepository.delete(id);
    }

    /**
     * Search for the securityApp corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityApp> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityApps for query {}", query);
        return securityAppSearchRepository.search(queryStringQuery(query), pageable);
    }
}
