package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.web.rest.dto.SecurityUserDTO;
import com.codahale.metrics.annotation.Timed;
import com.boyuanitsm.fort.domain.SecurityUser;
import com.boyuanitsm.fort.service.SecurityUserService;
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
 * REST controller for managing SecurityUser.
 */
@RestController
@RequestMapping("/api")
public class SecurityUserResource {

    private final Logger log = LoggerFactory.getLogger(SecurityUserResource.class);

    @Inject
    private SecurityUserService securityUserService;

    /**
     * POST  /security-users : Create a new securityUser.
     *
     * @param securityUser the securityUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new securityUser, or with status 400 (Bad Request) if the securityUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityUser> createSecurityUser(@Valid @RequestBody SecurityUser securityUser) throws URISyntaxException {
        log.debug("REST request to save SecurityUser : {}", securityUser);
        if (securityUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("securityUser", "idexists", "A new securityUser cannot already have an ID")).body(null);
        }
        SecurityUser result = securityUserService.save(securityUser);
        return ResponseEntity.created(new URI("/api/security-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("securityUser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /security-users : Updates an existing securityUser.
     *
     * @param securityUser the securityUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated securityUser,
     * or with status 400 (Bad Request) if the securityUser is not valid,
     * or with status 500 (Internal Server Error) if the securityUser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityUser> updateSecurityUser(@Valid @RequestBody SecurityUser securityUser) throws URISyntaxException {
        log.debug("REST request to update SecurityUser : {}", securityUser);
        if (securityUser.getId() == null) {
            return createSecurityUser(securityUser);
        }
        SecurityUser result = securityUserService.save(securityUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("securityUser", securityUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /security-users : get all the securityUsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of securityUsers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/security-users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityUser>> getAllSecurityUsers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SecurityUsers");
        Page<SecurityUser> page = securityUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/security-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /security-users/:id : get the "id" securityUser.
     *
     * @param id the id of the securityUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the securityUser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/security-users/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityUser> getSecurityUser(@PathVariable Long id) {
        log.debug("REST request to get SecurityUser : {}", id);
        SecurityUser securityUser = securityUserService.findOne(id);
        return Optional.ofNullable(securityUser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /security-users/:id : delete the "id" securityUser.
     *
     * @param id the id of the securityUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/security-users/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSecurityUser(@PathVariable Long id) {
        log.debug("REST request to delete SecurityUser : {}", id);
        securityUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("securityUser", id.toString())).build();
    }

    /**
     * SEARCH  /_search/security-users?query=:query : search for the securityUser corresponding
     * to the query.
     *
     * @param query the query of the securityUser search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/security-users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityUser>> searchSecurityUsers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SecurityUsers for query {}", query);
        Page<SecurityUser> page = securityUserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/security-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/security-user/authorization",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityUserDTO> authorization(@RequestBody SecurityUserDTO securityUserDTO) {
        SecurityUserDTO user = securityUserService.authorization(securityUserDTO);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }
}
