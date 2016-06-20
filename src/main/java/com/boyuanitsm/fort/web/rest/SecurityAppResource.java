package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.security.AuthoritiesConstants;
import com.boyuanitsm.fort.security.SecurityUtils;
import com.codahale.metrics.annotation.Timed;
import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.service.SecurityAppService;
import com.boyuanitsm.fort.web.rest.util.HeaderUtil;
import com.boyuanitsm.fort.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SecurityApp.
 */
@RestController
@RequestMapping("/api")
public class SecurityAppResource {

    private final Logger log = LoggerFactory.getLogger(SecurityAppResource.class);

    @Inject
    private SecurityAppService securityAppService;

    /**
     * POST  /security-apps : Create a new securityApp.
     *
     * @param securityApp the securityApp to create
     * @return the ResponseEntity with status 201 (Created) and with body the new securityApp, or with status 400 (Bad Request) if the securityApp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-apps",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityApp> createSecurityApp(@Valid @RequestBody SecurityApp securityApp) throws URISyntaxException {
        log.debug("REST request to save SecurityApp : {}", securityApp);
        if (securityApp.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("securityApp", "idexists", "A new securityApp cannot already have an ID")).body(null);
        }
        SecurityApp result = securityAppService.save(securityApp);
        return ResponseEntity.created(new URI("/api/security-apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("securityApp", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /security-apps : Updates an existing securityApp.
     *
     * @param securityApp the securityApp to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated securityApp,
     * or with status 400 (Bad Request) if the securityApp is not valid,
     * or with status 500 (Internal Server Error) if the securityApp couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/security-apps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityApp> updateSecurityApp(@Valid @RequestBody SecurityApp securityApp) throws URISyntaxException {
        log.debug("REST request to update SecurityApp : {}", securityApp);
        if (securityApp.getId() == null) {
            return createSecurityApp(securityApp);
        }
        SecurityApp result = securityAppService.save(securityApp);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("securityApp", securityApp.getId().toString()))
            .body(result);
    }

    /**
     * GET  /security-apps : get all the securityApps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of securityApps in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/security-apps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityApp>> getAllSecurityApps(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SecurityApps");
        Page<SecurityApp> page;

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SECURITY_APP)) {
            List<SecurityApp> list = new ArrayList<>();
            list.add(securityAppService.findCurrentSecurityApp());
            page = new PageImpl<>(list);
        } else {
            page = securityAppService.findAll(pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/security-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /security-apps/:id : get the "id" securityApp.
     *
     * @param id the id of the securityApp to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the securityApp, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/security-apps/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SecurityApp> getSecurityApp(@PathVariable Long id) {
        log.debug("REST request to get SecurityApp : {}", id);
        SecurityApp securityApp = securityAppService.findOne(id);
        return Optional.ofNullable(securityApp)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /security-apps/:id : delete the "id" securityApp.
     *
     * @param id the id of the securityApp to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/security-apps/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSecurityApp(@PathVariable Long id) {
        log.debug("REST request to delete SecurityApp : {}", id);
        securityAppService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("securityApp", id.toString())).build();
    }

    /**
     * SEARCH  /_search/security-apps?query=:query : search for the securityApp corresponding
     * to the query.
     *
     * @param query the query of the securityApp search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/security-apps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SecurityApp>> searchSecurityApps(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SecurityApps for query {}", query);
        Page<SecurityApp> page = securityAppService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/security-apps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * PUT  /security-apps : Reset an existing securityApp app secret.
     *
     * @param securityApp the securityApp to reset(only id)
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/security-apps/reset-app-secret",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> resetAppSecret(@RequestBody SecurityApp securityApp) {
        if (securityApp.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("securityApp", "idcannotbenull", "A securityApp id cannot be null")).body(null);
        }

        securityApp = securityAppService.findOne(securityApp.getId());

        if (securityApp == null) {
            return ResponseEntity.notFound().build();
        }

        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) && !securityApp.getCreatedBy().equals(SecurityUtils.getCurrentUserLogin())) {
            // this app is not current login created
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        securityAppService.resetAppSecret(securityApp);
        return ResponseEntity.ok(null);
    }
}
