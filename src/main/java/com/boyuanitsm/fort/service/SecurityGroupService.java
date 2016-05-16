package com.boyuanitsm.fort.service;

import com.boyuanitsm.fort.domain.SecurityGroup;
import com.boyuanitsm.fort.repository.SecurityGroupRepository;
import com.boyuanitsm.fort.repository.search.SecurityGroupSearchRepository;
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
 * Service Implementation for managing SecurityGroup.
 */
@Service
@Transactional
public class SecurityGroupService {

    private final Logger log = LoggerFactory.getLogger(SecurityGroupService.class);
    
    @Inject
    private SecurityGroupRepository securityGroupRepository;
    
    @Inject
    private SecurityGroupSearchRepository securityGroupSearchRepository;
    
    /**
     * Save a securityGroup.
     * 
     * @param securityGroup the entity to save
     * @return the persisted entity
     */
    public SecurityGroup save(SecurityGroup securityGroup) {
        log.debug("Request to save SecurityGroup : {}", securityGroup);
        SecurityGroup result = securityGroupRepository.save(securityGroup);
        securityGroupSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the securityGroups.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<SecurityGroup> findAll(Pageable pageable) {
        log.debug("Request to get all SecurityGroups");
        Page<SecurityGroup> result = securityGroupRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one securityGroup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SecurityGroup findOne(Long id) {
        log.debug("Request to get SecurityGroup : {}", id);
        SecurityGroup securityGroup = securityGroupRepository.findOne(id);
        return securityGroup;
    }

    /**
     *  Delete the  securityGroup by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SecurityGroup : {}", id);
        securityGroupRepository.delete(id);
        securityGroupSearchRepository.delete(id);
    }

    /**
     * Search for the securityGroup corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityGroup> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SecurityGroups for query {}", query);
        return securityGroupSearchRepository.search(queryStringQuery(query), pageable);
    }
}
