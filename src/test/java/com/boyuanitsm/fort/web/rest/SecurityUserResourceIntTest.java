package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.FortApp;
import com.boyuanitsm.fort.domain.SecurityUser;
import com.boyuanitsm.fort.repository.SecurityUserRepository;
import com.boyuanitsm.fort.service.SecurityUserService;
import com.boyuanitsm.fort.repository.search.SecurityUserSearchRepository;

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
 * Test class for the SecurityUserResource REST controller.
 *
 * @see SecurityUserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FortApp.class)
@WebAppConfiguration
@IntegrationTest
public class SecurityUserResourceIntTest {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_EMAIL = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;
    private static final String DEFAULT_ST = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ST = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SecurityUserRepository securityUserRepository;

    @Inject
    private SecurityUserService securityUserService;

    @Inject
    private SecurityUserSearchRepository securityUserSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSecurityUserMockMvc;

    private SecurityUser securityUser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityUserResource securityUserResource = new SecurityUserResource();
        ReflectionTestUtils.setField(securityUserResource, "securityUserService", securityUserService);
        this.restSecurityUserMockMvc = MockMvcBuilders.standaloneSetup(securityUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        securityUserSearchRepository.deleteAll();
        securityUser = new SecurityUser();
        securityUser.setLogin(DEFAULT_LOGIN);
        securityUser.setPasswordHash(DEFAULT_PASSWORD_HASH);
        securityUser.setEmail(DEFAULT_EMAIL);
        securityUser.setActivated(DEFAULT_ACTIVATED);
        securityUser.setSt(DEFAULT_ST);
    }

    @Test
    @Transactional
    public void createSecurityUser() throws Exception {
        int databaseSizeBeforeCreate = securityUserRepository.findAll().size();

        // Create the SecurityUser

        restSecurityUserMockMvc.perform(post("/api/security-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityUser)))
                .andExpect(status().isCreated());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUsers = securityUserRepository.findAll();
        assertThat(securityUsers).hasSize(databaseSizeBeforeCreate + 1);
        SecurityUser testSecurityUser = securityUsers.get(securityUsers.size() - 1);
        assertThat(testSecurityUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testSecurityUser.getPasswordHash()).isEqualTo(DEFAULT_PASSWORD_HASH);
        assertThat(testSecurityUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSecurityUser.isActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testSecurityUser.getSt()).isEqualTo(DEFAULT_ST);

        // Validate the SecurityUser in ElasticSearch
        SecurityUser securityUserEs = securityUserSearchRepository.findOne(testSecurityUser.getId());
        assertThat(securityUserEs).isEqualToComparingFieldByField(testSecurityUser);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityUserRepository.findAll().size();
        // set the field null
        securityUser.setLogin(null);

        // Create the SecurityUser, which fails.

        restSecurityUserMockMvc.perform(post("/api/security-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityUser)))
                .andExpect(status().isBadRequest());

        List<SecurityUser> securityUsers = securityUserRepository.findAll();
        assertThat(securityUsers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityUserRepository.findAll().size();
        // set the field null
        securityUser.setPasswordHash(null);

        // Create the SecurityUser, which fails.

        restSecurityUserMockMvc.perform(post("/api/security-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityUser)))
                .andExpect(status().isBadRequest());

        List<SecurityUser> securityUsers = securityUserRepository.findAll();
        assertThat(securityUsers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityUserRepository.findAll().size();
        // set the field null
        securityUser.setActivated(null);

        // Create the SecurityUser, which fails.

        restSecurityUserMockMvc.perform(post("/api/security-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityUser)))
                .andExpect(status().isBadRequest());

        List<SecurityUser> securityUsers = securityUserRepository.findAll();
        assertThat(securityUsers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSecurityUsers() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get all the securityUsers
        restSecurityUserMockMvc.perform(get("/api/security-users?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(securityUser.getId().intValue())))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
                .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
                .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }

    @Test
    @Transactional
    public void getSecurityUser() throws Exception {
        // Initialize the database
        securityUserRepository.saveAndFlush(securityUser);

        // Get the securityUser
        restSecurityUserMockMvc.perform(get("/api/security-users/{id}", securityUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(securityUser.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.passwordHash").value(DEFAULT_PASSWORD_HASH.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.st").value(DEFAULT_ST.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSecurityUser() throws Exception {
        // Get the securityUser
        restSecurityUserMockMvc.perform(get("/api/security-users/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecurityUser() throws Exception {
        // Initialize the database
        securityUserService.save(securityUser);

        int databaseSizeBeforeUpdate = securityUserRepository.findAll().size();

        // Update the securityUser
        SecurityUser updatedSecurityUser = new SecurityUser();
        updatedSecurityUser.setId(securityUser.getId());
        updatedSecurityUser.setLogin(UPDATED_LOGIN);
        updatedSecurityUser.setPasswordHash(UPDATED_PASSWORD_HASH);
        updatedSecurityUser.setEmail(UPDATED_EMAIL);
        updatedSecurityUser.setActivated(UPDATED_ACTIVATED);
        updatedSecurityUser.setSt(UPDATED_ST);

        restSecurityUserMockMvc.perform(put("/api/security-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSecurityUser)))
                .andExpect(status().isOk());

        // Validate the SecurityUser in the database
        List<SecurityUser> securityUsers = securityUserRepository.findAll();
        assertThat(securityUsers).hasSize(databaseSizeBeforeUpdate);
        SecurityUser testSecurityUser = securityUsers.get(securityUsers.size() - 1);
        assertThat(testSecurityUser.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testSecurityUser.getPasswordHash()).isEqualTo(UPDATED_PASSWORD_HASH);
        assertThat(testSecurityUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSecurityUser.isActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testSecurityUser.getSt()).isEqualTo(UPDATED_ST);

        // Validate the SecurityUser in ElasticSearch
        SecurityUser securityUserEs = securityUserSearchRepository.findOne(testSecurityUser.getId());
        assertThat(securityUserEs).isEqualToComparingFieldByField(testSecurityUser);
    }

    @Test
    @Transactional
    public void deleteSecurityUser() throws Exception {
        // Initialize the database
        securityUserService.save(securityUser);

        int databaseSizeBeforeDelete = securityUserRepository.findAll().size();

        // Get the securityUser
        restSecurityUserMockMvc.perform(delete("/api/security-users/{id}", securityUser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean securityUserExistsInEs = securityUserSearchRepository.exists(securityUser.getId());
        assertThat(securityUserExistsInEs).isFalse();

        // Validate the database is empty
        List<SecurityUser> securityUsers = securityUserRepository.findAll();
        assertThat(securityUsers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSecurityUser() throws Exception {
        // Initialize the database
        securityUserService.save(securityUser);

        // Search the securityUser
        restSecurityUserMockMvc.perform(get("/api/_search/security-users?query=id:" + securityUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].st").value(hasItem(DEFAULT_ST.toString())));
    }
}
