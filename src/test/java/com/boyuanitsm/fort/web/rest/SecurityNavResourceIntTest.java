package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.FortApp;
import com.boyuanitsm.fort.domain.SecurityNav;
import com.boyuanitsm.fort.repository.SecurityNavRepository;
import com.boyuanitsm.fort.service.SecurityNavService;
import com.boyuanitsm.fort.repository.search.SecurityNavSearchRepository;

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
 * Test class for the SecurityNavResource REST controller.
 *
 * @see SecurityNavResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FortApp.class)
@WebAppConfiguration
@IntegrationTest
public class SecurityNavResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ICON = "AAAAA";
    private static final String UPDATED_ICON = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_ST = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ST = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SecurityNavRepository securityNavRepository;

    @Inject
    private SecurityNavService securityNavService;

    @Inject
    private SecurityNavSearchRepository securityNavSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSecurityNavMockMvc;

    private SecurityNav securityNav;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityNavResource securityNavResource = new SecurityNavResource();
        ReflectionTestUtils.setField(securityNavResource, "securityNavService", securityNavService);
        this.restSecurityNavMockMvc = MockMvcBuilders.standaloneSetup(securityNavResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        securityNavSearchRepository.deleteAll();
        securityNav = new SecurityNav();
        securityNav.setName(DEFAULT_NAME);
        securityNav.setIcon(DEFAULT_ICON);
        securityNav.setDescription(DEFAULT_DESCRIPTION);
        securityNav.setSt(DEFAULT_ST);
    }

    @Test
    @Transactional
    public void createSecurityNav() throws Exception {
        int databaseSizeBeforeCreate = securityNavRepository.findAll().size();

        // Create the SecurityNav

        restSecurityNavMockMvc.perform(post("/api/security-navs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityNav)))
                .andExpect(status().isCreated());

        // Validate the SecurityNav in the database
        List<SecurityNav> securityNavs = securityNavRepository.findAll();
        assertThat(securityNavs).hasSize(databaseSizeBeforeCreate + 1);
        SecurityNav testSecurityNav = securityNavs.get(securityNavs.size() - 1);
        assertThat(testSecurityNav.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSecurityNav.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testSecurityNav.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSecurityNav.getSt()).isEqualTo(DEFAULT_ST);

        // Validate the SecurityNav in ElasticSearch
        SecurityNav securityNavEs = securityNavSearchRepository.findOne(testSecurityNav.getId());
        assertThat(securityNavEs).isEqualToComparingFieldByField(testSecurityNav);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityNavRepository.findAll().size();
        // set the field null
        securityNav.setName(null);

        // Create the SecurityNav, which fails.

        restSecurityNavMockMvc.perform(post("/api/security-navs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityNav)))
                .andExpect(status().isBadRequest());

        List<SecurityNav> securityNavs = securityNavRepository.findAll();
        assertThat(securityNavs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSecurityNavs() throws Exception {
        // Initialize the database
        securityNavRepository.saveAndFlush(securityNav);

        // Get all the securityNavs
        restSecurityNavMockMvc.perform(get("/api/security-navs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(securityNav.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }

    @Test
    @Transactional
    public void getSecurityNav() throws Exception {
        // Initialize the database
        securityNavRepository.saveAndFlush(securityNav);

        // Get the securityNav
        restSecurityNavMockMvc.perform(get("/api/security-navs/{id}", securityNav.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(securityNav.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.st").value(DEFAULT_ST.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSecurityNav() throws Exception {
        // Get the securityNav
        restSecurityNavMockMvc.perform(get("/api/security-navs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecurityNav() throws Exception {
        // Initialize the database
        securityNavService.save(securityNav);

        int databaseSizeBeforeUpdate = securityNavRepository.findAll().size();

        // Update the securityNav
        SecurityNav updatedSecurityNav = new SecurityNav();
        updatedSecurityNav.setId(securityNav.getId());
        updatedSecurityNav.setName(UPDATED_NAME);
        updatedSecurityNav.setIcon(UPDATED_ICON);
        updatedSecurityNav.setDescription(UPDATED_DESCRIPTION);
        updatedSecurityNav.setSt(UPDATED_ST);

        restSecurityNavMockMvc.perform(put("/api/security-navs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSecurityNav)))
                .andExpect(status().isOk());

        // Validate the SecurityNav in the database
        List<SecurityNav> securityNavs = securityNavRepository.findAll();
        assertThat(securityNavs).hasSize(databaseSizeBeforeUpdate);
        SecurityNav testSecurityNav = securityNavs.get(securityNavs.size() - 1);
        assertThat(testSecurityNav.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSecurityNav.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testSecurityNav.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSecurityNav.getSt()).isEqualTo(UPDATED_ST);

        // Validate the SecurityNav in ElasticSearch
        SecurityNav securityNavEs = securityNavSearchRepository.findOne(testSecurityNav.getId());
        assertThat(securityNavEs).isEqualToComparingFieldByField(testSecurityNav);
    }

    @Test
    @Transactional
    public void deleteSecurityNav() throws Exception {
        // Initialize the database
        securityNavService.save(securityNav);

        int databaseSizeBeforeDelete = securityNavRepository.findAll().size();

        // Get the securityNav
        restSecurityNavMockMvc.perform(delete("/api/security-navs/{id}", securityNav.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean securityNavExistsInEs = securityNavSearchRepository.exists(securityNav.getId());
        assertThat(securityNavExistsInEs).isFalse();

        // Validate the database is empty
        List<SecurityNav> securityNavs = securityNavRepository.findAll();
        assertThat(securityNavs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSecurityNav() throws Exception {
        // Initialize the database
        securityNavService.save(securityNav);

        // Search the securityNav
        restSecurityNavMockMvc.perform(get("/api/_search/security-navs?query=id:" + securityNav.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityNav.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }
}
