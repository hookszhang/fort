package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.FortApp;
import com.boyuanitsm.fort.domain.SecurityResourceEntity;
import com.boyuanitsm.fort.repository.SecurityResourceEntityRepository;
import com.boyuanitsm.fort.service.SecurityResourceEntityService;
import com.boyuanitsm.fort.repository.search.SecurityResourceEntitySearchRepository;

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

import com.boyuanitsm.fort.domain.enumeration.ResourceEntityType;

/**
 * Test class for the SecurityResourceEntityResource REST controller.
 *
 * @see SecurityResourceEntityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FortApp.class)
@WebAppConfiguration
@IntegrationTest
public class SecurityResourceEntityResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ResourceEntityType DEFAULT_RESOURCE_TYPE = ResourceEntityType.FUNCTION;
    private static final ResourceEntityType UPDATED_RESOURCE_TYPE = ResourceEntityType.NAV;
    private static final String DEFAULT_ST = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ST = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SecurityResourceEntityRepository securityResourceEntityRepository;

    @Inject
    private SecurityResourceEntityService securityResourceEntityService;

    @Inject
    private SecurityResourceEntitySearchRepository securityResourceEntitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSecurityResourceEntityMockMvc;

    private SecurityResourceEntity securityResourceEntity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityResourceEntityResource securityResourceEntityResource = new SecurityResourceEntityResource();
        ReflectionTestUtils.setField(securityResourceEntityResource, "securityResourceEntityService", securityResourceEntityService);
        this.restSecurityResourceEntityMockMvc = MockMvcBuilders.standaloneSetup(securityResourceEntityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        securityResourceEntitySearchRepository.deleteAll();
        securityResourceEntity = new SecurityResourceEntity();
        securityResourceEntity.setName(DEFAULT_NAME);
        securityResourceEntity.setUrl(DEFAULT_URL);
        securityResourceEntity.setDescription(DEFAULT_DESCRIPTION);
        securityResourceEntity.setResourceType(DEFAULT_RESOURCE_TYPE);
        securityResourceEntity.setSt(DEFAULT_ST);
    }

    @Test
    @Transactional
    public void createSecurityResourceEntity() throws Exception {
        int databaseSizeBeforeCreate = securityResourceEntityRepository.findAll().size();

        // Create the SecurityResourceEntity

        restSecurityResourceEntityMockMvc.perform(post("/api/security-resource-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityResourceEntity)))
                .andExpect(status().isCreated());

        // Validate the SecurityResourceEntity in the database
        List<SecurityResourceEntity> securityResourceEntities = securityResourceEntityRepository.findAll();
        assertThat(securityResourceEntities).hasSize(databaseSizeBeforeCreate + 1);
        SecurityResourceEntity testSecurityResourceEntity = securityResourceEntities.get(securityResourceEntities.size() - 1);
        assertThat(testSecurityResourceEntity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSecurityResourceEntity.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testSecurityResourceEntity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSecurityResourceEntity.getResourceType()).isEqualTo(DEFAULT_RESOURCE_TYPE);
        assertThat(testSecurityResourceEntity.getSt()).isEqualTo(DEFAULT_ST);

        // Validate the SecurityResourceEntity in ElasticSearch
        SecurityResourceEntity securityResourceEntityEs = securityResourceEntitySearchRepository.findOne(testSecurityResourceEntity.getId());
        assertThat(securityResourceEntityEs).isEqualToComparingFieldByField(testSecurityResourceEntity);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityResourceEntityRepository.findAll().size();
        // set the field null
        securityResourceEntity.setName(null);

        // Create the SecurityResourceEntity, which fails.

        restSecurityResourceEntityMockMvc.perform(post("/api/security-resource-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityResourceEntity)))
                .andExpect(status().isBadRequest());

        List<SecurityResourceEntity> securityResourceEntities = securityResourceEntityRepository.findAll();
        assertThat(securityResourceEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityResourceEntityRepository.findAll().size();
        // set the field null
        securityResourceEntity.setUrl(null);

        // Create the SecurityResourceEntity, which fails.

        restSecurityResourceEntityMockMvc.perform(post("/api/security-resource-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityResourceEntity)))
                .andExpect(status().isBadRequest());

        List<SecurityResourceEntity> securityResourceEntities = securityResourceEntityRepository.findAll();
        assertThat(securityResourceEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSecurityResourceEntities() throws Exception {
        // Initialize the database
        securityResourceEntityRepository.saveAndFlush(securityResourceEntity);

        // Get all the securityResourceEntities
        restSecurityResourceEntityMockMvc.perform(get("/api/security-resource-entities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(securityResourceEntity.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].resourceType").value(hasItem(DEFAULT_RESOURCE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }

    @Test
    @Transactional
    public void getSecurityResourceEntity() throws Exception {
        // Initialize the database
        securityResourceEntityRepository.saveAndFlush(securityResourceEntity);

        // Get the securityResourceEntity
        restSecurityResourceEntityMockMvc.perform(get("/api/security-resource-entities/{id}", securityResourceEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(securityResourceEntity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.resourceType").value(DEFAULT_RESOURCE_TYPE.toString()))
            .andExpect(jsonPath("$.st").value(DEFAULT_ST.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSecurityResourceEntity() throws Exception {
        // Get the securityResourceEntity
        restSecurityResourceEntityMockMvc.perform(get("/api/security-resource-entities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecurityResourceEntity() throws Exception {
        // Initialize the database
        securityResourceEntityService.save(securityResourceEntity);

        int databaseSizeBeforeUpdate = securityResourceEntityRepository.findAll().size();

        // Update the securityResourceEntity
        SecurityResourceEntity updatedSecurityResourceEntity = new SecurityResourceEntity();
        updatedSecurityResourceEntity.setId(securityResourceEntity.getId());
        updatedSecurityResourceEntity.setName(UPDATED_NAME);
        updatedSecurityResourceEntity.setUrl(UPDATED_URL);
        updatedSecurityResourceEntity.setDescription(UPDATED_DESCRIPTION);
        updatedSecurityResourceEntity.setResourceType(UPDATED_RESOURCE_TYPE);
        updatedSecurityResourceEntity.setSt(UPDATED_ST);

        restSecurityResourceEntityMockMvc.perform(put("/api/security-resource-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSecurityResourceEntity)))
                .andExpect(status().isOk());

        // Validate the SecurityResourceEntity in the database
        List<SecurityResourceEntity> securityResourceEntities = securityResourceEntityRepository.findAll();
        assertThat(securityResourceEntities).hasSize(databaseSizeBeforeUpdate);
        SecurityResourceEntity testSecurityResourceEntity = securityResourceEntities.get(securityResourceEntities.size() - 1);
        assertThat(testSecurityResourceEntity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSecurityResourceEntity.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSecurityResourceEntity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSecurityResourceEntity.getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
        assertThat(testSecurityResourceEntity.getSt()).isEqualTo(UPDATED_ST);

        // Validate the SecurityResourceEntity in ElasticSearch
        SecurityResourceEntity securityResourceEntityEs = securityResourceEntitySearchRepository.findOne(testSecurityResourceEntity.getId());
        assertThat(securityResourceEntityEs).isEqualToComparingFieldByField(testSecurityResourceEntity);
    }

    @Test
    @Transactional
    public void deleteSecurityResourceEntity() throws Exception {
        // Initialize the database
        securityResourceEntityService.save(securityResourceEntity);

        int databaseSizeBeforeDelete = securityResourceEntityRepository.findAll().size();

        // Get the securityResourceEntity
        restSecurityResourceEntityMockMvc.perform(delete("/api/security-resource-entities/{id}", securityResourceEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean securityResourceEntityExistsInEs = securityResourceEntitySearchRepository.exists(securityResourceEntity.getId());
        assertThat(securityResourceEntityExistsInEs).isFalse();

        // Validate the database is empty
        List<SecurityResourceEntity> securityResourceEntities = securityResourceEntityRepository.findAll();
        assertThat(securityResourceEntities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSecurityResourceEntity() throws Exception {
        // Initialize the database
        securityResourceEntityService.save(securityResourceEntity);

        // Search the securityResourceEntity
        restSecurityResourceEntityMockMvc.perform(get("/api/_search/security-resource-entities?query=id:" + securityResourceEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityResourceEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].resourceType").value(hasItem(DEFAULT_RESOURCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }
}
