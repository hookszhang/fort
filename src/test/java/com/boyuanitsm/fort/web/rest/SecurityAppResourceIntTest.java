package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.FortApp;
import com.boyuanitsm.fort.domain.SecurityApp;
import com.boyuanitsm.fort.repository.SecurityAppRepository;
import com.boyuanitsm.fort.service.SecurityAppService;
import com.boyuanitsm.fort.repository.search.SecurityAppSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SecurityAppResource REST controller.
 *
 * @see SecurityAppResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FortApp.class)
@WebAppConfiguration
@IntegrationTest
public class SecurityAppResourceIntTest {

    private static final String DEFAULT_APP_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_APP_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_APP_KEY = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_APP_KEY = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_APP_SECRET = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_APP_SECRET = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ST = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ST = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SecurityAppRepository securityAppRepository;

    @Inject
    private SecurityAppService securityAppService;

    @Inject
    private SecurityAppSearchRepository securityAppSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSecurityAppMockMvc;

    private SecurityApp securityApp;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityAppResource securityAppResource = new SecurityAppResource();
        ReflectionTestUtils.setField(securityAppResource, "securityAppService", securityAppService);
        this.restSecurityAppMockMvc = MockMvcBuilders.standaloneSetup(securityAppResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        securityAppSearchRepository.deleteAll();
        securityApp = new SecurityApp();
        securityApp.setAppName(DEFAULT_APP_NAME);
        securityApp.setAppKey(DEFAULT_APP_KEY);
        securityApp.setAppSecret(DEFAULT_APP_SECRET);
        securityApp.setSt(DEFAULT_ST);
    }

    @Test
    @Transactional
    public void createSecurityApp() throws Exception {
        int databaseSizeBeforeCreate = securityAppRepository.findAll().size();

        // Create the SecurityApp

        restSecurityAppMockMvc.perform(post("/api/security-apps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityApp)))
                .andExpect(status().isCreated());

        // Validate the SecurityApp in the database
        List<SecurityApp> securityApps = securityAppRepository.findAll();
        assertThat(securityApps).hasSize(databaseSizeBeforeCreate + 1);
        SecurityApp testSecurityApp = securityApps.get(securityApps.size() - 1);
        assertThat(testSecurityApp.getAppName()).isEqualTo(DEFAULT_APP_NAME);
        // assertThat(testSecurityApp.getAppKey()).isEqualTo(DEFAULT_APP_KEY);
        // assertThat(testSecurityApp.getAppSecret()).isEqualTo(DEFAULT_APP_SECRET);
        assertThat(testSecurityApp.getSt()).isEqualTo(DEFAULT_ST);

        // Validate the SecurityApp in ElasticSearch
        SecurityApp securityAppEs = securityAppSearchRepository.findOne(testSecurityApp.getId());
        // assertThat(securityAppEs).isEqualToComparingFieldByField(testSecurityApp);
    }

    @Test
    @Transactional
    public void checkAppNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityAppRepository.findAll().size();
        // set the field null
        securityApp.setAppName(null);

        // Create the SecurityApp, which fails.

        restSecurityAppMockMvc.perform(post("/api/security-apps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityApp)))
                .andExpect(status().isBadRequest());

        List<SecurityApp> securityApps = securityAppRepository.findAll();
        assertThat(securityApps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSecurityApps() throws Exception {
        // Initialize the database
        securityAppRepository.saveAndFlush(securityApp);

        // Get all the securityApps
        restSecurityAppMockMvc.perform(get("/api/security-apps?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(securityApp.getId().intValue())))
                .andExpect(jsonPath("$.[*].appName").value(hasItem(DEFAULT_APP_NAME.toString())))
                .andExpect(jsonPath("$.[*].appKey").value(hasItem(DEFAULT_APP_KEY.toString())))
                .andExpect(jsonPath("$.[*].appSecret").value(hasItem(DEFAULT_APP_SECRET.toString())))
                .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }

    @Test
    @Transactional
    public void getSecurityApp() throws Exception {
        // Initialize the database
        securityAppRepository.saveAndFlush(securityApp);

        // Get the securityApp
        restSecurityAppMockMvc.perform(get("/api/security-apps/{id}", securityApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(securityApp.getId().intValue()))
            .andExpect(jsonPath("$.appName").value(DEFAULT_APP_NAME.toString()))
            .andExpect(jsonPath("$.appKey").value(DEFAULT_APP_KEY.toString()))
            .andExpect(jsonPath("$.appSecret").value(DEFAULT_APP_SECRET.toString()))
            .andExpect(jsonPath("$.st").value(DEFAULT_ST.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSecurityApp() throws Exception {
        // Get the securityApp
        restSecurityAppMockMvc.perform(get("/api/security-apps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecurityApp() throws Exception {
        // Initialize the database
        securityAppService.save(securityApp);

        int databaseSizeBeforeUpdate = securityAppRepository.findAll().size();

        // Update the securityApp
        SecurityApp updatedSecurityApp = new SecurityApp();
        updatedSecurityApp.setId(securityApp.getId());
        updatedSecurityApp.setAppName(UPDATED_APP_NAME);
        updatedSecurityApp.setAppKey(UPDATED_APP_KEY);
        updatedSecurityApp.setAppSecret(UPDATED_APP_SECRET);
        updatedSecurityApp.setSt(UPDATED_ST);

        restSecurityAppMockMvc.perform(put("/api/security-apps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSecurityApp)))
                .andExpect(status().isOk());

        // Validate the SecurityApp in the database
        List<SecurityApp> securityApps = securityAppRepository.findAll();
        assertThat(securityApps).hasSize(databaseSizeBeforeUpdate);
        SecurityApp testSecurityApp = securityApps.get(securityApps.size() - 1);
        assertThat(testSecurityApp.getAppName()).isEqualTo(UPDATED_APP_NAME);
        assertThat(testSecurityApp.getAppKey()).isEqualTo(UPDATED_APP_KEY);
        assertThat(testSecurityApp.getAppSecret()).isEqualTo(UPDATED_APP_SECRET);
        assertThat(testSecurityApp.getSt()).isEqualTo(UPDATED_ST);

        // Validate the SecurityApp in ElasticSearch
        SecurityApp securityAppEs = securityAppSearchRepository.findOne(testSecurityApp.getId());
        assertThat(securityAppEs).isEqualToComparingFieldByField(testSecurityApp);
    }

    @Test
    @Transactional
    public void deleteSecurityApp() throws Exception {
        // Initialize the database
        securityAppService.save(securityApp);

        int databaseSizeBeforeDelete = securityAppRepository.findAll().size();

        // Get the securityApp
        restSecurityAppMockMvc.perform(delete("/api/security-apps/{id}", securityApp.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean securityAppExistsInEs = securityAppSearchRepository.exists(securityApp.getId());
        assertThat(securityAppExistsInEs).isFalse();

        // Validate the database is empty
        List<SecurityApp> securityApps = securityAppRepository.findAll();
        assertThat(securityApps).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSecurityApp() throws Exception {
        // Initialize the database
        securityAppService.save(securityApp);

        // Search the securityApp
        restSecurityAppMockMvc.perform(get("/api/_search/security-apps?query=id:" + securityApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].appName").value(hasItem(DEFAULT_APP_NAME.toString())))
            .andExpect(jsonPath("$.[*].appKey").value(hasItem(DEFAULT_APP_KEY.toString())))
            .andExpect(jsonPath("$.[*].appSecret").value(hasItem(DEFAULT_APP_SECRET.toString())))
            .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }
}
