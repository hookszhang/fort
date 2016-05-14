package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.FortApp;
import com.boyuanitsm.fort.domain.SecurityRole;
import com.boyuanitsm.fort.repository.SecurityRoleRepository;
import com.boyuanitsm.fort.service.SecurityRoleService;
import com.boyuanitsm.fort.repository.search.SecurityRoleSearchRepository;

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
 * Test class for the SecurityRoleResource REST controller.
 *
 * @see SecurityRoleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FortApp.class)
@WebAppConfiguration
@IntegrationTest
public class SecurityRoleResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_ST = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ST = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SecurityRoleRepository securityRoleRepository;

    @Inject
    private SecurityRoleService securityRoleService;

    @Inject
    private SecurityRoleSearchRepository securityRoleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSecurityRoleMockMvc;

    private SecurityRole securityRole;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityRoleResource securityRoleResource = new SecurityRoleResource();
        ReflectionTestUtils.setField(securityRoleResource, "securityRoleService", securityRoleService);
        this.restSecurityRoleMockMvc = MockMvcBuilders.standaloneSetup(securityRoleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        securityRoleSearchRepository.deleteAll();
        securityRole = new SecurityRole();
        securityRole.setName(DEFAULT_NAME);
        securityRole.setDescription(DEFAULT_DESCRIPTION);
        securityRole.setSt(DEFAULT_ST);
    }

    @Test
    @Transactional
    public void createSecurityRole() throws Exception {
        int databaseSizeBeforeCreate = securityRoleRepository.findAll().size();

        // Create the SecurityRole

        restSecurityRoleMockMvc.perform(post("/api/security-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityRole)))
                .andExpect(status().isCreated());

        // Validate the SecurityRole in the database
        List<SecurityRole> securityRoles = securityRoleRepository.findAll();
        assertThat(securityRoles).hasSize(databaseSizeBeforeCreate + 1);
        SecurityRole testSecurityRole = securityRoles.get(securityRoles.size() - 1);
        assertThat(testSecurityRole.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSecurityRole.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSecurityRole.getSt()).isEqualTo(DEFAULT_ST);

        // Validate the SecurityRole in ElasticSearch
        SecurityRole securityRoleEs = securityRoleSearchRepository.findOne(testSecurityRole.getId());
        assertThat(securityRoleEs).isEqualToComparingFieldByField(testSecurityRole);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityRoleRepository.findAll().size();
        // set the field null
        securityRole.setName(null);

        // Create the SecurityRole, which fails.

        restSecurityRoleMockMvc.perform(post("/api/security-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityRole)))
                .andExpect(status().isBadRequest());

        List<SecurityRole> securityRoles = securityRoleRepository.findAll();
        assertThat(securityRoles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSecurityRoles() throws Exception {
        // Initialize the database
        securityRoleRepository.saveAndFlush(securityRole);

        // Get all the securityRoles
        restSecurityRoleMockMvc.perform(get("/api/security-roles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(securityRole.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }

    @Test
    @Transactional
    public void getSecurityRole() throws Exception {
        // Initialize the database
        securityRoleRepository.saveAndFlush(securityRole);

        // Get the securityRole
        restSecurityRoleMockMvc.perform(get("/api/security-roles/{id}", securityRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(securityRole.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.st").value(DEFAULT_ST.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSecurityRole() throws Exception {
        // Get the securityRole
        restSecurityRoleMockMvc.perform(get("/api/security-roles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecurityRole() throws Exception {
        // Initialize the database
        securityRoleService.save(securityRole);

        int databaseSizeBeforeUpdate = securityRoleRepository.findAll().size();

        // Update the securityRole
        SecurityRole updatedSecurityRole = new SecurityRole();
        updatedSecurityRole.setId(securityRole.getId());
        updatedSecurityRole.setName(UPDATED_NAME);
        updatedSecurityRole.setDescription(UPDATED_DESCRIPTION);
        updatedSecurityRole.setSt(UPDATED_ST);

        restSecurityRoleMockMvc.perform(put("/api/security-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSecurityRole)))
                .andExpect(status().isOk());

        // Validate the SecurityRole in the database
        List<SecurityRole> securityRoles = securityRoleRepository.findAll();
        assertThat(securityRoles).hasSize(databaseSizeBeforeUpdate);
        SecurityRole testSecurityRole = securityRoles.get(securityRoles.size() - 1);
        assertThat(testSecurityRole.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSecurityRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSecurityRole.getSt()).isEqualTo(UPDATED_ST);

        // Validate the SecurityRole in ElasticSearch
        SecurityRole securityRoleEs = securityRoleSearchRepository.findOne(testSecurityRole.getId());
        assertThat(securityRoleEs).isEqualToComparingFieldByField(testSecurityRole);
    }

    @Test
    @Transactional
    public void deleteSecurityRole() throws Exception {
        // Initialize the database
        securityRoleService.save(securityRole);

        int databaseSizeBeforeDelete = securityRoleRepository.findAll().size();

        // Get the securityRole
        restSecurityRoleMockMvc.perform(delete("/api/security-roles/{id}", securityRole.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean securityRoleExistsInEs = securityRoleSearchRepository.exists(securityRole.getId());
        assertThat(securityRoleExistsInEs).isFalse();

        // Validate the database is empty
        List<SecurityRole> securityRoles = securityRoleRepository.findAll();
        assertThat(securityRoles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSecurityRole() throws Exception {
        // Initialize the database
        securityRoleService.save(securityRole);

        // Search the securityRole
        restSecurityRoleMockMvc.perform(get("/api/_search/security-roles?query=id:" + securityRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }
}
