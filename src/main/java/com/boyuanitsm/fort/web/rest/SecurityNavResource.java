package com.boyuanitsm.fort.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.boyuanitsm.fort.domain.SecurityNav;
import com.boyuanitsm.fort.service.SecurityNavService;
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
 * REST controller for managing SecurityNav.
 */
@RestController
@RequestMapping("/api")
public class SecurityNavResource {

    private final Logger log = LoggerFactory.getLogger(SecurityNavResource.class);
        
    @Inject
    private SecurityNavService securityNavService;
    
    /**
     * POST  /security-navs : Create a new securityNav.
     *
     * @param securityNav the securityNav to create
     * @return the ResponseEntity with status 201 (Created) and with body the new securityNav, or with status 400 (Bad Request) if the securityNav has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-navs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityNav> createSecurityNav(@Valid @RequestBody SecurityNav securityNav) throws URISyntaxException {
        log.debug("REST request to save SecurityNav : {}", securityNav);
        if (securityNav.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("securityNav", "idexists", "A new securityNav cannot already have an ID")).body(null);
        }
        SecurityNav result = securityNavService.save(securityNav);
        return ResponseEntity.created(new URI("/api/security-navs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("securityNav", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /security-navs : Updates an existing securityNav.
     *
     * @param securityNav the securityNav to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated securityNav,
     * or with status 400 (Bad Request) if the securityNav is not valid,
     * or with status 500 (Internal Server Error) if the securityNav couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-navs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityNav> updateSecurityNav(@Valid @RequestBody SecurityNav securityNav) throws URISyntaxException {
        log.debug("REST request to update SecurityNav : {}", securityNav);
        if (securityNav.getId() == null) {
            return createSecurityNav(securityNav);
        }
        SecurityNav result = securityNavService.save(securityNav);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("securityNav", securityNav.getId().toString()))
            .body(result);
    }

    /**
     * GET  /security-navs : get all the securityNavs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of securityNavs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/security-navs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityNav>> getAllSecurityNavs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SecurityNavs");
        Page<SecurityNav> page = securityNavService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/security-navs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /security-navs/:id : get the "id" securityNav.
     *
     * @param id the id of the securityNav to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the securityNav, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/security-navs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityNav> getSecurityNav(@PathVariable Long id) {
        log.debug("REST request to get SecurityNav : {}", id);
        SecurityNav securityNav = securityNavService.findOne(id);
        return Optional.ofNullable(securityNav)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /security-navs/:id : delete the "id" securityNav.
     *
     * @param id the id of the securityNav to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/security-navs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSecurityNav(@PathVariable Long id) {
        log.debug("REST request to delete SecurityNav : {}", id);
        securityNavService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("securityNav", id.toString())).build();
    }

    /**
     * SEARCH  /_search/security-navs?query=:query : search for the securityNav corresponding
     * to the query.
     *
     * @param query the query of the securityNav search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/security-navs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityNav>> searchSecurityNavs(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SecurityNavs for query {}", query);
        Page<SecurityNav> page = securityNavService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/security-navs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}