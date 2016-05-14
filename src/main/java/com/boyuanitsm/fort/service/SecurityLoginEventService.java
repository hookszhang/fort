package com.boyuanitsm.fort.service;

import com.boyuanitsm.fort.domain.SecurityLoginEvent;
import com.boyuanitsm.fort.repository.SecurityLoginEventRepository;
import com.boyuanitsm.fort.repository.search.SecurityLoginEventSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SecurityLoginEvent.
 */
@Service
@Transactional
public class SecurityLoginEventService {

    private final Logger log = LoggerFactory.getLogger(SecurityLoginEventService.class);
    
    @Inject
    private SecurityLoginEventRepository securityLoginEventRepository;
    
    @Inject
    private SecurityLoginEventSearchRepository securityLoginEventSearchRepository;
    
    /**
     * Save a securityLoginEvent.
     * 
     * @param securityLoginEvent the entity to save
     * @return the persisted entity
     */
    public SecurityLoginEvent save(SecurityLoginEvent securityLoginEvent) {
        log.debug("Request to save SecurityLoginEvent : {}", securityLoginEvent);
        SecurityLoginEvent result = securityLoginEventRepository.save(securityLoginEvent);
        securityLoginEventSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the securityLoginEvents.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<SecurityLoginEvent> findAll() {
        log.debug("Request to get all SecurityLoginEvents");
        List<SecurityLoginEvent> result = securityLoginEventRepository.findAll();
        return result;
    }

    /**
     *  Get one securityLoginEvent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SecurityLoginEvent findOne(Long id) {
        log.debug("Request to get SecurityLoginEvent : {}", id);
        SecurityLoginEvent securityLoginEvent = securityLoginEventRepository.findOne(id);
        return securityLoginEvent;
    }

    /**
     *  Delete the  securityLoginEvent by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SecurityLoginEvent : {}", id);
        securityLoginEventRepository.delete(id);
        securityLoginEventSearchRepository.delete(id);
    }

    /**
     * Search for the securityLoginEvent corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SecurityLoginEvent> search(String query) {
        log.debug("Request to search SecurityLoginEvents for query {}", query);
        return StreamSupport
            .stream(securityLoginEventSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
