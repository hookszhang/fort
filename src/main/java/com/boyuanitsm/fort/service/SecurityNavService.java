package com.boyuanitsm.fort.service;

import com.boyuanitsm.fort.domain.SecurityNav;
import com.boyuanitsm.fort.repository.SecurityNavRepository;
import com.boyuanitsm.fort.repository.search.SecurityNavSearchRepository;
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

    /**
     * Save a securityNav.
     *
     * @param securityNav the entity to save
     * @return the persisted entity
     */
    public SecurityNav save(SecurityNav securityNav) {
        log.debug("Request to save SecurityNav : {}", securityNav);
        SecurityNav result = securityNavRepository.save(securityNav);
        securityNavSearchRepository.save(result);
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

    /**
     *  Delete the  securityNav by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SecurityNav : {}", id);
        securityNavRepository.delete(id);
        securityNavSearchRepository.delete(id);
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
        return securityNavSearchRepository.search(queryStringQuery(query), pageable);
    }
}
