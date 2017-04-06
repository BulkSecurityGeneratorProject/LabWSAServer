package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Presencerequest;
import com.platform.repository.PresencerequestRepository;

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
 * Test class for the PresencerequestResource REST controller.
 *
 * @see PresencerequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PresencerequestResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final DateTime DEFAULT_PRESENCE_REQUEST_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_PRESENCE_REQUEST_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_PRESENCE_REQUEST_TIME_STR = dateTimeFormatter.print(DEFAULT_PRESENCE_REQUEST_TIME);

    @Inject
    private PresencerequestRepository presencerequestRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPresencerequestMockMvc;

    private Presencerequest presencerequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PresencerequestResource presencerequestResource = new PresencerequestResource();
        ReflectionTestUtils.setField(presencerequestResource, "presencerequestRepository", presencerequestRepository);
        this.restPresencerequestMockMvc = MockMvcBuilders.standaloneSetup(presencerequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        presencerequest = new Presencerequest();
        presencerequest.setPresenceRequestTime(DEFAULT_PRESENCE_REQUEST_TIME);
    }

    @Test
    @Transactional
    public void createPresencerequest() throws Exception {
        int databaseSizeBeforeCreate = presencerequestRepository.findAll().size();

        // Create the Presencerequest

        restPresencerequestMockMvc.perform(post("/api/presencerequests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(presencerequest)))
                .andExpect(status().isCreated());

        // Validate the Presencerequest in the database
        List<Presencerequest> presencerequests = presencerequestRepository.findAll();
        assertThat(presencerequests).hasSize(databaseSizeBeforeCreate + 1);
        Presencerequest testPresencerequest = presencerequests.get(presencerequests.size() - 1);
        assertThat(testPresencerequest.getPresenceRequestTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_PRESENCE_REQUEST_TIME);
    }

    @Test
    @Transactional
    public void getAllPresencerequests() throws Exception {
        // Initialize the database
        presencerequestRepository.saveAndFlush(presencerequest);

        // Get all the presencerequests
        restPresencerequestMockMvc.perform(get("/api/presencerequests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(presencerequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].presenceRequestTime").value(hasItem(DEFAULT_PRESENCE_REQUEST_TIME_STR)));
    }

    @Test
    @Transactional
    public void getPresencerequest() throws Exception {
        // Initialize the database
        presencerequestRepository.saveAndFlush(presencerequest);

        // Get the presencerequest
        restPresencerequestMockMvc.perform(get("/api/presencerequests/{id}", presencerequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(presencerequest.getId().intValue()))
            .andExpect(jsonPath("$.presenceRequestTime").value(DEFAULT_PRESENCE_REQUEST_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPresencerequest() throws Exception {
        // Get the presencerequest
        restPresencerequestMockMvc.perform(get("/api/presencerequests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePresencerequest() throws Exception {
        // Initialize the database
        presencerequestRepository.saveAndFlush(presencerequest);

		int databaseSizeBeforeUpdate = presencerequestRepository.findAll().size();

        // Update the presencerequest
        presencerequest.setPresenceRequestTime(UPDATED_PRESENCE_REQUEST_TIME);

        restPresencerequestMockMvc.perform(put("/api/presencerequests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(presencerequest)))
                .andExpect(status().isOk());

        // Validate the Presencerequest in the database
        List<Presencerequest> presencerequests = presencerequestRepository.findAll();
        assertThat(presencerequests).hasSize(databaseSizeBeforeUpdate);
        Presencerequest testPresencerequest = presencerequests.get(presencerequests.size() - 1);
        assertThat(testPresencerequest.getPresenceRequestTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_PRESENCE_REQUEST_TIME);
    }

    @Test
    @Transactional
    public void deletePresencerequest() throws Exception {
        // Initialize the database
        presencerequestRepository.saveAndFlush(presencerequest);

		int databaseSizeBeforeDelete = presencerequestRepository.findAll().size();

        // Get the presencerequest
        restPresencerequestMockMvc.perform(delete("/api/presencerequests/{id}", presencerequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Presencerequest> presencerequests = presencerequestRepository.findAll();
        assertThat(presencerequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
