package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.FortApp;
import com.boyuanitsm.fort.domain.SecurityAuthority;
import com.boyuanitsm.fort.repository.SecurityAuthorityRepository;
import com.boyuanitsm.fort.service.SecurityAuthorityService;
import com.boyuanitsm.fort.repository.search.SecurityAuthoritySearchRepository;

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
 * Test class for the SecurityAuthorityResource REST controller.
 *
 * @see SecurityAuthorityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FortApp.class)
@WebAppConfiguration
@IntegrationTest
public class SecurityAuthorityResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_ST = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ST = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SecurityAuthorityRepository securityAuthorityRepository;

    @Inject
    private SecurityAuthorityService securityAuthorityService;

    @Inject
    private SecurityAuthoritySearchRepository securityAuthoritySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSecurityAuthorityMockMvc;

    private SecurityAuthority securityAuthority;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityAuthorityResource securityAuthorityResource = new SecurityAuthorityResource();
        ReflectionTestUtils.setField(securityAuthorityResource, "securityAuthorityService", securityAuthorityService);
        this.restSecurityAuthorityMockMvc = MockMvcBuilders.standaloneSetup(securityAuthorityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        securityAuthoritySearchRepository.deleteAll();
        securityAuthority = new SecurityAuthority();
        securityAuthority.setName(DEFAULT_NAME);
        securityAuthority.setDescription(DEFAULT_DESCRIPTION);
        securityAuthority.setSt(DEFAULT_ST);
    }

    @Test
    @Transactional
    public void createSecurityAuthority() throws Exception {
        int databaseSizeBeforeCreate = securityAuthorityRepository.findAll().size();

        // Create the SecurityAuthority

        restSecurityAuthorityMockMvc.perform(post("/api/security-authorities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityAuthority)))
                .andExpect(status().isCreated());

        // Validate the SecurityAuthority in the database
        List<SecurityAuthority> securityAuthorities = securityAuthorityRepository.findAll();
        assertThat(securityAuthorities).hasSize(databaseSizeBeforeCreate + 1);
        SecurityAuthority testSecurityAuthority = securityAuthorities.get(securityAuthorities.size() - 1);
        assertThat(testSecurityAuthority.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSecurityAuthority.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSecurityAuthority.getSt()).isEqualTo(DEFAULT_ST);

        // Validate the SecurityAuthority in ElasticSearch
        SecurityAuthority securityAuthorityEs = securityAuthoritySearchRepository.findOne(testSecurityAuthority.getId());
        assertThat(securityAuthorityEs).isEqualToComparingFieldByField(testSecurityAuthority);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityAuthorityRepository.findAll().size();
        // set the field null
        securityAuthority.setName(null);

        // Create the SecurityAuthority, which fails.

        restSecurityAuthorityMockMvc.perform(post("/api/security-authorities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityAuthority)))
                .andExpect(status().isBadRequest());

        List<SecurityAuthority> securityAuthorities = securityAuthorityRepository.findAll();
        assertThat(securityAuthorities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSecurityAuthorities() throws Exception {
        // Initialize the database
        securityAuthorityRepository.saveAndFlush(securityAuthority);

        // Get all the securityAuthorities
        restSecurityAuthorityMockMvc.perform(get("/api/security-authorities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(securityAuthority.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }

    @Test
    @Transactional
    public void getSecurityAuthority() throws Exception {
        // Initialize the database
        securityAuthorityRepository.saveAndFlush(securityAuthority);

        // Get the securityAuthority
        restSecurityAuthorityMockMvc.perform(get("/api/security-authorities/{id}", securityAuthority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(securityAuthority.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.st").value(DEFAULT_ST.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSecurityAuthority() throws Exception {
        // Get the securityAuthority
        restSecurityAuthorityMockMvc.perform(get("/api/security-authorities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecurityAuthority() throws Exception {
        // Initialize the database
        securityAuthorityService.save(securityAuthority);

        int databaseSizeBeforeUpdate = securityAuthorityRepository.findAll().size();

        // Update the securityAuthority
        SecurityAuthority updatedSecurityAuthority = new SecurityAuthority();
        updatedSecurityAuthority.setId(securityAuthority.getId());
        updatedSecurityAuthority.setName(UPDATED_NAME);
        updatedSecurityAuthority.setDescription(UPDATED_DESCRIPTION);
        updatedSecurityAuthority.setSt(UPDATED_ST);

        restSecurityAuthorityMockMvc.perform(put("/api/security-authorities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSecurityAuthority)))
                .andExpect(status().isOk());

        // Validate the SecurityAuthority in the database
        List<SecurityAuthority> securityAuthorities = securityAuthorityRepository.findAll();
        assertThat(securityAuthorities).hasSize(databaseSizeBeforeUpdate);
        SecurityAuthority testSecurityAuthority = securityAuthorities.get(securityAuthorities.size() - 1);
        assertThat(testSecurityAuthority.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSecurityAuthority.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSecurityAuthority.getSt()).isEqualTo(UPDATED_ST);

        // Validate the SecurityAuthority in ElasticSearch
        SecurityAuthority securityAuthorityEs = securityAuthoritySearchRepository.findOne(testSecurityAuthority.getId());
        assertThat(securityAuthorityEs).isEqualToComparingFieldByField(testSecurityAuthority);
    }

    @Test
    @Transactional
    public void deleteSecurityAuthority() throws Exception {
        // Initialize the database
        securityAuthorityService.save(securityAuthority);

        int databaseSizeBeforeDelete = securityAuthorityRepository.findAll().size();

        // Get the securityAuthority
        restSecurityAuthorityMockMvc.perform(delete("/api/security-authorities/{id}", securityAuthority.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean securityAuthorityExistsInEs = securityAuthoritySearchRepository.exists(securityAuthority.getId());
        assertThat(securityAuthorityExistsInEs).isFalse();

        // Validate the database is empty
        List<SecurityAuthority> securityAuthorities = securityAuthorityRepository.findAll();
        assertThat(securityAuthorities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSecurityAuthority() throws Exception {
        // Initialize the database
        securityAuthorityService.save(securityAuthority);

        // Search the securityAuthority
        restSecurityAuthorityMockMvc.perform(get("/api/_search/security-authorities?query=id:" + securityAuthority.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityAuthority.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }
}
