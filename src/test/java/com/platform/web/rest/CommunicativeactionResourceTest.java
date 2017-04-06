package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Communicativeaction;
import com.platform.repository.CommunicativeactionRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CommunicativeactionResource REST controller.
 *
 * @see CommunicativeactionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CommunicativeactionResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_ACTION_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_ACTION_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_ACTION_TIME_STR = dateTimeFormatter.print(DEFAULT_ACTION_TIME);
    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";
    private static final String DEFAULT_LANGUAGE = "AAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBB";

    @Inject
    private CommunicativeactionRepository communicativeactionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommunicativeactionMockMvc;

    private Communicativeaction communicativeaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommunicativeactionResource communicativeactionResource = new CommunicativeactionResource();
        ReflectionTestUtils.setField(communicativeactionResource, "communicativeactionRepository", communicativeactionRepository);
        this.restCommunicativeactionMockMvc = MockMvcBuilders.standaloneSetup(communicativeactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        communicativeaction = new Communicativeaction();
        communicativeaction.setActionTime(DEFAULT_ACTION_TIME);
        communicativeaction.setContent(DEFAULT_CONTENT);
        communicativeaction.setLanguage(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    public void createCommunicativeaction() throws Exception {
        int databaseSizeBeforeCreate = communicativeactionRepository.findAll().size();

        // Create the Communicativeaction

        restCommunicativeactionMockMvc.perform(post("/api/communicativeactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(communicativeaction)))
                .andExpect(status().isCreated());

        // Validate the Communicativeaction in the database
        List<Communicativeaction> communicativeactions = communicativeactionRepository.findAll();
        assertThat(communicativeactions).hasSize(databaseSizeBeforeCreate + 1);
        Communicativeaction testCommunicativeaction = communicativeactions.get(communicativeactions.size() - 1);
        assertThat(testCommunicativeaction.getActionTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_ACTION_TIME);
        assertThat(testCommunicativeaction.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testCommunicativeaction.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllCommunicativeactions() throws Exception {
        // Initialize the database
        communicativeactionRepository.saveAndFlush(communicativeaction);

        // Get all the communicativeactions
        restCommunicativeactionMockMvc.perform(get("/api/communicativeactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(communicativeaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].actionTime").value(hasItem(DEFAULT_ACTION_TIME_STR)))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }

    @Test
    @Transactional
    public void getCommunicativeaction() throws Exception {
        // Initialize the database
        communicativeactionRepository.saveAndFlush(communicativeaction);

        // Get the communicativeaction
        restCommunicativeactionMockMvc.perform(get("/api/communicativeactions/{id}", communicativeaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(communicativeaction.getId().intValue()))
            .andExpect(jsonPath("$.actionTime").value(DEFAULT_ACTION_TIME_STR))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommunicativeaction() throws Exception {
        // Get the communicativeaction
        restCommunicativeactionMockMvc.perform(get("/api/communicativeactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommunicativeaction() throws Exception {
        // Initialize the database
        communicativeactionRepository.saveAndFlush(communicativeaction);

		int databaseSizeBeforeUpdate = communicativeactionRepository.findAll().size();

        // Update the communicativeaction
        communicativeaction.setActionTime(UPDATED_ACTION_TIME);
        communicativeaction.setContent(UPDATED_CONTENT);
        communicativeaction.setLanguage(UPDATED_LANGUAGE);

        restCommunicativeactionMockMvc.perform(put("/api/communicativeactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(communicativeaction)))
                .andExpect(status().isOk());

        // Validate the Communicativeaction in the database
        List<Communicativeaction> communicativeactions = communicativeactionRepository.findAll();
        assertThat(communicativeactions).hasSize(databaseSizeBeforeUpdate);
        Communicativeaction testCommunicativeaction = communicativeactions.get(communicativeactions.size() - 1);
        assertThat(testCommunicativeaction.getActionTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_ACTION_TIME);
        assertThat(testCommunicativeaction.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testCommunicativeaction.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void deleteCommunicativeaction() throws Exception {
        // Initialize the database
        communicativeactionRepository.saveAndFlush(communicativeaction);

		int databaseSizeBeforeDelete = communicativeactionRepository.findAll().size();

        // Get the communicativeaction
        restCommunicativeactionMockMvc.perform(delete("/api/communicativeactions/{id}", communicativeaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Communicativeaction> communicativeactions = communicativeactionRepository.findAll();
        assertThat(communicativeactions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
