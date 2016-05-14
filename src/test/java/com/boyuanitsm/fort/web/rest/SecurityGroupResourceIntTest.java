package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.FortApp;
import com.boyuanitsm.fort.domain.SecurityGroup;
import com.boyuanitsm.fort.repository.SecurityGroupRepository;
import com.boyuanitsm.fort.repository.search.SecurityGroupSearchRepository;

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
 * Test class for the SecurityGroupResource REST controller.
 *
 * @see SecurityGroupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FortApp.class)
@WebAppConfiguration
@IntegrationTest
public class SecurityGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_ST = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ST = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SecurityGroupRepository securityGroupRepository;

    @Inject
    private SecurityGroupSearchRepository securityGroupSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSecurityGroupMockMvc;

    private SecurityGroup securityGroup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityGroupResource securityGroupResource = new SecurityGroupResource();
        ReflectionTestUtils.setField(securityGroupResource, "securityGroupSearchRepository", securityGroupSearchRepository);
        ReflectionTestUtils.setField(securityGroupResource, "securityGroupRepository", securityGroupRepository);
        this.restSecurityGroupMockMvc = MockMvcBuilders.standaloneSetup(securityGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        securityGroupSearchRepository.deleteAll();
        securityGroup = new SecurityGroup();
        securityGroup.setName(DEFAULT_NAME);
        securityGroup.setDescription(DEFAULT_DESCRIPTION);
        securityGroup.setSt(DEFAULT_ST);
    }

    @Test
    @Transactional
    public void createSecurityGroup() throws Exception {
        int databaseSizeBeforeCreate = securityGroupRepository.findAll().size();

        // Create the SecurityGroup

        restSecurityGroupMockMvc.perform(post("/api/security-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityGroup)))
                .andExpect(status().isCreated());

        // Validate the SecurityGroup in the database
        List<SecurityGroup> securityGroups = securityGroupRepository.findAll();
        assertThat(securityGroups).hasSize(databaseSizeBeforeCreate + 1);
        SecurityGroup testSecurityGroup = securityGroups.get(securityGroups.size() - 1);
        assertThat(testSecurityGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSecurityGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSecurityGroup.getSt()).isEqualTo(DEFAULT_ST);

        // Validate the SecurityGroup in ElasticSearch
        SecurityGroup securityGroupEs = securityGroupSearchRepository.findOne(testSecurityGroup.getId());
        assertThat(securityGroupEs).isEqualToComparingFieldByField(testSecurityGroup);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityGroupRepository.findAll().size();
        // set the field null
        securityGroup.setName(null);

        // Create the SecurityGroup, which fails.

        restSecurityGroupMockMvc.perform(post("/api/security-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityGroup)))
                .andExpect(status().isBadRequest());

        List<SecurityGroup> securityGroups = securityGroupRepository.findAll();
        assertThat(securityGroups).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSecurityGroups() throws Exception {
        // Initialize the database
        securityGroupRepository.saveAndFlush(securityGroup);

        // Get all the securityGroups
        restSecurityGroupMockMvc.perform(get("/api/security-groups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(securityGroup.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }

    @Test
    @Transactional
    public void getSecurityGroup() throws Exception {
        // Initialize the database
        securityGroupRepository.saveAndFlush(securityGroup);

        // Get the securityGroup
        restSecurityGroupMockMvc.perform(get("/api/security-groups/{id}", securityGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(securityGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.st").value(DEFAULT_ST.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSecurityGroup() throws Exception {
        // Get the securityGroup
        restSecurityGroupMockMvc.perform(get("/api/security-groups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecurityGroup() throws Exception {
        // Initialize the database
        securityGroupRepository.saveAndFlush(securityGroup);
        securityGroupSearchRepository.save(securityGroup);
        int databaseSizeBeforeUpdate = securityGroupRepository.findAll().size();

        // Update the securityGroup
        SecurityGroup updatedSecurityGroup = new SecurityGroup();
        updatedSecurityGroup.setId(securityGroup.getId());
        updatedSecurityGroup.setName(UPDATED_NAME);
        updatedSecurityGroup.setDescription(UPDATED_DESCRIPTION);
        updatedSecurityGroup.setSt(UPDATED_ST);

        restSecurityGroupMockMvc.perform(put("/api/security-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSecurityGroup)))
                .andExpect(status().isOk());

        // Validate the SecurityGroup in the database
        List<SecurityGroup> securityGroups = securityGroupRepository.findAll();
        assertThat(securityGroups).hasSize(databaseSizeBeforeUpdate);
        SecurityGroup testSecurityGroup = securityGroups.get(securityGroups.size() - 1);
        assertThat(testSecurityGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSecurityGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSecurityGroup.getSt()).isEqualTo(UPDATED_ST);

        // Validate the SecurityGroup in ElasticSearch
        SecurityGroup securityGroupEs = securityGroupSearchRepository.findOne(testSecurityGroup.getId());
        assertThat(securityGroupEs).isEqualToComparingFieldByField(testSecurityGroup);
    }

    @Test
    @Transactional
    public void deleteSecurityGroup() throws Exception {
        // Initialize the database
        securityGroupRepository.saveAndFlush(securityGroup);
        securityGroupSearchRepository.save(securityGroup);
        int databaseSizeBeforeDelete = securityGroupRepository.findAll().size();

        // Get the securityGroup
        restSecurityGroupMockMvc.perform(delete("/api/security-groups/{id}", securityGroup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean securityGroupExistsInEs = securityGroupSearchRepository.exists(securityGroup.getId());
        assertThat(securityGroupExistsInEs).isFalse();

        // Validate the database is empty
        List<SecurityGroup> securityGroups = securityGroupRepository.findAll();
        assertThat(securityGroups).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSecurityGroup() throws Exception {
        // Initialize the database
        securityGroupRepository.saveAndFlush(securityGroup);
        securityGroupSearchRepository.save(securityGroup);

        // Search the securityGroup
        restSecurityGroupMockMvc.perform(get("/api/_search/security-groups?query=id:" + securityGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }
}
