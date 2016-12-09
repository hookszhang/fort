package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.security.SecurityUtils;
import com.codahale.metrics.annotation.Timed;
import com.boyuanitsm.fort.domain.SecurityAuthority;
import com.boyuanitsm.fort.service.SecurityAuthorityService;
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
 * REST controller for managing SecurityAuthority.
 */
@RestController
@RequestMapping("/api")
public class SecurityAuthorityResource {

    private final Logger log = LoggerFactory.getLogger(SecurityAuthorityResource.class);

    @Inject
    private SecurityAuthorityService securityAuthorityService;

    /**
     * POST  /security-authorities : Create a new securityAuthority.
     *
     * @param securityAuthority the securityAuthority to create
     * @return the ResponseEntity with status 201 (Created) and with body the new securityAuthority, or with status 400 (Bad Request) if the securityAuthority has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-authorities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityAuthority> createSecurityAuthority(@Valid @RequestBody SecurityAuthority securityAuthority) throws URISyntaxException {
        log.debug("REST request to save SecurityAuthority : {}", securityAuthority);
        if (securityAuthority.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("securityAuthority", "idexists", "A new securityAuthority cannot already have an ID")).body(null);
        }
        if (securityAuthorityService.findByAppAndName(securityAuthority.getApp(), securityAuthority.getName()) != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("securityAuthority", "nameexists", "A new securityAuthority cannot already have an name")).body(null);
        }
        SecurityAuthority result = securityAuthorityService.save(securityAuthority);
        return ResponseEntity.created(new URI("/api/security-authorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("securityAuthority", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /security-authorities : Updates an existing securityAuthority.
     *
     * @param securityAuthority the securityAuthority to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated securityAuthority,
     * or with status 400 (Bad Request) if the securityAuthority is not valid,
     * or with status 500 (Internal Server Error) if the securityAuthority couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-authorities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityAuthority> updateSecurityAuthority(@Valid @RequestBody SecurityAuthority securityAuthority) throws URISyntaxException {
        log.debug("REST request to update SecurityAuthority : {}", securityAuthority);
        if (securityAuthority.getId() == null) {
            return createSecurityAuthority(securityAuthority);
        }
        SecurityAuthority result = securityAuthorityService.save(securityAuthority);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("securityAuthority", securityAuthority.getId().toString()))
            .body(result);
    }

    /**
     * GET  /security-authorities : get all the securityAuthorities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of securityAuthorities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/security-authorities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityAuthority>> getAllSecurityAuthorities(Pageable pageable, Long appId)
        throws URISyntaxException {
        log.debug("REST request to get a page of SecurityAuthorities");
        Page<SecurityAuthority> page = securityAuthorityService.findAll(pageable, appId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/security-authorities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /security-authorities/:id : get the "id" securityAuthority.
     *
     * @param id the id of the securityAuthority to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the securityAuthority, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/security-authorities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityAuthority> getSecurityAuthority(@PathVariable Long id) {
        log.debug("REST request to get SecurityAuthority : {}", id);
        SecurityAuthority securityAuthority = securityAuthorityService.findOne(id);
        return Optional.ofNullable(securityAuthority)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /security-authorities/:id : delete the "id" securityAuthority.
     *
     * @param id the id of the securityAuthority to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/security-authorities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSecurityAuthority(@PathVariable Long id) {
        log.debug("REST request to delete SecurityAuthority : {}", id);
        securityAuthorityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("securityAuthority", id.toString())).build();
    }

    /**
     * SEARCH  /_search/security-authorities?query=:query : search for the securityAuthority corresponding
     * to the query.
     *
     * @param query the query of the securityAuthority search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/security-authorities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityAuthority>> searchSecurityAuthorities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SecurityAuthorities for query {}", query);
        Page<SecurityAuthority> page = securityAuthorityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/security-authorities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Find all this app authorities with eager relationships.
     * Role ROLE_SECURITY_APP dedicated.
     *
     * @return the result of the find
     */
    @RequestMapping(value = "/sa/security-authorities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityAuthority>> findAllWithEagerRelationships() {
        String appKey = SecurityUtils.getCurrentUserLogin();
        List<SecurityAuthority> authorities = securityAuthorityService.findAllByAppKeyWithEagerRelationships(appKey);
        return new ResponseEntity<>(authorities, HttpStatus.OK);
    }
}
