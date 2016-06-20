package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.security.AuthoritiesConstants;
import com.boyuanitsm.fort.security.SecurityUtils;
import com.boyuanitsm.fort.service.SecurityAppService;
import com.codahale.metrics.annotation.Timed;
import com.boyuanitsm.fort.domain.SecurityResourceEntity;
import com.boyuanitsm.fort.service.SecurityResourceEntityService;
import com.boyuanitsm.fort.web.rest.util.HeaderUtil;
import com.boyuanitsm.fort.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SecurityResourceEntity.
 */
@RestController
@RequestMapping("/api")
public class SecurityResourceEntityResource {

    private final Logger log = LoggerFactory.getLogger(SecurityResourceEntityResource.class);

    @Inject
    private SecurityResourceEntityService securityResourceEntityService;

    @Inject
    private SecurityAppService securityAppService;

    /**
     * POST  /security-resource-entities : Create a new securityResourceEntity.
     *
     * @param securityResourceEntity the securityResourceEntity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new securityResourceEntity, or with status 400 (Bad Request) if the securityResourceEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-resource-entities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityResourceEntity> createSecurityResourceEntity(@Valid @RequestBody SecurityResourceEntity securityResourceEntity) throws URISyntaxException {
        log.debug("REST request to save SecurityResourceEntity : {}", securityResourceEntity);
        if (securityResourceEntity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("securityResourceEntity", "idexists", "A new securityResourceEntity cannot already have an ID")).body(null);
        }
        // unique url on same app
        if (securityResourceEntityService.findByAppAndUrl(
                securityResourceEntity.getApp(), securityResourceEntity.getUrl()) != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("securityResourceEntity", "urlexists", "A new securityResourceEntity cannot already have an url")).body(null);
        }
        // set current app
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SECURITY_APP)) {
            securityResourceEntity.setApp(securityAppService.findCurrentSecurityApp());
        }
        SecurityResourceEntity result = securityResourceEntityService.save(securityResourceEntity);
        return ResponseEntity.created(new URI("/api/security-resource-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("securityResourceEntity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /security-resource-entities : Updates an existing securityResourceEntity.
     *
     * @param securityResourceEntity the securityResourceEntity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated securityResourceEntity,
     * or with status 400 (Bad Request) if the securityResourceEntity is not valid,
     * or with status 500 (Internal Server Error) if the securityResourceEntity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-resource-entities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityResourceEntity> updateSecurityResourceEntity(@Valid @RequestBody SecurityResourceEntity securityResourceEntity) throws URISyntaxException {
        log.debug("REST request to update SecurityResourceEntity : {}", securityResourceEntity);
        if (securityResourceEntity.getId() == null) {
            return createSecurityResourceEntity(securityResourceEntity);
        }
        // set current app
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SECURITY_APP)) {
            securityResourceEntity.setApp(securityAppService.findCurrentSecurityApp());
        }
        SecurityResourceEntity result = securityResourceEntityService.save(securityResourceEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("securityResourceEntity", securityResourceEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /security-resource-entities : get all the securityResourceEntities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of securityResourceEntities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/security-resource-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityResourceEntity>> getAllSecurityResourceEntities(Pageable pageable, Long appId)
        throws URISyntaxException {
        log.debug("REST request to get a page of SecurityResourceEntities");
        Page<SecurityResourceEntity> page = securityResourceEntityService.findAll(pageable, appId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/security-resource-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /security-resource-entities/:id : get the "id" securityResourceEntity.
     *
     * @param id the id of the securityResourceEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the securityResourceEntity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/security-resource-entities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityResourceEntity> getSecurityResourceEntity(@PathVariable Long id) {
        log.debug("REST request to get SecurityResourceEntity : {}", id);
        SecurityResourceEntity securityResourceEntity = securityResourceEntityService.findOne(id);
        return Optional.ofNullable(securityResourceEntity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /security-resource-entities/:id : delete the "id" securityResourceEntity.
     *
     * @param id the id of the securityResourceEntity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/security-resource-entities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSecurityResourceEntity(@PathVariable Long id) {
        log.debug("REST request to delete SecurityResourceEntity : {}", id);
        securityResourceEntityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("securityResourceEntity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/security-resource-entities?query=:query : search for the securityResourceEntity corresponding
     * to the query.
     *
     * @param query the query of the securityResourceEntity search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/security-resource-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityResourceEntity>> searchSecurityResourceEntities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SecurityResourceEntities for query {}", query);
        Page<SecurityResourceEntity> page = securityResourceEntityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/security-resource-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Find all this app resource entities with eager relationships.
     * Role ROLE_SECURITY_APP dedicated.
     *
     * @return the result of the find
     */
    @RequestMapping(value = "/sa/security-resource-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityResourceEntity>> findAllWithEagerRelationships() {
        String appKey = SecurityUtils.getCurrentUserLogin();
        List<SecurityResourceEntity> resourceEntities = securityResourceEntityService.findAllByAppKeyWithEagerRelationships(appKey);
        return new ResponseEntity<>(resourceEntities, HttpStatus.OK);
    }
}
