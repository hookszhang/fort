package com.boyuanitsm.fort.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.boyuanitsm.fort.domain.SecurityLoginEvent;
import com.boyuanitsm.fort.service.SecurityLoginEventService;
import com.boyuanitsm.fort.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing SecurityLoginEvent.
 */
@RestController
@RequestMapping("/api")
public class SecurityLoginEventResource {

    private final Logger log = LoggerFactory.getLogger(SecurityLoginEventResource.class);
        
    @Inject
    private SecurityLoginEventService securityLoginEventService;
    
    /**
     * POST  /security-login-events : Create a new securityLoginEvent.
     *
     * @param securityLoginEvent the securityLoginEvent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new securityLoginEvent, or with status 400 (Bad Request) if the securityLoginEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-login-events",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityLoginEvent> createSecurityLoginEvent(@Valid @RequestBody SecurityLoginEvent securityLoginEvent) throws URISyntaxException {
        log.debug("REST request to save SecurityLoginEvent : {}", securityLoginEvent);
        if (securityLoginEvent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("securityLoginEvent", "idexists", "A new securityLoginEvent cannot already have an ID")).body(null);
        }
        SecurityLoginEvent result = securityLoginEventService.save(securityLoginEvent);
        return ResponseEntity.created(new URI("/api/security-login-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("securityLoginEvent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /security-login-events : Updates an existing securityLoginEvent.
     *
     * @param securityLoginEvent the securityLoginEvent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated securityLoginEvent,
     * or with status 400 (Bad Request) if the securityLoginEvent is not valid,
     * or with status 500 (Internal Server Error) if the securityLoginEvent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-login-events",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityLoginEvent> updateSecurityLoginEvent(@Valid @RequestBody SecurityLoginEvent securityLoginEvent) throws URISyntaxException {
        log.debug("REST request to update SecurityLoginEvent : {}", securityLoginEvent);
        if (securityLoginEvent.getId() == null) {
            return createSecurityLoginEvent(securityLoginEvent);
        }
        SecurityLoginEvent result = securityLoginEventService.save(securityLoginEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("securityLoginEvent", securityLoginEvent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /security-login-events : get all the securityLoginEvents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of securityLoginEvents in body
     */
    @RequestMapping(value = "/security-login-events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SecurityLoginEvent> getAllSecurityLoginEvents() {
        log.debug("REST request to get all SecurityLoginEvents");
        return securityLoginEventService.findAll();
    }

    /**
     * GET  /security-login-events/:id : get the "id" securityLoginEvent.
     *
     * @param id the id of the securityLoginEvent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the securityLoginEvent, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/security-login-events/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityLoginEvent> getSecurityLoginEvent(@PathVariable Long id) {
        log.debug("REST request to get SecurityLoginEvent : {}", id);
        SecurityLoginEvent securityLoginEvent = securityLoginEventService.findOne(id);
        return Optional.ofNullable(securityLoginEvent)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /security-login-events/:id : delete the "id" securityLoginEvent.
     *
     * @param id the id of the securityLoginEvent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/security-login-events/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSecurityLoginEvent(@PathVariable Long id) {
        log.debug("REST request to delete SecurityLoginEvent : {}", id);
        securityLoginEventService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("securityLoginEvent", id.toString())).build();
    }

    /**
     * SEARCH  /_search/security-login-events?query=:query : search for the securityLoginEvent corresponding
     * to the query.
     *
     * @param query the query of the securityLoginEvent search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/security-login-events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SecurityLoginEvent> searchSecurityLoginEvents(@RequestParam String query) {
        log.debug("REST request to search SecurityLoginEvents for query {}", query);
        return securityLoginEventService.search(query);
    }

}
