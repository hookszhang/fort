package com.boyuanitsm.fort.service;

import com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption;
import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.domain.SecurityGroup;
import com.boyuanitsm.fort.repository.SecurityGroupRepository;
import com.boyuanitsm.fort.repository.search.SecurityGroupSearchRepository;
import com.boyuanitsm.fort.service.util.QueryBuilderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceClass.SECURITY_GROUP;
import static com.boyuanitsm.fort.bean.enumeration.OnUpdateSecurityResourceOption.*;

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

    @Inject
    private SecurityResourceUpdateService updateService;

    @Inject
    private SecurityAppService securityAppService;

    /**
     * Save a securityGroup.
     *
     * @param securityGroup the entity to save
     * @return the persisted entity
     */
    public SecurityGroup save(SecurityGroup securityGroup) {
        log.debug("Request to save SecurityGroup : {}", securityGroup);

        OnUpdateSecurityResourceOption option = securityGroup.getId() == null ? POST : PUT;

        SecurityApp app = securityAppService.findCurrentSecurityApp();
        // set app
        if (app != null) {
            securityGroup.setApp(app);
        }

        SecurityGroup result = securityGroupRepository.save(securityGroup);
        securityGroupSearchRepository.save(result);

        updateService.send(option, SECURITY_GROUP, result);
        return result;
    }

    /**
     *  Get all the securityGroups.
     *
     *  @param pageable the pagination information
     *  @param appId the id of the app
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SecurityGroup> findAll(Pageable pageable, Long appId) {
        log.debug("Request to get all SecurityGroups");
        if (appId != null) {
            return securityGroupRepository.findAllByAppId(pageable, appId);
        } else {
            return securityGroupRepository.findOwnAll(pageable);
        }
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
        String appKey = securityGroupRepository.getOne(id).getApp().getAppKey();

        securityGroupRepository.delete(id);
        securityGroupSearchRepository.delete(id);

        updateService.send(DELETE, SECURITY_GROUP, new SecurityGroup(id, appKey));
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
        return securityGroupSearchRepository.search(QueryBuilderUtil.build(query), pageable);
    }

    public SecurityGroup findByAppAndName(SecurityApp app, String name) {
        return securityGroupRepository.findByAppAndName(app, name);
    }
}
