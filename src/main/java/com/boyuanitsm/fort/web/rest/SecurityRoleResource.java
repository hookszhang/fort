package com.boyuanitsm.fort.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.boyuanitsm.fort.domain.SecurityRole;
import com.boyuanitsm.fort.service.SecurityRoleService;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SecurityRole.
 */
@RestController
@RequestMapping("/api")
public class SecurityRoleResource {

    private final Logger log = LoggerFactory.getLogger(SecurityRoleResource.class);
        
    @Inject
    private SecurityRoleService securityRoleService;
    
    /**
     * POST  /security-roles : Create a new securityRole.
     *
     * @param securityRole the securityRole to create
     * @return the ResponseEntity with status 201 (Created) and with body the new securityRole, or with status 400 (Bad Request) if the securityRole has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-roles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityRole> createSecurityRole(@Valid @RequestBody SecurityRole securityRole) throws URISyntaxException {
        log.debug("REST request to save SecurityRole : {}", securityRole);
        if (securityRole.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("securityRole", "idexists", "A new securityRole cannot already have an ID")).body(null);
        }
        SecurityRole result = securityRoleService.save(securityRole);
        return ResponseEntity.created(new URI("/api/security-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("securityRole", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /security-roles : Updates an existing securityRole.
     *
     * @param securityRole the securityRole to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated securityRole,
     * or with status 400 (Bad Request) if the securityRole is not valid,
     * or with status 500 (Internal Server Error) if the securityRole couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-roles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityRole> updateSecurityRole(@Valid @RequestBody SecurityRole securityRole) throws URISyntaxException {
        log.debug("REST request to update SecurityRole : {}", securityRole);
        if (securityRole.getId() == null) {
            return createSecurityRole(securityRole);
        }
        SecurityRole result = securityRoleService.save(securityRole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("securityRole", securityRole.getId().toString()))
            .body(result);
    }

    /**
     * GET  /security-roles : get all the securityRoles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of securityRoles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/security-roles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityRole>> getAllSecurityRoles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SecurityRoles");
        Page<SecurityRole> page = securityRoleService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/security-roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /security-roles/:id : get the "id" securityRole.
     *
     * @param id the id of the securityRole to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the securityRole, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/security-roles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityRole> getSecurityRole(@PathVariable Long id) {
        log.debug("REST request to get SecurityRole : {}", id);
        SecurityRole securityRole = securityRoleService.findOne(id);
        return Optional.ofNullable(securityRole)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /security-roles/:id : delete the "id" securityRole.
     *
     * @param id the id of the securityRole to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/security-roles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSecurityRole(@PathVariable Long id) {
        log.debug("REST request to delete SecurityRole : {}", id);
        securityRoleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("securityRole", id.toString())).build();
    }

    /**
     * SEARCH  /_search/security-roles?query=:query : search for the securityRole corresponding
     * to the query.
     *
     * @param query the query of the securityRole search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/security-roles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityRole>> searchSecurityRoles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SecurityRoles for query {}", query);
        Page<SecurityRole> page = securityRoleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/security-roles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
