package com.boyuanitsm.fort.web.rest;

import com.boyuanitsm.fort.FortApp;
import com.boyuanitsm.fort.domain.SecurityLoginEvent;
import com.boyuanitsm.fort.repository.SecurityLoginEventRepository;
import com.boyuanitsm.fort.service.SecurityLoginEventService;
import com.boyuanitsm.fort.repository.search.SecurityLoginEventSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SecurityLoginEventResource REST controller.
 *
 * @see SecurityLoginEventResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FortApp.class)
@WebAppConfiguration
@IntegrationTest
public class SecurityLoginEventResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TOKEN_VALUE = "AAAAA";
    private static final String UPDATED_TOKEN_VALUE = "BBBBB";

    private static final ZonedDateTime DEFAULT_TOKEN_OVERDUE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TOKEN_OVERDUE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TOKEN_OVERDUE_TIME_STR = dateTimeFormatter.format(DEFAULT_TOKEN_OVERDUE_TIME);
    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_USER_AGENT = "AAAAA";
    private static final String UPDATED_USER_AGENT = "BBBBB";

    @Inject
    private SecurityLoginEventRepository securityLoginEventRepository;

    @Inject
    private SecurityLoginEventService securityLoginEventService;

    @Inject
    private SecurityLoginEventSearchRepository securityLoginEventSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSecurityLoginEventMockMvc;

    private SecurityLoginEvent securityLoginEvent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecurityLoginEventResource securityLoginEventResource = new SecurityLoginEventResource();
        ReflectionTestUtils.setField(securityLoginEventResource, "securityLoginEventService", securityLoginEventService);
        this.restSecurityLoginEventMockMvc = MockMvcBuilders.standaloneSetup(securityLoginEventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        securityLoginEventSearchRepository.deleteAll();
        securityLoginEvent = new SecurityLoginEvent();
        securityLoginEvent.setTokenValue(DEFAULT_TOKEN_VALUE);
        securityLoginEvent.setTokenOverdueTime(DEFAULT_TOKEN_OVERDUE_TIME);
        securityLoginEvent.setIpAddress(DEFAULT_IP_ADDRESS);
        securityLoginEvent.setUserAgent(DEFAULT_USER_AGENT);
    }

    @Test
    @Transactional
    public void createSecurityLoginEvent() throws Exception {
        int databaseSizeBeforeCreate = securityLoginEventRepository.findAll().size();

        // Create the SecurityLoginEvent

        restSecurityLoginEventMockMvc.perform(post("/api/security-login-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(securityLoginEvent)))
                .andExpect(status().isCreated());

        // Validate the SecurityLoginEvent in the database
        List<SecurityLoginEvent> securityLoginEvents = securityLoginEventRepository.findAll();
        assertThat(securityLoginEvents).hasSize(databaseSizeBeforeCreate + 1);
        SecurityLoginEvent testSecurityLoginEvent = securityLoginEvents.get(securityLoginEvents.size() - 1);
        assertThat(testSecurityLoginEvent.getTokenValue()).isEqualTo(DEFAULT_TOKEN_VALUE);
        assertThat(testSecurityLoginEvent.getTokenOverdueTime()).isEqualTo(DEFAULT_TOKEN_OVERDUE_TIME);
        assertThat(testSecurityLoginEvent.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testSecurityLoginEvent.getUserAgent()).isEqualTo(DEFAULT_USER_AGENT);

        // Validate the SecurityLoginEvent in ElasticSearch
        SecurityLoginEvent securityLoginEventEs = securityLoginEventSearchRepository.findOne(testSecurityLoginEvent.getId());
        assertThat(securityLoginEventEs).isEqualToComparingFieldByField(testSecurityLoginEvent);
    }

    @Test
    @Transactional
    public void getAllSecurityLoginEvents() throws Exception {
        // Initialize the database
        securityLoginEventRepository.saveAndFlush(securityLoginEvent);

        // Get all the securityLoginEvents
        restSecurityLoginEventMockMvc.perform(get("/api/security-login-events?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(securityLoginEvent.getId().intValue())))
                .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE.toString())))
                .andExpect(jsonPath("$.[*].tokenOverdueTime").value(hasItem(DEFAULT_TOKEN_OVERDUE_TIME_STR)))
                .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].userAgent").value(hasItem(DEFAULT_USER_AGENT.toString())));
    }

    @Test
    @Transactional
    public void getSecurityLoginEvent() throws Exception {
        // Initialize the database
        securityLoginEventRepository.saveAndFlush(securityLoginEvent);

        // Get the securityLoginEvent
        restSecurityLoginEventMockMvc.perform(get("/api/security-login-events/{id}", securityLoginEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(securityLoginEvent.getId().intValue()))
            .andExpect(jsonPath("$.tokenValue").value(DEFAULT_TOKEN_VALUE.toString()))
            .andExpect(jsonPath("$.tokenOverdueTime").value(DEFAULT_TOKEN_OVERDUE_TIME_STR))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS.toString()))
            .andExpect(jsonPath("$.userAgent").value(DEFAULT_USER_AGENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSecurityLoginEvent() throws Exception {
        // Get the securityLoginEvent
        restSecurityLoginEventMockMvc.perform(get("/api/security-login-events/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecurityLoginEvent() throws Exception {
        // Initialize the database
        securityLoginEventService.save(securityLoginEvent);

        int databaseSizeBeforeUpdate = securityLoginEventRepository.findAll().size();

        // Update the securityLoginEvent
        SecurityLoginEvent updatedSecurityLoginEvent = new SecurityLoginEvent();
        updatedSecurityLoginEvent.setId(securityLoginEvent.getId());
        updatedSecurityLoginEvent.setTokenValue(UPDATED_TOKEN_VALUE);
        updatedSecurityLoginEvent.setTokenOverdueTime(UPDATED_TOKEN_OVERDUE_TIME);
        updatedSecurityLoginEvent.setIpAddress(UPDATED_IP_ADDRESS);
        updatedSecurityLoginEvent.setUserAgent(UPDATED_USER_AGENT);

        restSecurityLoginEventMockMvc.perform(put("/api/security-login-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSecurityLoginEvent)))
                .andExpect(status().isOk());

        // Validate the SecurityLoginEvent in the database
        List<SecurityLoginEvent> securityLoginEvents = securityLoginEventRepository.findAll();
        assertThat(securityLoginEvents).hasSize(databaseSizeBeforeUpdate);
        SecurityLoginEvent testSecurityLoginEvent = securityLoginEvents.get(securityLoginEvents.size() - 1);
        assertThat(testSecurityLoginEvent.getTokenValue()).isEqualTo(UPDATED_TOKEN_VALUE);
        assertThat(testSecurityLoginEvent.getTokenOverdueTime()).isEqualTo(UPDATED_TOKEN_OVERDUE_TIME);
        assertThat(testSecurityLoginEvent.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testSecurityLoginEvent.getUserAgent()).isEqualTo(UPDATED_USER_AGENT);

        // Validate the SecurityLoginEvent in ElasticSearch
        SecurityLoginEvent securityLoginEventEs = securityLoginEventSearchRepository.findOne(testSecurityLoginEvent.getId());
        assertThat(securityLoginEventEs).isEqualToComparingFieldByField(testSecurityLoginEvent);
    }

    @Test
    @Transactional
    public void deleteSecurityLoginEvent() throws Exception {
        // Initialize the database
        securityLoginEventService.save(securityLoginEvent);

        int databaseSizeBeforeDelete = securityLoginEventRepository.findAll().size();

        // Get the securityLoginEvent
        restSecurityLoginEventMockMvc.perform(delete("/api/security-login-events/{id}", securityLoginEvent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean securityLoginEventExistsInEs = securityLoginEventSearchRepository.exists(securityLoginEvent.getId());
        assertThat(securityLoginEventExistsInEs).isFalse();

        // Validate the database is empty
        List<SecurityLoginEvent> securityLoginEvents = securityLoginEventRepository.findAll();
        assertThat(securityLoginEvents).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSecurityLoginEvent() throws Exception {
        // Initialize the database
        securityLoginEventService.save(securityLoginEvent);

        // Search the securityLoginEvent
        restSecurityLoginEventMockMvc.perform(get("/api/_search/security-login-events?query=id:" + securityLoginEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityLoginEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE.toString())))
            .andExpect(jsonPath("$.[*].tokenOverdueTime").value(hasItem(DEFAULT_TOKEN_OVERDUE_TIME_STR)))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].userAgent").value(hasItem(DEFAULT_USER_AGENT.toString())));
    }
}
